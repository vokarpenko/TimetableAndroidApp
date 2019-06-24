package kubsu.timetable.timetable;

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
import android.widget.Toast;

import java.util.List;
import kubsu.timetable.R;

import static kubsu.timetable.utility.Constant.EXTRA_TIMETABLE_TYPE;


public class TimetableFragment extends Fragment implements TimetableView {
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TimetableAdapter adapter;

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
    }

    private void init() {
        recyclerView = view.findViewById(R.id.recycler_view_student_timetable);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TimetableAdapter(getTimetableType());
        recyclerView.setAdapter(adapter);
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              presenter.refreshTimetable();
            }
        });
    }

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
    public void setTimetable(List<Day> days) {
        adapter.setDays(days);
    }

    @Override
    public void showCurrentDay(int day) {
        recyclerView.smoothScrollToPosition(day);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public int getTimetableType() {
        return getArguments() != null ? getArguments().getInt(EXTRA_TIMETABLE_TYPE) : -1;
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }
}

