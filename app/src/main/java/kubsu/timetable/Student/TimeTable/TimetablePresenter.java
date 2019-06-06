package kubsu.timetable.Student.TimeTable;

import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.Login.LoginRepository;
import kubsu.timetable.Retrofit.NetworkService;
import kubsu.timetable.Utility.L;

class TimetablePresenter {
    private TimetableView view;
    private TimetableRepository repository;

    TimetablePresenter(TimetableView view, TimetableRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    void setTimetable(){
        view.setTimeTable(repository.getTimetable());
    }

    void showCurrentDay(){
        view.showCurrentDay(repository.getCurrentNumberDayWeek());
    }

    void test() {
        NetworkService.getInstance().getApi().test("Bearer "+repository.getAuthToken())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableSingleObserver<LoginRepository.Test>() {
                    @Override
                    public void onSuccess(LoginRepository.Test test) {
                        L.log(test.user);
                    }
                    @Override
                    public void onError(Throwable e) {
                        L.log(e.getLocalizedMessage());
                    }
                });
    }
}
