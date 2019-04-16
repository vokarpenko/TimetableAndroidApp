package kubsu.timetable.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CurrentDayWeek {

    public  int getCurrentNumberDayWeek(){
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
}
