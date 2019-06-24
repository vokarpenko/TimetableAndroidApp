package kubsu.timetable.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.login.LoginRepository;
import kubsu.timetable.R;
import kubsu.timetable.retrofit.NetworkService;
import kubsu.timetable.utility.L;

import static kubsu.timetable.utility.Constant.PREF_AUTH_TOKEN;
import static kubsu.timetable.utility.Constant.PREF_FILE;
import static kubsu.timetable.utility.Constant.PREF_GROUP;
import static kubsu.timetable.utility.Constant.PREF_SUBGROUP;
import static kubsu.timetable.utility.Constant.PREF_USER_NAME;
import static kubsu.timetable.utility.Constant.PREF_USER_PATRONYMIC;
import static kubsu.timetable.utility.Constant.PREF_USER_SURNAME;

class NotesRepository {

    void timestampsToDate(List<Note> notes) {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd MMM HH:mm",new Locale("ru"));
        Date date = Calendar.getInstance().getTime();
        for (Note note:
             notes) {
            date.setTime(note.getTimestamp()*1000);
            String strDate = simpleDateFormat.format(date);
            note.setDate(strDate);
        }
    }

    String getStringDateNow(){
        DateFormat simpleDateFormat = new SimpleDateFormat("dd MMM HH:mm",new Locale("ru"));
        Date date = Calendar.getInstance().getTime();
        return simpleDateFormat.format(date);
    }

    interface NoteAddCallback{
        void add(Note note);
        void nopeText();
    }

    private Context context;
    private SharedPreferences setting;
    NotesRepository(Context context) {
        this.context = context;
        this.setting = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
    }

    void createDialog(final long timestamp, final NoteAddCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_add_note,null,false);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        final CheckedTextView checkedTextViewShowAll = view.findViewById(R.id.checked_text_view_show_all);
        checkedTextViewShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedTextViewShowAll.setChecked(!checkedTextViewShowAll.isChecked());
            }
        });
        final Button buttonAdd = view.findViewById(R.id.add_note_add_button);
        final Button buttonCancel = view.findViewById(R.id.add_note_cancel_button);
        final EditText textNote = view.findViewById(R.id.add_note_text_input);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.isShowing()){
                    String text = textNote.getText().toString();
                    if (!text.equals("")){
                        Note newNote = new Note(getName(),getSurname(),text,getStringDateNow());
                        sendNoteToServer(text,checkedTextViewShowAll.isChecked(),timestamp);
                        callback.add(newNote);
                        dialog.hide();
                    }
                    else callback.nopeText();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog.isShowing()){
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    private void sendNoteToServer(String textNote ,boolean showAll,long timestamp) {
        addNote(textNote,showAll,timestamp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<LoginRepository.ResponseLogin>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(LoginRepository.ResponseLogin responseLogin) {
                if (responseLogin.isSuccess())
                L.log(String.valueOf(responseLogin.isSuccess()));
            }

            @Override
            public void onError(Throwable e) {
                L.log(e.toString());
            }
        });
    }

    private Single<LoginRepository.ResponseLogin> addNote(String textNote, boolean showAll,long timestamp) {
        int status;
        if (showAll) status = 0; else status = 1;
        L.log(String.valueOf(timestamp));
        AddNote note = new AddNote(textNote,timestamp,getGroup(),getSubgroup(),status);
        String jsonNote = getJSONFromAddNote(note);
        String token = getToken();
        return NetworkService.getInstance().getApi().addNote(token,jsonNote);
    }

    private String getToken() {
        return setting.getString(PREF_AUTH_TOKEN,"");
    }

    String getGroup(){
        return setting.getString(PREF_GROUP,"");
    }
    String getSubgroup(){
        return setting.getString(PREF_SUBGROUP,"");
    }

    private String getJSONFromAddNote(AddNote note){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(note);
    }

    private String getJSONFromGetNote(GetNotes notes){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(notes);
    }

    Single<List<Note>> getNotesFromServer(long timestamp) {
        GetNotes getNotes = new GetNotes(getGroup(),getSubgroup(),timestamp);
        return NetworkService.getInstance().getApi().getNotes(getToken(),getJSONFromGetNote(getNotes));
    }

    private class AddNote {
        private String message;
        private long timestamp;
        private String group;
        private String subgroup;
        private int status;

        AddNote(String message, long timestamp, String group, String subgroup, int status) {
            this.message = message;
            this.timestamp = timestamp;
            this.group = group;
            this.subgroup = subgroup;
            this.status = status;
        }
    }

    private class GetNotes{
        private String group;
        private String subgroup;
        private long timestamp;

        GetNotes(String group, String subgroup, long timestamp) {
            this.group = group;
            this.subgroup = subgroup;
            this.timestamp = timestamp;
        }
    }

    private String getName(){
        return setting.getString(PREF_USER_NAME,"");
    }

    private String getSurname(){
        return setting.getString(PREF_USER_SURNAME,"");
    }

    private String getPatronymic(){
        return setting.getString(PREF_USER_PATRONYMIC,"");
    }
}
