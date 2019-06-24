package kubsu.timetable.timetable;

import java.util.List;

import kubsu.timetable.utility.ErrorToast;

public interface TimetableView extends ErrorToast {
    void setTimetable(List<Day> days);

    void showCurrentDay(int day);

    void setRefreshing(boolean refreshing);

    int getTimetableType();
}
