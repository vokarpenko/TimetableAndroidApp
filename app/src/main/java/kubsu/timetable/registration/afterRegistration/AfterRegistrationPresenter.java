package kubsu.timetable.registration.afterRegistration;

class AfterRegistrationPresenter {
    private AfterRegistrationView view;

    AfterRegistrationPresenter(AfterRegistrationView view) {
        this.view = view;
    }

    void openLoginActivity(){
        view.openLoginActivity();
    }
}

