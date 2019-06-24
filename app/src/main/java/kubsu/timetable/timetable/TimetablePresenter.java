package kubsu.timetable.timetable;

import static kubsu.timetable.utility.Constant.INTERNET_ERROR;

class TimetablePresenter {
    private TimetableView view;
    private TimetableRepository repository;

    TimetablePresenter(TimetableView view, TimetableRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    void setTimetable(){
        view.setTimetable(repository.getTimetable(view.getTimetableType()));
    }

    void showCurrentDay(){
        view.showCurrentDay(repository.getCurrentNumberDayWeek());
    }

    void refreshTimetable() {
        repository.refreshTimetable(new TimetableRepository.UpdateCallback() {
            @Override
            public void onSuccess() {
                view.setTimetable(repository.getTimetable(view.getTimetableType()));
                view.setRefreshing(false);
            }

            @Override
            public void onFailure() {
                view.setRefreshing(false);
                view.showErrorMessage(INTERNET_ERROR);
            }
        });
    }
}
