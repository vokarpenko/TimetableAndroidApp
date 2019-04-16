package kubsu.timetable.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kubsu.timetable.R;

import static kubsu.timetable.Activity.StudentActivity.PREFS_FILE;
import static kubsu.timetable.Activity.StudentActivity.PREF_TEACHER;

public class StartActivity extends AppCompatActivity {
    public static final String HAS_VISITED="hasVisited";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //проверка на первый запуск
        final SharedPreferences setting = getSharedPreferences(PREFS_FILE,Context.MODE_PRIVATE);
        int hasVisited = setting.getInt(HAS_VISITED, 0);
        if (hasVisited==1){
            startActivity(new Intent(getBaseContext(),StudentActivity.class));
            finish();
        }
        if (hasVisited==2){
            Log.i("mytag",setting.getString(PREF_TEACHER,"null"));
            if(setting.getString(PREF_TEACHER,"null").equals("null")){
                startActivity(new Intent(getBaseContext(),ChangeTeacherActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(getBaseContext(), TeacherActivity.class));
                finish();
            }
        }

        setContentView(R.layout.activity_start);
        final SharedPreferences.Editor editor = setting.edit();
        Button buttonTeacher = findViewById(R.id.button_teacher);
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt(HAS_VISITED, 2);
                editor.apply();
                if(setting.getString(PREF_TEACHER,"null").equals("null")){
                    startActivity(new Intent(getBaseContext(),ChangeTeacherActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getBaseContext(), TeacherActivity.class));
                    finish();
                }
            }
        });
        Button buttonStudent = findViewById(R.id.button_std);
        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt(HAS_VISITED, 1);
                editor.apply();
                startActivity(new Intent(getBaseContext(),StudentActivity.class));
                finish();
            }
        });
        setHelloImageAndText();
    }
    private void setHelloImageAndText(){
        TextView textHello = findViewById(R.id.text_hello);
        ImageView imageHello = findViewById(R.id.hello_image);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", new Locale("ru"));
        Date date = new Date();
        int currentTime = Integer.parseInt(simpleDateFormat.format(date));
        if (currentTime>=6&currentTime<=10){
            textHello.setText("Доброе утро");
            imageHello.setImageResource(R.drawable.sunrise);
        }
        else if (currentTime>=11&currentTime<=17){
            textHello.setText("Добрый день");
            imageHello.setImageResource(R.drawable.day);
        }
        else if (currentTime>=18&currentTime<=21){
            textHello.setText("Добрый вечер");
            imageHello.setImageResource(R.drawable.evening);
        }
        else {
            textHello.setText("Доброй ночи");
            imageHello.setImageResource(R.drawable.night);
        }
    }
}
