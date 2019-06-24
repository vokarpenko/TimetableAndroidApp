package kubsu.timetable.student.changeGroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import kubsu.timetable.database.AppDatabase;
import kubsu.timetable.database.TimetableDao;
import kubsu.timetable.R;
import kubsu.timetable.student.GroupEntity;

import static kubsu.timetable.utility.Constant.PREF_FILE;
import static kubsu.timetable.utility.Constant.PREF_GROUP;
import static kubsu.timetable.utility.Constant.PREF_SUBGROUP;

class ChangeGroupRepository {
    private TimetableDao timetableDao;
    private Context context;
    private SharedPreferences setting;
    ChangeGroupRepository(Context context) {
        this.context = context;
        this.timetableDao = AppDatabase.getMemoryDatabase(context).timetableDao();
        this.setting = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
    }

    Single<List<GroupEntity>> loadGroupFromDatabase() {
       return timetableDao.getAllGroups();
    }

    List<Group> getGroupFromGroupEntity(List<GroupEntity> groupEntityList) {
        List<Group> groupList = new ArrayList<>();
        for (int i = 0; i < groupEntityList.size(); i++) {
            String group = groupEntityList.get(i).getGroup();
            String subgroup = groupEntityList.get(i).getSubgroup();
            //если группа уже есть, то добавляеям к ней подгруппу
            if (containInList(groupList, group)) {
                //если подгруппа новая для данной группы то добавляем ее к группе
                if (!groupList.get(getIndexGroup(groupList, group)).getSubgroups().contains(subgroup))
                    groupList.get(getIndexGroup(groupList, group)).getSubgroups().add(subgroup);
                //если данная группа новая,то добавляем ее в список всех групп вместе с подгруппой
            }
                else {
                    List<String> subList = new ArrayList<>();
                    if (!subgroup.equals("")) subList.add(subgroup);
                    groupList.add(new Group(group, subList, group.substring(0, 1)));
                }
        }
        return groupList;
    }

    private boolean containInList(List<Group> list, String group) {
        if (list.size() == 0) return false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGroup().equals(group)) return true;
        }
        return false;
    }

    private int getIndexGroup(List<Group> list, String group) {
        if (list.size() == 0) return -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGroup().equals(group)) return i;
        }
        return -1;
    }

    void initSpinners(Spinner courseNumber, final Spinner groupNumber, final Spinner subGroupNumber, final List<Group> groups) {
        String[] courseNumberString = {"1", "2", "3", "4"};
        ArrayAdapter courseAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, courseNumberString);
        courseNumber.setAdapter(courseAdapter);
        courseNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        String[] groupNumberString = getStringGroupArray("1",groups);
                        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 1:
                        groupNumberString = getStringGroupArray("2",groups);
                        groupAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 2:
                        groupNumberString = getStringGroupArray("3",groups);
                        groupAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 3:
                        groupNumberString = getStringGroupArray("4",groups);
                        groupAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        groupNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] subNumberString = getStringSubArray(groupNumber.getSelectedItem().toString(),groups);
                if (subNumberString.length == 0) {
                    subNumberString = new String[1];
                    subNumberString[0] = "";
                    ArrayAdapter<String> subgroupAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, subNumberString);
                    subGroupNumber.setAdapter(subgroupAdapter);
                    subGroupNumber.setEnabled(false);
                } else {
                    subGroupNumber.setEnabled(true);
                    ArrayAdapter<String> subgroupAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, subNumberString);
                    subGroupNumber.setAdapter(subgroupAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private String[] getStringGroupArray(String course, List<Group> groups){
        List<String> returnList = new ArrayList<>();
        for (int i = 0; i <groups.size() ; i++) {
            if (groups.get(i).getCourse().equals(course)) returnList.add(groups.get(i).getGroup());
        }
        String[] returnArray = returnList.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
    }


    private String[] getStringSubArray(String group, List<Group> groups){
        List<String> returnList = new ArrayList<>();
        for (int i = 0; i <groups.size() ; i++) {
            if (groups.get(i).getGroup().equals(group)) {
                returnList = groups.get(i).getSubgroups();
                break;
            }
        }
        String[] returnArray = returnList.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
    }

    void saveSettingGroup(String group, String subgroup) {
        setting.edit().putString(PREF_GROUP,group)
                .putString(PREF_SUBGROUP,subgroup)
                .apply();
    }

    String getSelected(Spinner groupSpinner) {
        return groupSpinner.getSelectedItem().toString();
    }

    String getSelectedSubgroup(Spinner subgroupSpinner) {
        return subgroupSpinner.getSelectedItem().toString();
    }

}
