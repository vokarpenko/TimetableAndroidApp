package kubsu.timetable;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DaysFromJSONarray {
    private String[] stringDayArray;

    DaysFromJSONarray(String[] stringDayArray){
        this.stringDayArray=stringDayArray;
    }

    private String getTimePar(String num_par){
        switch (num_par){
            case "1":
                return "8:00-9:30";
            case "2":
                return "9:40-11:10";
            case "3":
                return "11:30-13:00";
            case "4":
                return "13:10-14:40";
            case "5":
                return "15:00-16:30";
            case "6":
                return "16:40-18:10";
            case "7":
                return "18:20-19:50";
            case "8":
                return "20:00-21:30";
                default:return "error_number_par";
        }
    }

    List<Day> create(){
        try {
            List<Day> days=new ArrayList<>();
            String[] dateArray = getDateArray();
            for (int i = 0; i < stringDayArray.length; i++) {
                JSONArray jsonArray = new JSONArray(stringDayArray[i]);
                int arrayLenth = jsonArray.length();
                String[] times = new String[arrayLenth];
                String[] subjects = new String[arrayLenth];
                String[] teachers = new String[arrayLenth];
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(j);
                    times[j] = getTimePar(jsonObject.getString("num_par"));
                    subjects[j] = jsonObject.getString("nam_predmet");
                    teachers[j]=jsonObject.getString("nam_prepod");
                }
                days.add(new Day(times, subjects, teachers, dateArray[i]));
            }
            return days;
        }
        catch (JSONException e) {
            Log.i("mytag", "unexpected JSON exception", e);
        }
        return null;
    }

    static public int getDayWeekNumber(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E", new Locale("ru"));
        Date current_date = new Date();
        String string_date = simpleDateFormat.format(current_date);
        int day_number;
        switch (string_date) {
            case "пн":
                day_number = 0;
                break;
            case "вт":
                day_number = 1;
                break;
            case "ср":
                day_number = 2;
                break;
            case "чт":
                day_number = 3;
                break;
            case "пт":
                day_number = 4;
                break;
            case "сб":
                day_number = 5;
                break;
            case "вс":
                day_number = 6;
                break;
            default:
                day_number = -1;
                break;
        }
        return day_number;
    }

    private String[] getDateArray() {
        String[] date_array = new String[14];
        int day_week_number = getDayWeekNumber();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
        Date date = new Date();
        Date newDate;
        int day_dec=0;
        for (int i = day_week_number; i >-1 ; i--) {
            newDate = addDays(date,day_dec);
            String string_date = simpleDateFormat.format(newDate);
            date_array[i]=string_date;
            day_dec-=1;
        }
        int day_inc=1;
        for (int i = day_week_number+1; i <date_array.length ; i++) {
            newDate=addDays(date,day_inc);
            String string_date = simpleDateFormat.format(newDate);
            date_array[i]=string_date;
            day_inc+=1;
        }
        return date_array;
    }
    private static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}

class Day {
   String[] times;
   String[] subjects;
   String[] teachers;
   String date;

    public String[] getTimes() {
        return times;
    }

    public void setTimes(String[] times) {
        this.times = times;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }

    public String[] getTeachers() {
        return teachers;
    }

    public void setTeachers(String[] teachers) {
        this.teachers = teachers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    Day(String[] times, String[] subjects, String[] teachers, String date) {
       this.times = times;
       this.subjects = subjects;
       this.date = date;
       this.teachers=teachers;
   }
}