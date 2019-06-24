package kubsu.timetable.teacher;

import kubsu.timetable.utility.ErrorToast;

public interface TeacherView extends ErrorToast {
    void setTittle(String text);

    void openStartActivity();
}
