package kubsu.timetable.Login;

import android.widget.TextView;

import kubsu.timetable.Utility.ErrorToast;

public interface LoginView extends ErrorToast {
    void openStudentActivity();
    void openRegisterActivity();
    void setRememberChecked(boolean checked);
    void setLogin( String login);
    void setPassword( String password);
}
