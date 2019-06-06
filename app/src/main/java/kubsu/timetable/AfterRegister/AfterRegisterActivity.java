package kubsu.timetable.AfterRegister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kubsu.timetable.Login.LoginActivity;
import kubsu.timetable.R;

public class AfterRegisterActivity extends AppCompatActivity implements AfterRegisterView {
    AfterRegisterPresenter presenter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);
        if (presenter==null) presenter = new AfterRegisterPresenter(this);
        init();
    }

    void init(){
        Button buttonGoLogin = findViewById(R.id.login_button_after_register);
        buttonGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openLoginActivity();
            }
        });
    }

    @Override
    public void openLoginActivity() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}
