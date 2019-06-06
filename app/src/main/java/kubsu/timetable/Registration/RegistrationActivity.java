package kubsu.timetable.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kubsu.timetable.AfterRegister.AfterRegisterActivity;
import kubsu.timetable.Login.LoginActivity;
import kubsu.timetable.R;

public class RegistrationActivity extends AppCompatActivity implements RegistrationView{
    private EditText nameText ;
    private EditText loginText;
    private EditText surnameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText passwordRepeatText,patronymicText;
    private RegistrationPresenter presenter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init() {
        nameText = findViewById(R.id.input_name);
        patronymicText = findViewById(R.id.input_patronymic);
        surnameText = findViewById(R.id.input_family);
        emailText = findViewById(R.id.input_email_singup);
        passwordText = findViewById(R.id.input_password_singup);
        passwordRepeatText = findViewById(R.id.input_password_singup_repeat);
        loginText = findViewById(R.id.input_login_sign_up);
        Button singUpButton = findViewById(R.id.singup_button);
        TextView loginButton = findViewById(R.id.link_login);
        if(presenter==null){
            presenter = new RegistrationPresenter(this,new RegistrationRepository());
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loginButtonClick();
            }
        });
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.singUpButtonClick(emailText.getText(),loginText.getText(),nameText.getText(),patronymicText.getText(),
                        surnameText.getText(),passwordText.getText(),passwordRepeatText.getText());
            }
        });
    }

    @Override
    public void openLoginActivity() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void openAfterRegisterActivity() {
        startActivity(new Intent(getApplicationContext(), AfterRegisterActivity.class));
        finish();
    }
}
