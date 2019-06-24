package kubsu.timetable.login;

import android.content.SharedPreferences;
import android.text.Editable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import io.reactivex.Emitter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.retrofit.NetworkService;
import kubsu.timetable.student.StudentRepository;
import kubsu.timetable.utility.L;

import static kubsu.timetable.utility.Constant.PREF_AUTH_TOKEN;
import static kubsu.timetable.utility.Constant.PREF_HAS_VISITED;
import static kubsu.timetable.utility.Constant.PREF_LOGIN;
import static kubsu.timetable.utility.Constant.PREF_PASSWORD;
import static kubsu.timetable.utility.Constant.PREF_REMEMBER_ME;
import static kubsu.timetable.utility.Constant.PREF_USER_EMAIL;
import static kubsu.timetable.utility.Constant.PREF_USER_NAME;
import static kubsu.timetable.utility.Constant.PREF_USER_PATRONYMIC;
import static kubsu.timetable.utility.Constant.PREF_USER_STATUS;
import static kubsu.timetable.utility.Constant.PREF_USER_SURNAME;

public class LoginRepository {
    private SharedPreferences setting;
    private boolean rememberMe;

    public boolean isRememberMe() {
        return rememberMe;
    }


    LoginRepository(SharedPreferences setting) {
        this.setting = setting;
        rememberMe = setting.getBoolean(PREF_REMEMBER_ME,false);
    }

    int verifyData(Editable password){
            if (password.length() >= 6) {
                return 1;
            }
            else {
                return 2;
            }
    }

    void saveSettingVisited(Editable login, Editable password,int loginType){
        if (loginType==0)
            setting.edit().putInt(PREF_HAS_VISITED, 1).apply();
        if (loginType==1)
            setting.edit().putInt(PREF_HAS_VISITED, 2).apply();
        if (rememberMe){
            setting.edit().putString(PREF_LOGIN,login.toString())
                    .putString(PREF_PASSWORD,password.toString())
                    .putBoolean(PREF_REMEMBER_ME,true)
                    .apply();
        }
        else {
            setting.edit().putString(PREF_LOGIN,"")
                    .putString(PREF_PASSWORD,"")
                    .putBoolean(PREF_REMEMBER_ME,false)
                    .apply();
        }
    }

    public Single<ResponseLogin> login(Editable login, Editable password) {
        User user = new User(password.toString(),login.toString());
        String jsonLogin = getJSONFromUser(user);
         return NetworkService.getInstance().getApi().login(jsonLogin);
    }

    void saveAuthToken(String token) {
        setting.edit().putString(PREF_AUTH_TOKEN,"Bearer "+token).apply();
    }

    boolean rememberMeClick() {
        rememberMe = !rememberMe;
        return rememberMe;
    }

    String getLogin() {
        return setting.getString(PREF_LOGIN,"");
    }

    String getPassword() {
        return setting.getString(PREF_PASSWORD,"");
    }

    String getAuthToken() {
        return setting.getString(PREF_AUTH_TOKEN,"");
    }

    public class User{
        String user_password;
        String user_login;

        User(String user_password, String user_login) {
            this.user_password = user_password;
            this.user_login = user_login;
        }
    }

    public class ResponseLogin {
        @Expose
        @SerializedName("success")
        private boolean success;
        @Expose
        @SerializedName("token")
        private String token;
        @Expose
        @SerializedName("errors")
        private Map<String ,String[]> arrayErrors;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Map<String ,String[]> getArrayErrors() {
            return arrayErrors;
        }

        public void setArrayErrors(Map<String ,String[]> arrayErrors) {
            this.arrayErrors = arrayErrors;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    private String getJSONFromUser(User user){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(user);
    }

    void getUserInfo(String authToken) {
        NetworkService.getInstance().getApi().getUserInfo(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<StudentRepository.UserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(StudentRepository.UserInfo userInfo) {
                        saveUserInfo(userInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.log(e.toString());
                    }
                });
    }

    private void saveUserInfo(StudentRepository.UserInfo userInfo) {
        setting.edit().putString(PREF_USER_NAME,userInfo.getName())
                .putString(PREF_USER_SURNAME,userInfo.getSurname())
                .putString(PREF_USER_PATRONYMIC,userInfo.getPatronymic())
                .putString(PREF_USER_EMAIL,userInfo.getEmail())
                .putString(PREF_USER_STATUS,userInfo.getStatus())
                .apply();
    }
}
