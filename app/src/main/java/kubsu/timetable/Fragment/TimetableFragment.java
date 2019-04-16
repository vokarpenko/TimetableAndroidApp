package kubsu.timetable.Fragment;

import android.content.SharedPreferences;
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

import kubsu.timetable.Adapter.RVStudentAdapter;
import kubsu.timetable.AsyncTask.AsyncRequestTimetableStudent;
import kubsu.timetable.Model.Day;
import kubsu.timetable.Model.ListDays;
import kubsu.timetable.R;
import kubsu.timetable.Utility.CurrentDayWeek;
import kubsu.timetable.Utility.Internet;

import static android.content.Context.MODE_PRIVATE;
import static kubsu.timetable.Activity.StudentActivity.PREFS_FILE;
import static kubsu.timetable.Activity.StudentActivity.PREF_GROUP;
import static kubsu.timetable.Activity.StudentActivity.PREF_OLD_GROUP;
import static kubsu.timetable.Activity.StudentActivity.PREF_OLD_SUBGROUP;
import static kubsu.timetable.Activity.StudentActivity.PREF_SUBGROUP;

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
        if ((!oldGroup.equals(group)||!oldSubGroup.equals(subGroup))&&internet.isConnection()){
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
            if (days==null&&internet.isConnection()){
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
           if (!internet.isConnection()) Toast.makeText(getActivity().getApplicationContext(),"Нет соединения, расписание не обновлено",Toast.LENGTH_SHORT).show();
        }

        RVStudentAdapter adapter = new RVStudentAdapter(days);
        rv.setAdapter(adapter);
        rv.smoothScrollToPosition(new CurrentDayWeek().getCurrentNumberDayWeek());
    }

    private void saveTimetableCash()  {
        try{
            FileOutputStream fos = getActivity().openFileOutput("test.txt", MODE_PRIVATE);
            ObjectOutputStream outStream = new ObjectOutputStream(fos);
            ListDays listDays = new ListDays(days);
            outStream.writeObject(listDays);
            outStream.flush();
            outStream.close();
        }catch(Exception e)
        {
            Log.i("mytag", e.toString());
        }
    }

    private List<Day> loadTimetableCash(){
        ListDays listDays;
        try{  FileInputStream fis = getActivity().openFileInput("test.txt");
            ObjectInputStream inputStream = new ObjectInputStream(fis);
            listDays = (ListDays) inputStream.readObject();
            inputStream.close();
        }catch(Exception e){
            Log.i("mytag", e.toString());
            return null;
        }
        return listDays.getList();
    }
}

