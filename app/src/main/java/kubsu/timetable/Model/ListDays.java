package kubsu.timetable.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kubsu.timetable.Model.Day;
import kubsu.timetable.Utility.CurrentDayWeek;

public class ListDays implements Serializable {
    private List<Day> list;

    public ListDays(List<Day> list) {
        this.list = list;
    }

    public ListDays(String[] stringArrayDays) {
        createFromStringArrayDays(stringArrayDays);
    }

    public List<Day> getList() {
        return list;
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

    private void createFromStringArrayDays(String[] stringArrayDays){
        try {
            List<Day> days=new ArrayList<>();
            String[] dateArray = getDateArray();
            for (int i = 0; i < stringArrayDays.length; i++) {
                JSONArray jsonArray = new JSONArray(stringArrayDays[i]);
                int arrayLenth = jsonArray.length();
                String[] times = new String[arrayLenth];
                String[] subjects = new String[arrayLenth];
                String[] teachers = new String[arrayLenth];

                //извлечение значений для сортировки массива
                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                for (int z = 0; z < jsonArray.length(); z++) {
                    jsonValues.add(jsonArray.getJSONObject(z));
                }
                //сортировка по возрастанию времени пары
                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                    private static final String KEY = "num_par";
                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = "";
                        String valB = "";

                        try {
                            valA = (String) a.get(KEY);
                            valB = (String) b.get(KEY);
                        }
                        catch (JSONException e) {
                            Log.i("mytag",e.toString());
                        }
                        return valA.compareTo(valB);
                    }
                });
                jsonArray = new JSONArray();
                for (int z = 0; z < arrayLenth; z++) {
                    jsonArray.put(jsonValues.get(z));
                }

                //заполнение одного дня с учетом сортировки
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject;
                    jsonObject = jsonArray.getJSONObject(j);
                    times[j] = getTimePar(jsonObject.getString("num_par"));
                    subjects[j] = jsonObject.getString("nam_predmet");
                    teachers[j]=jsonObject.getString("nam_prepod");
                }
                days.add(new Day(times, subjects, teachers, dateArray[i]));
            }
            this.list = days;
        }
        catch (JSONException e) {
            Log.i("mytag", "unexpected JSON exception", e);
        }
    }




    private String[] getDateArray() {
        String[] date_array = new String[14];
        int day_week_number = new CurrentDayWeek().getCurrentNumberDayWeek();
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
    private Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}


