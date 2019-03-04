package kubsu.timetable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static kubsu.timetable.StudentActivity.PREFS_FILE;

public class StartActivity extends AppCompatActivity {
    public static final String HAS_VISITED="hasVisited";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        //проверка на первый запуск
        SharedPreferences sp = getSharedPreferences(PREFS_FILE,
                Context.MODE_PRIVATE);
        int hasVisited = sp.getInt("hasVisited", 0);
        if (hasVisited==1){
            startActivity(new Intent(getBaseContext(),StudentActivity.class));
            finish();
        }
        if (hasVisited==2){
            startActivity(new Intent(getBaseContext(),TeacherActivity.class));
            finish();
        }

        setContentView(R.layout.activity_start);
        final SharedPreferences.Editor editor = sp.edit();
        Button buttonTeacher = findViewById(R.id.button_teacher);
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putInt(HAS_VISITED, 2);
                editor.apply();
                startActivity(new Intent(getBaseContext(),TeacherActivity.class));
                finish();
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
        //buttonTeacher.performClick();
        //Intent i = new Intent(this, RegistrationService.class);
        //startService(i);
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
