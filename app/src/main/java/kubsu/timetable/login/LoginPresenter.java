package kubsu.timetable.login;

import android.text.Editable;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.registration.RegistrationRepository;
import kubsu.timetable.retrofit.NetworkService;
import kubsu.timetable.student.StudentRepository;
import kubsu.timetable.utility.L;

import static kubsu.timetable.utility.Constant.PREF_USER_EMAIL;
import static kubsu.timetable.utility.Constant.PREF_USER_NAME;
import static kubsu.timetable.utility.Constant.PREF_USER_PATRONYMIC;
import static kubsu.timetable.utility.Constant.PREF_USER_STATUS;
import static kubsu.timetable.utility.Constant.PREF_USER_SURNAME;

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
                                    repository.saveAuthToken(responseLogin.getToken());

                                    repository.getUserInfo(repository.getAuthToken());

                                    int loginType = view.getLoginType();
                                    repository.saveSettingVisited(login,password,loginType);
                                    if(loginType==0) {
                                        view.openStudentActivity();
                                    }
                                    if(loginType==1) {
                                        view.openTeacherActivity();
                                    }
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

    private void openViewAfterLogin(){

    }
}
