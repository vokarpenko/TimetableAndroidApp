package kubsu.timetable;
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

import static kubsu.timetable.StudentActivity.PREFS_FILE;
import static kubsu.timetable.StudentActivity.PREF_GROUP;
import static kubsu.timetable.StudentActivity.PREF_SUBGROUP;

public class ChangeGroupActivity extends AppCompatActivity {
    private Spinner courseNumber;
    private Spinner groupNumber;
    private Spinner subGroupNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_group);
        courseNumber = findViewById(R.id.course_number);
        groupNumber = findViewById(R.id.group_number);
        subGroupNumber = findViewById(R.id.podgroup_number);
        createSpinners();
        Button buttonChange = findViewById(R.id.button_change);
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
        String[] courseNumberString = {"1 курс","2 курс","3 курс","4 курс","1 курс маг.","2 курс маг."};
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,courseNumberString);
        courseNumber.setAdapter(courseAdapter);
        courseNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        String[] groupNumberString = new String[10];
                        for (int i = 10; i <=19 ; i++) {
                            groupNumberString[i-10]=Integer.toString(i);
                        }
                        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 1:
                        groupNumberString = new String[7];
                        groupNumberString[0]="21";
                        groupNumberString[1]="22";
                        groupNumberString[2]="23";
                        groupNumberString[3]="25";
                        groupNumberString[4]="26";
                        groupNumberString[5]="27";
                        groupNumberString[6]="28";
                        groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 2:
                        groupNumberString = new String[5];
                        groupNumberString[0]="31";
                        groupNumberString[1]="32";
                        groupNumberString[2]="35";
                        groupNumberString[3]="36";
                        groupNumberString[4]="37";
                        groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 3:
                        groupNumberString = new String[6];
                        groupNumberString[0]="КПМ";
                        groupNumberString[1]="КММ";
                        groupNumberString[2]="КИТ";
                        groupNumberString[3]="45";
                        groupNumberString[4]="46";
                        groupNumberString[5]="47";
                        groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 4:
                        groupNumberString = new String[4];
                        groupNumberString[0]="202";
                        groupNumberString[1]="209";
                        groupNumberString[2]="212";
                        groupNumberString[3]="65";
                        groupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,groupNumberString);
                        groupNumber.setAdapter(groupAdapter);
                        break;
                    case 5:
                        groupNumberString = new String[4];
                        groupNumberString[0]="302";
                        groupNumberString[1]="309";
                        groupNumberString[2]="312";
                        groupNumberString[3]="75";
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
                switch (courseNumber.getSelectedItem().toString()){
                    case "1 курс":
                        if (groupNumber.getSelectedItem().toString().equals("10")){
                            String[] podgroupNumberString = {""};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(false);
                        }
                        else {
                            String[] podgroupNumberString = {"1","2"};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(true);
                        }
                        break;
                    case "2 курс":
                        if (groupNumber.getSelectedItem().toString().equals("23")
                                |groupNumber.getSelectedItem().toString().equals("28")){
                            String[] podgroupNumberString = {""};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(false);
                        }
                        else {
                            String[] podgroupNumberString = {"1","2"};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(true);
                        }
                        break;
                    case "3 курс":
                        if (groupNumber.getSelectedItem().toString().equals("31")|groupNumber.getSelectedItem().toString().equals("32")){
                            String[] podgroupNumberString = {"КПМ","КММ","КИТ"};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(true);
                        }
                        else{
                            String[] podgroupNumberString = {"1","2"};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(true);
                        }
                        break;
                    case "4 курс":
                        if (groupNumber.getSelectedItem().toString().equals("КПМ")
                                |groupNumber.getSelectedItem().toString().equals("КММ")
                                |groupNumber.getSelectedItem().toString().equals("КИТ")){
                            String[] podgroupNumberString = {""};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(false);
                        }
                        else{
                            String[] podgroupNumberString = {"1","2"};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(true);
                        }
                        break;
                    case "1 курс маг.":
                        if (groupNumber.getSelectedItem().toString().equals("212")){
                            String[] podgroupNumberString = {"1","2"};

                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(true);
                        }
                        else {
                            String[] podgroupNumberString = {""};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(false);
                        }
                        break;
                    case "2 курс маг.":
                        if (groupNumber.getSelectedItem().toString().equals("312")){
                            String[] podgroupNumberString = {"1","2"};

                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(true);
                        }
                        else {
                            String[] podgroupNumberString = {""};
                            ArrayAdapter<String> podgroupAdapter = new ArrayAdapter<>(getBaseContext(),R.layout.spinner_item,podgroupNumberString);
                            subGroupNumber.setAdapter(podgroupAdapter);
                            subGroupNumber.setEnabled(false);
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}