package kubsu.timetable.Student;

import android.support.v4.app.Fragment;

import kubsu.timetable.Utility.ErrorToast;

interface StudentView extends ErrorToast {
    void setTittle(String tittle);
    void setFragment(Fragment fragment);
    void setHeader(String text);
    void openStartActivity();
}
