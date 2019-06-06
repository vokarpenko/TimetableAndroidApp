package kubsu.timetable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

import kubsu.timetable.R;

import static kubsu.timetable.Utility.Constant.PREF_FILE;
import static kubsu.timetable.Utility.Constant.PREF_TEACHER;


public class ChangeTeacherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences settings = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        setContentView(R.layout.activity_change_teacher);
        final AppCompatAutoCompleteTextView teacherNameTextView = findViewById(R.id.techer_name_text_view);
        final String[] listTeachers = {"Малыхин К.В.","Уварова А.В."};
        teacherNameTextView.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, listTeachers));
        final Button buttonChooseTeacher = findViewById(R.id.button_choose_teacher);
        buttonChooseTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Arrays.asList(listTeachers).contains(teacherNameTextView.getText().toString())){
                    settings.edit().putString(PREF_TEACHER,teacherNameTextView.getText().toString()).apply();
                    startActivity(new Intent(getBaseContext(),TeacherActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(getBaseContext(), "Выберите преподавателя из выпадающего списка", Toast.LENGTH_LONG).show();
                }
            }
        });
        teacherNameTextView.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            buttonChooseTeacher.performClick();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }
}
