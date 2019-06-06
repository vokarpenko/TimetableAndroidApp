package kubsu.timetable.Start;

class StartPresenter {
    private StartView view;
    private StartRepository repository;

    StartPresenter(StartView view, StartRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    void checkFirstOpen(){
        switch (repository.checkFirstOpen()){
            case 1:
                view.openStudentActivity();
                break;
            case 2:
                view.openTeacherActivity();
                break;
        }
    }

    void setHelloImageAndText(){
        StartRepository.Hello hello = repository.getHelloImageAndText();
        view.setHelloImage(hello.getText(),hello.getResourceId());
    }

    void buttonStudentClick(){
        view.openLoginActivity();
    }

    void buttonTeacherClick(){
        view.openTeacherActivity();
    }
}
