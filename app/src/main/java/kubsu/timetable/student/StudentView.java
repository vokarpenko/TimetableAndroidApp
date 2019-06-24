package kubsu.timetable.student;

import android.support.v4.app.Fragment;

import kubsu.timetable.utility.ErrorToast;

interface StudentView extends ErrorToast {

    void setTittle(String tittle);

    void setFragment(Fragment fragment);

    void setHeader(String text);

    void openStartActivity();

    void setProgressBarVisibility(int visibility);
}
