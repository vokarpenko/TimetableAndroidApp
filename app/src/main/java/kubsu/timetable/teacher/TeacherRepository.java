package kubsu.timetable.teacher;

import android.content.Context;
import android.content.SharedPreferences;

import kubsu.timetable.student.StudentRepository;

import static kubsu.timetable.utility.Constant.PREF_FILE;
import static kubsu.timetable.utility.Constant.PREF_HAS_VISITED;

class TeacherRepository {
    private Context context;
    private SharedPreferences setting;
    private StudentRepository studentRepository;
    TeacherRepository(Context context) {
        this.context = context;
        this.setting = context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        this.studentRepository = new StudentRepository(context);
    }

    void cleanSetting() {
        setting.edit().putInt(PREF_HAS_VISITED,0).apply();
    }


    String getTittle() {
        return studentRepository.getUserSurname()+" "+studentRepository.getUserName()+" "+studentRepository.getUserPatronymic();
    }
}
