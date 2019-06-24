package kubsu.timetable.registration;

import android.text.Editable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import io.reactivex.Single;
import kubsu.timetable.retrofit.NetworkService;


public class RegistrationRepository {
    int verifyData(Editable email, Editable name, Editable patronymic, Editable surname, Editable password, Editable passwordRepeat){
        if(email.length()!=0&&patronymic.length()!=0&&name.length()!=0&& surname.length()!=0&&password.length()!=0&&passwordRepeat.length()!=0)
            if(isValidEmail(email.toString()))
                if(passwordRepeat.toString().equals(password.toString()))
                    if (password.length()>=6){
                        return 1;
                    }
                    else return 2;
                else return 3;
            else return 4;
        else return 5;
    }

    Single<ResponseRegister> register(Editable email,Editable login, Editable name, Editable patronymic, Editable surname, Editable password){
                        User user = new User(name.toString(),patronymic.toString(),surname.toString(),
                                email.toString(),login.toString(),password.toString());
                       String jsonRegister = getJSONFromUser(user);
                       return NetworkService.getInstance().getApi().register(jsonRegister);
    }

    private String getJSONFromUser(User user){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(user);
    }

    public static String getError(Map<String, String[]> errors) {
        String errorMessage="";
        for (String[] arrayString:
                errors.values()) {
            for (String error:
                    arrayString) {
                errorMessage = errorMessage.concat(error+"\n");
            }
        }
        return errorMessage;
    }

    class User {
        private String name,patronymic,surname,email,user_login, user_password;
        User(String name, String patronymic, String surname, String email,String login, String user_password) {
            this.name = name;
            this.patronymic= patronymic;
            this.surname = surname;
            this.email = email;
            this.user_password =  user_password;
            this.user_login = login;
        }

    }
    public class ResponseRegister{
        @Expose
        @SerializedName("success")
        private boolean success;
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
    }

    private  boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
