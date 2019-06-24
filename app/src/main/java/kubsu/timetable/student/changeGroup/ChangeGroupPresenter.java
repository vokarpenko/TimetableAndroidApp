package kubsu.timetable.student.changeGroup;

import android.widget.Spinner;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import kubsu.timetable.student.GroupEntity;
import kubsu.timetable.student.StudentRepository;
import kubsu.timetable.timetable.TimetableFragment;

class ChangeGroupPresenter {
    private ChangeGroupView view;
    private ChangeGroupRepository repository;

    ChangeGroupPresenter(ChangeGroupView view, ChangeGroupRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    void setGroups(final Spinner courseNumber, final Spinner groupNumber, final Spinner subGroupNumber) {
        repository.loadGroupFromDatabase().subscribe(new SingleObserver<List<GroupEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(List<GroupEntity> groupEntities) {
                repository.initSpinners(courseNumber,groupNumber,subGroupNumber,repository.getGroupFromGroupEntity(groupEntities));
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    void buttonChangeClick(Spinner groupSpinner, Spinner subgroupSpinner){
        repository.saveSettingGroup(repository.getSelected(groupSpinner),repository.getSelectedSubgroup(subgroupSpinner));
        view.setTimetableFragment(StudentRepository.getFragment(TimetableFragment.class));
    }
}
