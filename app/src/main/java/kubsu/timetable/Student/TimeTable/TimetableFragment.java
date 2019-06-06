package kubsu.timetable.Student.TimeTable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import kubsu.timetable.R;


public class TimetableFragment extends Fragment implements TimetableView {
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StudentTimetableAdapter adapter;

    private TimetablePresenter presenter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_timetable, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        if (presenter==null){
            presenter=new TimetablePresenter(this,new TimetableRepository(getContext()));
        }
        presenter.setTimetable();
        presenter.showCurrentDay();
        //presenter.test();
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recycler_view_student_timetable);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new StudentTimetableAdapter();
        recyclerView.setAdapter(adapter);
    }
        /*
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        SharedPreferences settings = getActivity().getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        group = settings.getString(PREF_GROUP,"");
        subGroup = settings.getString(PREF_SUBGROUP,"");
        // проверка на смену группы
        String oldGroup = settings.getString(PREF_OLD_GROUP,"");
        String oldSubGroup = settings.getString(PREF_OLD_SUBGROUP,"");
        final Internet internet = new Internet(getContext());
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

        adapter = new StudentTimetableAdapter(days);
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(new CurrentDayWeek().getCurrentNumberDayWeek());
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(internet.isConnection()){
                    updateTimeTable();
                }
                else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity().getApplicationContext(),"Нет соединения, расписание не обновлено",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }*/

   /* private void updateTimeTable(){
       new Thread(new Runnable() {
           @Override
           public void run() {
               days = getTimeTableStudent(group,subGroup);
               //saveTimetableCash();
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       adapter = new StudentTimetableAdapter(days);
                       recyclerView.setAdapter(adapter);
                       adapter.notifyDataSetChanged();
                       swipeRefreshLayout.setRefreshing(false);
                   }
               });
           }
       }).start();
    }*/

  /*  List<Day> getTimeTableStudent(String group, String subGroup) {
        String[] stringDayArray;
        String path;
        try {
            stringDayArray = new String[14];
            Calendar now = Calendar.getInstance();
            Calendar startClasses = Calendar.getInstance();
            startClasses.set(Calendar.YEAR, 2018);
            startClasses.set(Calendar.MONTH, 9);
            startClasses.set(Calendar.DATE, 1);
            int startClassesWeek = startClasses.get(Calendar.WEEK_OF_YEAR);
            int currentWeek = now.get(Calendar.WEEK_OF_YEAR);
            int parityCurrentWeek = currentWeek - startClassesWeek;
            path = "http://timetable-fktpm.ru/index.php?option=mInfo&gruppa=" + group + "&subgroup=" + subGroup + "&nday=";
            if (parityCurrentWeek % 2 == 1) {
                //если числитель
                for (int i = 1; i <= stringDayArray.length; i++) {
                    String stringDay = "";
                    org.jsoup.nodes.Document doc = Jsoup.connect(path + Integer.toString(i)).get();
                    stringDay = doc.select("timetable").text();
                    stringDayArray[i - 1] = stringDay;
                }
            } else {
                //если знаменатель
                for (int i = 1; i <= 7; i++) {
                    String stringDay = "";
                    org.jsoup.nodes.Document doc = Jsoup.connect(path + Integer.toString(i + 7)).get();
                    stringDay = doc.select("timetable").text();
                    stringDayArray[i - 1] = stringDay;
                }
                for (int i = 8; i <= stringDayArray.length; i++) {
                    String stringDay = "";
                    org.jsoup.nodes.Document doc = Jsoup.connect(path + Integer.toString(i - 7)).get();
                    stringDay = doc.select("timetable").text();
                    stringDayArray[i - 1] = stringDay;
                }
            }
            return new ListDays(stringDayArray).getList();
        } catch (Exception e) {
            Log.i("myapp", e.toString());
            return null;
        }
    }*/

    @Override
    public void setTimeTable(List<Day> days) {
        adapter.setDays(days);
    }

    @Override
    public void showCurrentDay(int day) {
        recyclerView.smoothScrollToPosition(day);
    }
}

