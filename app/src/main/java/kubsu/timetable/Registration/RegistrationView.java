package kubsu.timetable.Registration;

import kubsu.timetable.Utility.ErrorToast;

interface RegistrationView extends ErrorToast {
    void openLoginActivity();
    void openAfterRegisterActivity();
}
