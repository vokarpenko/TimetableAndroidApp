package kubsu.timetable.AfterRegister;

class AfterRegisterPresenter {
    AfterRegisterView view;

    AfterRegisterPresenter(AfterRegisterView view) {
        this.view = view;
    }

    void openLoginActivity(){
        view.openLoginActivity();
    }
}
