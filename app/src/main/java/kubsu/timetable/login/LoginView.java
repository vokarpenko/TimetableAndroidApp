package kubsu.timetable.login;

import kubsu.timetable.utility.ErrorToast;

public interface LoginView extends ErrorToast {
    void openStudentActivity();

    void openRegisterActivity();

    void setRememberChecked(boolean checked);

    void setLogin( String login);

    void setPassword( String password);

    void openTeacherActivity();

    int getLoginType();
}
