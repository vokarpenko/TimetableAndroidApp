package kubsu.timetable.Student.TimeTable;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kubsu.timetable.Database.AppDatabase;
import kubsu.timetable.Database.TimetableDao;

import static android.content.Context.MODE_PRIVATE;
import static kubsu.timetable.Utility.Constant.PREF_AUTH_TOKEN;
import static kubsu.timetable.Utility.Constant.PREF_FILE;
import static kubsu.timetable.Utility.Constant.PREF_GROUP;
import static kubsu.timetable.Utility.Constant.PREF_SUBGROUP;

class TimetableRepository {
    private TimetableDao timetableDao;
    private SharedPreferences setting;
    private String group,subgroup;

    TimetableRepository(Context context) {
        this.timetableDao = AppDatabase.getMemoryDatabase(context).timetableDao();
        setting = context.getSharedPreferences(PREF_FILE,MODE_PRIVATE);
        group=getGroup();
        subgroup=getSubGroup();
    }

    List<Day> getTimetable() {
        List<Day> days = createListDay();
        for (Day day:
             days) {
            for (Lesson lesson:
                 day.getLessons()) {
                lesson.setTime(getLessonTime(lesson.getClassNumber()));
            }
        }
        return days;
    }

    private List<Day> createListDay(){
        List<Day> days = new ArrayList<>();
        String[] dateArray = getDateArray();
        for( int i=1; i<=14; i++){
            List<Lesson> lessons = timetableDao.getLessonsDay(group,subgroup,String.valueOf(i));
            String date = dateArray[i-1];
            lessons = sortLessons(lessons);
            days.add(new Day(lessons,date));
        }
        return days;
    }

    private String getGroup(){
        return setting.getString(PREF_GROUP,"");
    }

    private String getSubGroup(){
        return setting.getString(PREF_SUBGROUP,"");
    }

    private String[] getDateArray() {
        String[] dateArray = new String[14];
        int dayWeekNumber = getCurrentNumberDayWeek();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
        Date date = new Date();
        Date newDate;
        int dayDec=0;
        for (int i = dayWeekNumber; i >-1 ; i--) {
            newDate = addDays(date, dayDec);
            String stringDate = simpleDateFormat.format(newDate);
            dateArray[i] = stringDate;
            dayDec -= 1;
        }

        int dayInc=1;
        for (int i = dayWeekNumber+1; i <dateArray.length ; i++) {
            newDate=addDays(date,dayInc);
            String stringDate = simpleDateFormat.format(newDate);
            dateArray[i]=stringDate;
            dayInc+=1;
        }
        return dateArray;
    }

    private Date addDays(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    private String getLessonTime(Integer lessonNumber){
        switch (lessonNumber){
            case 1:
                return "8:00-9:30";
            case 2:
                return "9:40-11:10";
            case 3:
                return "11:30-13:00";
            case 4:
                return "13:10-14:40";
            case 5:
                return "15:00-16:30";
            case 6:
                return "16:40-18:10";
            case 7:
                return "18:20-19:50";
            case 8:
                return "20:00-21:30";
            default:return "";
        }
    }

    private List<Lesson> sortLessons(List<Lesson> lessons){
        Collections.sort(lessons, new Comparator<Lesson>() {
            @Override
            public int compare(Lesson l1, Lesson l2) {
                return l1.getClassNumber().compareTo(l2.getClassNumber());
            }
        });
        return lessons;
    }

    public int getCurrentNumberDayWeek(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E", new Locale("ru"));
        Date currentDate = new Date();
        String stringDate = simpleDateFormat.format(currentDate);
        int dayNumber;
        switch (stringDate) {
            case "пн":
                dayNumber = 0;
                break;
            case "вт":
                dayNumber = 1;
                break;
            case "ср":
                dayNumber = 2;
                break;
            case "чт":
                dayNumber = 3;
                break;
            case "пт":
                dayNumber = 4;
                break;
            case "сб":
                dayNumber = 5;
                break;
            case "вс":
                dayNumber = 6;
                break;
            default:
                dayNumber = -1;
                break;
        }
        return dayNumber;
    }

    String getAuthToken() {
        return setting.getString(PREF_AUTH_TOKEN,"");
    }
}
