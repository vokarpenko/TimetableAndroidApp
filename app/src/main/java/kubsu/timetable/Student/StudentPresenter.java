package kubsu.timetable.Student;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.Student.ChangeGroup.ChangeGroupFragment;
import kubsu.timetable.Student.TimeTable.Lesson;
import kubsu.timetable.Student.TimeTable.TimetableFragment;
import kubsu.timetable.Utility.LoadingFragment;

import static kubsu.timetable.Utility.Constant.INTERNET_ERROR;

class StudentPresenter {
    private StudentView view;
    private StudentRepository repository;

    StudentPresenter(StudentView view, StudentRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    void setTittle(String tittle){
        view.setTittle(tittle);
    }

    void setFragment(Class fragmentClass,boolean checkInternet) {
        if (checkInternet)
        if (repository.isConnection())
            view.setFragment(StudentRepository.getFragment(fragmentClass));
        else
            view.showErrorMessage(INTERNET_ERROR);
        else view.setFragment(StudentRepository.getFragment(fragmentClass));
    }


    void setHeader(){
        String textHeader = "Группа "+repository.getGroup()+" "+repository.getSubgroup();
        view.setHeader(textHeader);
    }

    void logOut(){
        repository.cleanGroup();
        view.openStartActivity();
    }

    private void downloadDataLessons() {
        setFragment(LoadingFragment.class,false);
                        repository.downloadDataLessons()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<List<Lesson>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                    }

                                    @Override
                                    public void onSuccess(List<Lesson> lessons) {
                                        repository.saveData(lessons);
                                        repository.setDataLessons();
                                        if (repository.hasGroup())
                                            view.setFragment(StudentRepository.getFragment(TimetableFragment.class));
                                        else
                                            view.setFragment(StudentRepository.getFragment(ChangeGroupFragment.class));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        view.showErrorMessage(INTERNET_ERROR);
                                    }
                                });
    }
    private void downloadDataGroups(){
        repository.downloadDataGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<GroupEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<GroupEntity> groupEntities) {
                        repository.saveGroup(groupEntities);
                        repository.setDataGroups();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage(INTERNET_ERROR);
                    }
                });
    }

    void checkData() {
        if(!repository.hasData()){
            downloadDataGroups();
            downloadDataLessons();
        }
        else {
            if (repository.hasGroup())
                view.setFragment(StudentRepository.getFragment(TimetableFragment.class));
            else
                view.setFragment(StudentRepository.getFragment(ChangeGroupFragment.class));
        }
    }
}
