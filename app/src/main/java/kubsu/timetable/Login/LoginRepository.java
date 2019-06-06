package kubsu.timetable.Login;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import io.reactivex.Single;
import kubsu.timetable.Retrofit.NetworkService;

import static kubsu.timetable.Start.StartActivity.HAS_VISITED;
import static kubsu.timetable.Utility.Constant.PREF_AUTH_TOKEN;
import static kubsu.timetable.Utility.Constant.PREF_LOGIN;
import static kubsu.timetable.Utility.Constant.PREF_PASSWORD;
import static kubsu.timetable.Utility.Constant.PREF_REMEMBER_ME;

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

    void saveSettingVisited(Editable login, Editable password){
        setting.edit().putInt(HAS_VISITED, 1).apply();
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
        setting.edit().putString(PREF_AUTH_TOKEN,token).apply();
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

    public class Test {
        @SerializedName("user")
        @Expose
        public String user;
    }
}
