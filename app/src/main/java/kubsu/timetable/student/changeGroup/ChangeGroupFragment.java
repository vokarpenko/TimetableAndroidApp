package kubsu.timetable.student.changeGroup;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import kubsu.timetable.R;

public class ChangeGroupFragment extends Fragment implements ChangeGroupView {
    private Spinner courseNumber;
    private Spinner groupNumber;
    private Spinner subGroupNumber;
    private List<Group> listGroup;
    private View view;
    private ChangeGroupPresenter presenter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_group, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        if (presenter==null){
            presenter=new ChangeGroupPresenter(this,new ChangeGroupRepository(getContext()));
        }
        presenter.setGroups(courseNumber,groupNumber,subGroupNumber);
    }

    private void init() {
        courseNumber = view.findViewById(R.id.course_number);
        groupNumber = view.findViewById(R.id.group_number);
        subGroupNumber = view.findViewById(R.id.podgroup_number);
        Button buttonChange = view.findViewById(R.id.button_change);
        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.buttonChangeClick(groupNumber,subGroupNumber);
            }
        });
    }

    @Override
    public void setTimetableFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
        }
    }
}