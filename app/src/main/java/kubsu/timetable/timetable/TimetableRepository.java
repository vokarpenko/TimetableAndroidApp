package kubsu.timetable.timetable;

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
import java.util.TimeZone;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kubsu.timetable.database.AppDatabase;
import kubsu.timetable.database.TimetableDao;
import kubsu.timetable.student.StudentRepository;
import kubsu.timetable.utility.L;

import static android.content.Context.MODE_PRIVATE;
import static kubsu.timetable.utility.Constant.PREF_FILE;
import static kubsu.timetable.utility.Constant.PREF_GROUP;
import static kubsu.timetable.utility.Constant.PREF_SUBGROUP;
import static kubsu.timetable.utility.Constant.PREF_USER_NAME;
import static kubsu.timetable.utility.Constant.PREF_USER_PATRONYMIC;
import static kubsu.timetable.utility.Constant.PREF_USER_SURNAME;

class TimetableRepository {
    interface UpdateCallback{
        void onSuccess();
        void onFailure();
    }

    private TimetableDao timetableDao;
    private SharedPreferences setting;
    private String group,subgroup;
    private StudentRepository studentRepository;
    TimetableRepository(Context context) {
        this.timetableDao = AppDatabase.getMemoryDatabase(context).timetableDao();
        setting = context.getSharedPreferences(PREF_FILE,MODE_PRIVATE);
        group=getGroup();
        subgroup=getSubGroup();
        this.studentRepository = new StudentRepository(context);
    }

    List<Day> getTimetable(int timetableType) {
        List<Day> days = createListDay(timetableType);
        for (Day day:
             days) {
            for (Lesson lesson:
                 day.getLessons()) {
                lesson.setTime(getLessonTime(lesson.getClassNumber()));
                L.log(day.getLessons().get(0).getTeacher());
            }
        }
        return days;
    }

    private List<Day> createListDay(int timetableType){
        List<Lesson> lessons = new ArrayList<>();
        List<Day> days = new ArrayList<>();
        String[] stringDateArray = getStringDateArray();
        Date[] dateArray = getDateArray();
        for( int i=1; i<=14; i++){
            if (timetableType==0)
                lessons = timetableDao.getLessonsDayStudent(group,subgroup,String.valueOf(i));
            if (timetableType==1) {
                lessons = timetableDao.getLessonsDayTeacher(getTeacher(), String.valueOf(i));
                lessons = unionLessons(lessons);
            }
                for (int j = 0; j < lessons.size(); j++) {
                    lessons.get(j).setTimeStamp(getLessonTimeStamp(lessons.get(j).getClassNumber(), dateArray[i-1]));
                }
            String date = stringDateArray[i-1];
            lessons = sortLessons(lessons);
            days.add(new Day(lessons,date));
        }
        return days;
    }

    private List<Lesson> unionLessons(List<Lesson> lessons) {
        List<Lesson> teacherLessons = new ArrayList<>();
        for (int i = 0; i < lessons.size(); i++)
            lessons.get(i).setTeacher(lessons.get(i).getGroup()+"/"+lessons.get(i).getSubgroup());
        if (lessons.size()<=1)
            return lessons;
        for (int i = 0; i < lessons.size(); i++) {
            for (int j = i+1; j < lessons.size(); j++) {
                if (lessons.get(i).getClassNumber().equals(lessons.get(j).getClassNumber())){
                    lessons.get(i).setTeacher(lessons.get(i).getTeacher()+"; "+lessons.get(j).getTeacher());
                }
                if (!containLesson(teacherLessons,lessons.get(i).getClassNumber()))
                    teacherLessons.add(lessons.get(i));

            }
        }
        return teacherLessons;
    }

    private boolean containLesson(List<Lesson> teacherLessons, int numberLesson){
        for (Lesson lesson:
             teacherLessons) {
            if (lesson.getClassNumber()==numberLesson)
                return true;
        }
        return false;
    }

    private String getTeacher() {
        return getUserSurname()+" "+getUserName().charAt(0)+"."+getUserPatronymic().charAt(0)+".";

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
    private Date[] getDateArray() {
        Date[] dateArray = new Date[14];
        int dayWeekNumber = getCurrentNumberDayWeek();
        Date date = new Date();
        Date newDate;
        int dayDec=0;
        for (int i = dayWeekNumber; i >-1 ; i--) {
            newDate = addDays(date, dayDec);
            dateArray[i] = newDate;
            dayDec -= 1;
        }
        int dayInc=1;
        for (int i = dayWeekNumber+1; i <dateArray.length ; i++) {
            newDate=addDays(date,dayInc);
            dateArray[i]=newDate;
            dayInc+=1;
        }
        return dateArray;
    }

    private String getGroup(){
        return setting.getString(PREF_GROUP,"");
    }

    private String getSubGroup(){
        return setting.getString(PREF_SUBGROUP,"");
    }

    private String[] getStringDateArray() {
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

    private long getLessonTimeStamp(int lessonNumber,Date day){
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.SECOND,0);
        switch (lessonNumber){
            case 1:
                cal.set(Calendar.HOUR_OF_DAY,8);
                cal.set(Calendar.MINUTE,0);
                break;
            case 2:
                cal.set(Calendar.HOUR_OF_DAY,9);
                cal.set(Calendar.MINUTE,40);
                break;
            case 3:
                cal.set(Calendar.HOUR_OF_DAY,11);
                cal.set(Calendar.MINUTE,30);
                break;
            case 4:
                cal.set(Calendar.HOUR_OF_DAY,13);
                cal.set(Calendar.MINUTE,10);
                break;
            case 5:
                cal.set(Calendar.HOUR_OF_DAY,15);
                cal.set(Calendar.MINUTE,0);
                break;
            case 6:
                cal.set(Calendar.HOUR_OF_DAY,16);
                cal.set(Calendar.MINUTE,40);
                break;
            case 7:
                cal.set(Calendar.HOUR_OF_DAY,18);
                cal.set(Calendar.MINUTE,20);
                break;
            case 8:
                cal.set(Calendar.HOUR_OF_DAY,20);
                cal.set(Calendar.MINUTE,0);
                break;
                default:return 0;
        }
        cal.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return cal.getTimeInMillis()/1000;
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

    int getCurrentNumberDayWeek(){
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

    void refreshTimetable(final UpdateCallback updateCallback){
        studentRepository.downloadDataLessons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Lesson>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<Lesson> lessons) {
                        studentRepository.updateLessons(lessons);
                        updateCallback.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateCallback.onFailure();
                    }
                });
    }
}
