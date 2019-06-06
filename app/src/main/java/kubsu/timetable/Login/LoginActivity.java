package kubsu.timetable.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kubsu.timetable.Student.ChangeGroup.ChangeGroupFragment;
import kubsu.timetable.Registration.RegistrationActivity;
import kubsu.timetable.R;
import kubsu.timetable.Student.StudentActivity;

import static kubsu.timetable.Utility.Constant.PREF_FILE;


public class LoginActivity extends AppCompatActivity implements LoginView {
    private EditText inputLogin, inputPassword;
    private LoginPresenter presenter;
    private CheckedTextView rememberMeCheckTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (presenter==null) {
            LoginRepository repository = new LoginRepository(getSharedPreferences(PREF_FILE, MODE_PRIVATE));
            presenter = new LoginPresenter(this, repository);
        }
        init();
    }

    private void init() {
        inputLogin = findViewById(R.id.input_login_login);
        inputPassword = findViewById(R.id.input_password_login);
        presenter.setLoginPassword();
        Button loginButton = findViewById(R.id.login_button);
        TextView registerButton = findViewById(R.id.link_registration);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             presenter.loginButtonClick(inputLogin.getText(),inputPassword.getText());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.registerButtonClick();
            }
        });
        rememberMeCheckTextView = findViewById(R.id.remember_me);
        rememberMeCheckTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.rememberMeClick();
            }
        });
        presenter.setRememberMeCheck();
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getBaseContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openStudentActivity() {
        startActivity(new Intent(getBaseContext(),StudentActivity.class));
        finish();
    }

    @Override
    public void openRegisterActivity() {
        startActivity(new Intent(getBaseContext(),RegistrationActivity.class));
        finish();
    }

    @Override
    public void setRememberChecked(boolean checked) {
        rememberMeCheckTextView.setChecked(checked);
    }

    @Override
    public void setLogin(String login) {
        inputLogin.setText(login);
    }

    @Override
    public void setPassword(String password) {
        inputPassword.setText(password);
    }
}
