package kubsu.timetable.notes;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.utility.L;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static kubsu.timetable.utility.Constant.INTERNET_ERROR;

class NotesPresenter {
    private NotesRepository repository;
    private NotesView view;

    NotesPresenter(NotesView view, NotesRepository repository) {
        this.repository = repository;
        this.view = view;
    }

    void setData(long timestamp){
        view.setSubject();
        repository.getNotesFromServer(timestamp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<Note> notes) {
                        if (notes.size()!=0) {
                            repository.timestampsToDate(notes);
                            view.setNotes(notes);
                            view.setHintVisibility(INVISIBLE);
                        }
                        else   view.setHintVisibility(VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.log(e.toString());
                        view.showErrorMessage(INTERNET_ERROR);
                    }
                });
    }

    void fabAddNoteClick(long timestamp) {
        repository.createDialog(timestamp, new NotesRepository.NoteAddCallback() {
            @Override
            public void add(Note note) {
                view.addNote(note);
                view.setHintVisibility(INVISIBLE);
            }

            @Override
            public void nopeText() {
                view.showErrorMessage("Вы не можете добавить пустую заметку");
            }
        });
    }
}
