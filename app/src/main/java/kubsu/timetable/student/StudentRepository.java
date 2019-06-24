package kubsu.timetable.student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.database.AppDatabase;
import kubsu.timetable.database.TimetableDao;
import kubsu.timetable.retrofit.NetworkService;
import kubsu.timetable.timetable.Lesson;
import kubsu.timetable.utility.Internet;
import kubsu.timetable.utility.L;

import static android.content.Context.MODE_PRIVATE;
import static kubsu.timetable.utility.Constant.EXTRA_TIMETABLE_TYPE;
import static kubsu.timetable.utility.Constant.PREF_AUTH_TOKEN;
import static kubsu.timetable.utility.Constant.PREF_DATA_GROUPS;
import static kubsu.timetable.utility.Constant.PREF_DATA_LESSONS;
import static kubsu.timetable.utility.Constant.PREF_FILE;
import static kubsu.timetable.utility.Constant.PREF_GROUP;
import static kubsu.timetable.utility.Constant.PREF_HAS_VISITED;
import static kubsu.timetable.utility.Constant.PREF_SUBGROUP;
import static kubsu.timetable.utility.Constant.PREF_USER_EMAIL;
import static kubsu.timetable.utility.Constant.PREF_USER_NAME;
import static kubsu.timetable.utility.Constant.PREF_USER_PATRONYMIC;
import static kubsu.timetable.utility.Constant.PREF_USER_STATUS;
import static kubsu.timetable.utility.Constant.PREF_USER_SURNAME;

public class StudentRepository {
    private Context context;
    private SharedPreferences setting;
    private TimetableDao timetableDao;

    public StudentRepository(Context context) {
        this.context = context;
        this.setting =context.getSharedPreferences(PREF_FILE,MODE_PRIVATE);
        this.timetableDao = AppDatabase.getDatabase(context).timetableDao();
    }

    @NonNull
    public static Fragment getFragment(Class fragmentClass){
        try {Fragment fragment = (Fragment)  fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_TIMETABLE_TYPE,0);
            fragment.setArguments(bundle);
            return fragment;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return new Fragment();
        }
    }
    boolean isConnection(){
        return new Internet(context).isConnection();
    }

    boolean hasGroup(){
        return !getGroup().equals("");
    }

    public void updateLessons(List<Lesson> lessons ){
        timetableDao.deleteLessons();
        timetableDao.insertAllLessons(lessons);
    }

    void cleanGroup() {
        setting.edit().putInt(PREF_HAS_VISITED,0).apply();
    }

    String getGroup(){
        return setting.getString(PREF_GROUP,"");
    }

    String getSubgroup(){
        return setting.getString(PREF_SUBGROUP,"");
    }

    public Single<List<Lesson>> downloadDataLessons() {
        return NetworkService.getInstance().getApi().getAllClasses();
    }



    public void saveLessons(List<Lesson> lessons) {
        timetableDao.insertAllLessons(lessons);
    }

    Single<List<GroupEntity>> downloadDataGroups() {
        return NetworkService.getInstance().getApi().getListGroup();
    }

    void saveGroups(List<GroupEntity> groupEntities) {
        timetableDao.insertAllGroups(groupEntities);

        AppDatabase.getDatabase(context).timetableDao().deleteLessons();



    }

    boolean hasData() {
        return setting.getBoolean(PREF_DATA_LESSONS,false)&& setting.getBoolean(PREF_DATA_GROUPS,false);
    }

    void setDataGroups() {
        setting.edit().putBoolean(PREF_DATA_GROUPS,true).apply();
    }

    void setDataLessons() {
        setting.edit().putBoolean(PREF_DATA_LESSONS,true).apply();
    }

    private String getAuthToken() {
        return setting.getString(PREF_AUTH_TOKEN,"");
    }

    String getTextHeader() {
        String textHeader = "Группа "+getGroup()+" "+getSubgroup();
        return textHeader+"\n"+getUserName()+" "+getUserSurname();
    }

    public String getUserName(){
        return setting.getString(PREF_USER_NAME,"");
    }

    public String getUserSurname(){
        return setting.getString(PREF_USER_SURNAME,"");
    }

    public String getUserPatronymic(){
        return setting.getString(PREF_USER_PATRONYMIC,"");
    }

    public class UserInfo {
        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("surname")
        @Expose
        private String surname;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("patronymic")
        @Expose
        private String patronymic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public void setPatronymic(String patronymic) {
            this.patronymic = patronymic;
        }
    }
}

