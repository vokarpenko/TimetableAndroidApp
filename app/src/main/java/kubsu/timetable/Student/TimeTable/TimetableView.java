package kubsu.timetable.Student.TimeTable;

import java.util.List;

public interface TimetableView {
    void setTimeTable(List<Day> days);
    void showCurrentDay(int day);
}
