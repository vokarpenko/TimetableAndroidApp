package kubsu.timetable.Start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import kubsu.timetable.Activity.ChangeTeacherActivity;
import kubsu.timetable.Student.StudentActivity;
import kubsu.timetable.Login.LoginActivity;
import kubsu.timetable.R;

import static kubsu.timetable.Utility.Constant.PREF_FILE;


public class StartActivity extends AppCompatActivity implements StartView {
    public static final String HAS_VISITED="has_visited";
    private StartPresenter presenter=null;
    private TextView textHello;
    private ImageView imageHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
        presenter.checkFirstOpen();
        presenter.setHelloImageAndText();
    }

    private void init() {
        Button buttonTeacher = findViewById(R.id.button_teacher);
        Button buttonStudent = findViewById(R.id.button_std);
        textHello = findViewById(R.id.text_hello);
        imageHello = findViewById(R.id.hello_image);
        if (presenter==null) {
            StartRepository repository = new StartRepository(getSharedPreferences(PREF_FILE,MODE_PRIVATE));
            presenter = new StartPresenter(this,repository);
        }
        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.buttonStudentClick();
            }
        });
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.buttonTeacherClick();
            }
        });
    }

    @Override
    public void openTeacherActivity() {
        startActivity(new Intent(getBaseContext(),ChangeTeacherActivity.class));
        finish();
    }

    @Override
    public void openStudentActivity() {
        startActivity(new Intent(getBaseContext(),StudentActivity.class));
        finish();
    }

    @Override
    public void openLoginActivity() {
        startActivity(new Intent(getBaseContext(),LoginActivity.class));
        finish();
    }

    @Override
    public void setHelloImage(String text, int resourceId) {
        textHello.setText(text);
        imageHello.setImageResource(resourceId);
    }
}
