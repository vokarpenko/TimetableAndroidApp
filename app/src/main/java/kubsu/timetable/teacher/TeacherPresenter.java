package kubsu.timetable.teacher;

class TeacherPresenter {
    private TeacherView view;
    private TeacherRepository repository;

    TeacherPresenter(TeacherView view, TeacherRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    void logOut(){
        repository.cleanSetting();
        view.openStartActivity();
    }

    void setData(){
        view.setTittle(repository.getTittle());
    }
}
