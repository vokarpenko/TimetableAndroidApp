package kubsu.timetable.Login;

import android.text.Editable;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.Registration.RegistrationRepository;
import kubsu.timetable.Retrofit.NetworkService;
import kubsu.timetable.Utility.L;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class LoginPresenter {
    private LoginView view;
    private LoginRepository repository;

    LoginPresenter(LoginView view, LoginRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    void loginButtonClick(final Editable login, final Editable password){
        switch (repository.verifyData(password)){
            case 1:
                repository.login(login,password)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(new DisposableSingleObserver<LoginRepository.ResponseLogin>() {
                            @Override
                            public void onSuccess(LoginRepository.ResponseLogin responseLogin) {
                                if(responseLogin.isSuccess()){
                                    repository.saveSettingVisited(login,password);
                                    repository.saveAuthToken(responseLogin.getToken());
                                    view.openStudentActivity();
                                    L.log(responseLogin.getToken());
                                }
                               else view.showErrorMessage(RegistrationRepository.getError(responseLogin.getArrayErrors()));
                            }

                            @Override
                            public void onError(Throwable e) {
                                L.log(e.getLocalizedMessage());
                                view.showErrorMessage(e.getLocalizedMessage());
                            }
                        });

                break;
            case 2:
                view.showErrorMessage("Пароль не должен быть короче 6 символов");
                break;
        }
    }
    void registerButtonClick(){
        view.openRegisterActivity();
    }

    void rememberMeClick() {
        if(repository.rememberMeClick())
            view.setRememberChecked(true);
        else view.setRememberChecked(false);
    }

    void setRememberMeCheck() {
        if(repository.isRememberMe())
            view.setRememberChecked(true);
        else view.setRememberChecked(false);
    }

    void setLoginPassword() {
        if(repository.isRememberMe()){
            view.setLogin(repository.getLogin());
            view.setPassword(repository.getPassword());
        }
    }
}
