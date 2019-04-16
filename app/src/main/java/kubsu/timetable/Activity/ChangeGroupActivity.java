package kubsu.timetable.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kubsu.timetable.AsyncTask.AsyncRequestListGroup;
import kubsu.timetable.Model.Group;
import kubsu.timetable.R;
import kubsu.timetable.Utility.Internet;

import static kubsu.timetable.Activity.StudentActivity.PREFS_FILE;
import static kubsu.timetable.Activity.StudentActivity.PREF_GROUP;
import static kubsu.timetable.Activity.StudentActivity.PREF_SUBGROUP;

public class ChangeGroupActivity extends AppCompatActivity {
    private Spinner courseNumber;
    private Spinner groupNumber;
    private Spinner subGroupNumber;
    private List<Group> listGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_group);
        courseNumber = findViewById(R.id.course_number);
        groupNumber = findViewById(R.id.group_number);
        subGroupNumber = findViewById(R.id.podgroup_number);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Button buttonChange = findViewById(R.id.button_change);
        if (new Internet(getBaseContext()).isConnection()) {
            AsyncRequestListGroup asyncRequestListGroup = new AsyncRequestListGroup();
            buttonChange.setEnabled(true);
            try {
                listGroup = asyncRequestListGroup.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            listGroup  = new ArrayList<>();
            Toast.makeText(getBaseContext(),"Нет интернет соединения",Toast.LENGTH_LONG).show();
            buttonChange.setEnabled(false);
        }
        createSpinners();

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //уставнока новой группы по нажатию кнопки
                SharedPreferences settings = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_GROUP,groupNumber.getSelectedItem().toString());
                prefEditor.putString(PREF_SUBGROUP, subGroupNumber.getSelectedItem().toString());
                prefEditor.apply();
                Intent intent = new Intent(ChangeGroupActivity.this, StudentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }


    private  void createSpinners(){
        String[] courseNumberString = {"1","2","3","4"};
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,courseNumberString);
        courseNumber.setAdapter(courseAdapter);
        courseNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        String[] groupNumberString = getStringGroupArray("1");
                        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 1:
                        groupNumberString = getStringGroupArray("2");
                        groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 2:
                        groupNumberString = getStringGroupArray("3");
                        groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 3:
                        groupNumberString = getStringGroupArray("4");
                        groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
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
                    String[] subNumberString = getStringSubArray(groupNumber.getSelectedItem().toString());
                    if (subNumberString.length==0){
                        subNumberString = new String[1];
                        subNumberString[0]="";
                        ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item, subNumberString);
                        subGroupNumber.setAdapter(podgroupAdapter);
                        subGroupNumber.setEnabled(false);
                    }
                    else {
                        subGroupNumber.setEnabled(true);
                        ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item, subNumberString);
                        subGroupNumber.setAdapter(podgroupAdapter);
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private String[] getStringGroupArray(String course){
        List<String> returnList = new ArrayList<>();
        for (int i = 0; i <listGroup.size() ; i++) {
            if (listGroup.get(i).getCourse().equals(course)) returnList.add(listGroup.get(i).getGroup());
        }
        String[] returnArray = returnList.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
    }


    private String[] getStringSubArray(String group){
        List<String> returnList = new ArrayList<>();
        for (int i = 0; i <listGroup.size() ; i++) {
            if (listGroup.get(i).getGroup().equals(group)) {
                returnList = listGroup.get(i).getSubgroups();
                break;
            }
        }
        String[] returnArray = returnList.toArray(new String[0]);
        Arrays.sort(returnArray);
        return returnArray;
    }
}