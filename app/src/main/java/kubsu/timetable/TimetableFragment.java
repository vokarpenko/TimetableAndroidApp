package kubsu.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static kubsu.timetable.DaysFromJSONarray.getDayWeekNumber;
import static kubsu.timetable.StudentActivity.PREF_OLD_GROUP;
import static kubsu.timetable.StudentActivity.PREFS_FILE;
import static kubsu.timetable.StudentActivity.PREF_GROUP;
import static kubsu.timetable.StudentActivity.PREF_OLD_SUBGROUP;
import static kubsu.timetable.StudentActivity.PREF_SUBGROUP;

public class TimetableFragment extends Fragment {
    private List<Day> days;
    private View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.timetable_fragment, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        String group = settings.getString(PREF_GROUP,"");
        String subGroup = settings.getString(PREF_SUBGROUP,"");
        // проверка на смену группы
        String oldGroup = settings.getString(PREF_OLD_GROUP,"");
        String oldSubGroup = settings.getString(PREF_OLD_SUBGROUP,"");
        Internet internet = new Internet(getContext());
        if ((!oldGroup.equals(group)||!oldSubGroup.equals(subGroup))&&internet.internetTrue()){
            AsyncRequestTimetableStudent asyncRequestTimetableStudent = new AsyncRequestTimetableStudent();
            asyncRequestTimetableStudent.execute(group,subGroup);
            try{
                days = asyncRequestTimetableStudent.get();
                saveTimetableCash();
            }
            catch (Exception e){
                Log.i("mytag",e.toString());
            }
            SharedPreferences.Editor prefEditor = settings.edit();
            prefEditor.putString(PREF_OLD_GROUP, group);
            prefEditor.putString(PREF_OLD_SUBGROUP, subGroup);
            prefEditor.apply();
        }
        else {
            days = loadTimetableCash();
            if (days==null&&internet.internetTrue()){
                AsyncRequestTimetableStudent asyncRequestTimetableStudent = new AsyncRequestTimetableStudent();
                asyncRequestTimetableStudent.execute(group,subGroup);
                try{
                    days = asyncRequestTimetableStudent.get();
                    saveTimetableCash();
                }
                catch (Exception e){
                    Log.i("mytag",e.toString());
                }
            }
           if (!internet.internetTrue()) Toast.makeText(getActivity().getApplicationContext(),"Нет соединения, расписание не обновлено",Toast.LENGTH_SHORT).show();
        }

        RVStudentAdapter adapter = new RVStudentAdapter(days);
        rv.setAdapter(adapter);
        rv.smoothScrollToPosition(getDayWeekNumber());
    }

    public void saveTimetableCash()  {
        try{
            FileOutputStream fos = getActivity().openFileOutput("test.txt", MODE_PRIVATE);
            ObjectOutputStream outStream = new ObjectOutputStream(fos);
            DaySerializeble daySerializeble = new DaySerializeble(days);
            outStream.writeObject(daySerializeble);
            outStream.flush();
            outStream.close();
        }catch(Exception e)
        {
            Log.i("mytag", e.toString());
        }
    }

    public List<Day> loadTimetableCash(){
        DaySerializeble daySerializeble ;
        try{  FileInputStream fis = getActivity().openFileInput("test.txt");
            ObjectInputStream inputStream = new ObjectInputStream(fis);
            daySerializeble = (DaySerializeble) inputStream.readObject();
            inputStream.close();
        }catch(Exception e){
            Log.i("mytag", e.toString());
            return null;
        }
        return daySerializeble.getList();
    }
}
class Internet {
    Context context;
    Internet(Context context){
        this.context=context;
    }
    boolean internetTrue() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        return connected;
    }
}
