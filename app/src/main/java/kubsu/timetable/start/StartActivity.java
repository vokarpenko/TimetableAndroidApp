package kubsu.timetable.start;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import kubsu.timetable.teacher.TeacherActivity;
import kubsu.timetable.login.LoginActivity;
import kubsu.timetable.R;
import kubsu.timetable.student.StudentActivity;

import static kubsu.timetable.utility.Constant.EXTRA_LOGIN_TYPE;
import static kubsu.timetable.utility.Constant.PREF_FILE;


public class StartActivity extends AppCompatActivity implements StartView {
    private StartPresenter presenter=null;
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
        final Button buttonTeacher = findViewById(R.id.button_teacher);
        final Button buttonStudent = findViewById(R.id.button_std);
        imageHello = findViewById(R.id.hello_image);
        if (presenter==null) {
            StartRepository repository = new StartRepository(getSharedPreferences(PREF_FILE,MODE_PRIVATE));
            presenter = new StartPresenter(this,repository);
        }
        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FlipInX).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        presenter.buttonStudentClick();
                    }
                }).playOn(buttonStudent);
            }
        });
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FlipInX).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        presenter.buttonTeacherClick();
                    }
                }).playOn(buttonTeacher);

            }
        });
        imageHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.RotateOut).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        YoYo.with(Techniques.RotateIn).playOn(imageHello);
                    }
                }).playOn(imageHello);
            }
        });
        initToolbar();
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar);
    }
    @Override
    public void openTeacherActivity() {
        startActivity(new Intent(getBaseContext(),TeacherActivity.class));
        finish();
    }

    @Override
    public void openStudentActivity() {
        startActivity(new Intent(getBaseContext(),StudentActivity.class));
        finish();
    }

    @Override
    public void openLoginActivity(int loginType) {
        Intent intent = new Intent(getBaseContext(),LoginActivity.class);
        intent.putExtra(EXTRA_LOGIN_TYPE,loginType);
        startActivity(intent);
        finish();
    }

    @Override
    public void setHelloImage(String text, int resourceId) {
        setTitle(text);
        imageHello.setImageResource(resourceId);
    }
}
