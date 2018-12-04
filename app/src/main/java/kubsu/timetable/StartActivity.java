package kubsu.timetable;

import android.content.Intent;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private TextView textHello;
    private ImageView imageHello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button buttonTeacher = findViewById(R.id.button_teacher);
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),TeacherActivity.class));
            }
        });
        Button buttonStudent = findViewById(R.id.button_std);
        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),MainActivity.class));
            }
        });
        setHelloImageAndText();

    }
    private void setHelloImageAndText(){
        textHello = findViewById(R.id.text_hello);
        imageHello = findViewById(R.id.hello_image);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", new Locale("ru"));
        Date date = new Date();
        int currentTime = Integer.parseInt(simpleDateFormat.format(date));
        if (currentTime>=6&currentTime<=11){
            textHello.setText("Доброе утро");
            imageHello.setImageResource(R.drawable.sunrise);
        }
        else if (currentTime>=12&currentTime<=18){
            textHello.setText("Добрый день");
            imageHello.setImageResource(R.drawable.day);
        }
        else if (currentTime>=19&currentTime<=23){
            textHello.setText("Добрый вечер");
            imageHello.setImageResource(R.drawable.evening);
        }
        else if (currentTime<=5){
            textHello.setText("Доброй ночи");
            imageHello.setImageResource(R.drawable.night);
        }


    }
}
