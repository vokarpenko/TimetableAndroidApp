package kubsu.timetable.registration;

import kubsu.timetable.utility.ErrorToast;

interface RegistrationView extends ErrorToast {
    void openLoginActivity();
    void openAfterRegisterActivity();
}
