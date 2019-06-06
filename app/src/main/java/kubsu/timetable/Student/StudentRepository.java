package kubsu.timetable.Student;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

import io.reactivex.Single;
import kubsu.timetable.Database.AppDatabase;
import kubsu.timetable.Database.TimetableDao;
import kubsu.timetable.Retrofit.NetworkService;
import kubsu.timetable.Student.TimeTable.Lesson;
import kubsu.timetable.Utility.Internet;

import static android.content.Context.MODE_PRIVATE;
import static kubsu.timetable.Start.StartActivity.HAS_VISITED;
import static kubsu.timetable.Utility.Constant.PREF_DATA_GROUPS;
import static kubsu.timetable.Utility.Constant.PREF_DATA_LESSONS;
import static kubsu.timetable.Utility.Constant.PREF_FILE;
import static kubsu.timetable.Utility.Constant.PREF_GROUP;
import static kubsu.timetable.Utility.Constant.PREF_SUBGROUP;

public class StudentRepository {
    private Context context;
    private SharedPreferences settings;
    private TimetableDao timetableDao;
    StudentRepository(Context context) {
        this.context = context;
        this.settings =context.getSharedPreferences(PREF_FILE,MODE_PRIVATE);
        this.timetableDao = AppDatabase.getDatabase(context).timetableDao();
    }

    @NonNull
    public static Fragment getFragment(Class fragmentClass){
        try {
            return (Fragment) fragmentClass.newInstance();
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

    void cleanGroup() {
        settings.edit().putInt(HAS_VISITED,0).apply();
    }
    String getGroup(){
        return settings.getString(PREF_GROUP,"");
    }
    String getSubgroup(){
        return settings.getString(PREF_SUBGROUP,"");
    }

    Single<List<Lesson>> downloadDataLessons() {
        return NetworkService.getInstance().getApi().getAllClasses();
    }

    void saveData(List<Lesson> lessons) {
        timetableDao.insertAllLessons(lessons);
    }

    Single<List<GroupEntity>> downloadDataGroups() {
        return NetworkService.getInstance().getApi().getListGroup();
    }

    void saveGroup(List<GroupEntity> groupEntities) {
        timetableDao.insertAllGroups(groupEntities);
    }

    boolean hasData() {
        return settings.getBoolean(PREF_DATA_LESSONS,false)&&settings.getBoolean(PREF_DATA_GROUPS,false);
    }

    void setDataGroups() {
        settings.edit().putBoolean(PREF_DATA_GROUPS,true).apply();
    }
    void setDataLessons() {
        settings.edit().putBoolean(PREF_DATA_LESSONS,true).apply();
    }
}
