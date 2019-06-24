package kubsu.timetable.registration;

import android.text.Editable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

class RegistrationPresenter {
    private RegistrationView view;
    private RegistrationRepository repository;

    RegistrationPresenter(RegistrationView view, RegistrationRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    void loginButtonClick(){
        view.openLoginActivity();
    }

    void singUpButtonClick(Editable email,Editable login, Editable name,Editable patronymic, Editable surname, Editable password, Editable passwordRepeat) {
        switch (repository.verifyData(email, name, patronymic, surname, password, passwordRepeat)) {
            case 1:
                repository.register(email, login, name, patronymic, surname, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<RegistrationRepository.ResponseRegister>() {
                    @Override
                    public void onSuccess(RegistrationRepository.ResponseRegister responseRegister) {
                        if (responseRegister.isSuccess())
                            view.openAfterRegisterActivity();
                        else
                            view.showErrorMessage(RegistrationRepository.getError(responseRegister.getArrayErrors()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMessage("Ошибка соединения");
                    }
                });
                break;
            case 2:
                view.showErrorMessage("Длина пароля должна быть не менее 6 символов");
                break;
            case 3:
                view.showErrorMessage("Пароли не совпадают");
                break;
            case 4:
                view.showErrorMessage("Некоректный email");
                break;
            case 5:
                view.showErrorMessage("Заполните все поля");
                break;
        }
    }
}
