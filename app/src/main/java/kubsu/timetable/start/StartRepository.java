package kubsu.timetable.start;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kubsu.timetable.R;

import static kubsu.timetable.utility.Constant.PREF_HAS_VISITED;

class StartRepository {
    class Hello{
        private int resourceId;
        private String text;

        Hello(int resourceId, String text) {
            this.resourceId = resourceId;
            this.text = text;
        }

        int getResourceId() {
            return resourceId;
        }

        String getText() {
            return text;
        }
    }

    private SharedPreferences setting;

    StartRepository(SharedPreferences setting) {
        this.setting = setting;
    }

    int checkFirstOpen(){
        int hasVisited = setting.getInt(PREF_HAS_VISITED, 0);
       switch (hasVisited){
           case 1:return 1;
           case 2:return 2;
           default:return 0;
       }
    }

    Hello getHelloImageAndText(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH", new Locale("ru"));
        Date date = new Date();
        int currentTime = Integer.parseInt(simpleDateFormat.format(date));
        if (currentTime>=6&currentTime<=10){
            return new Hello(R.drawable.sunrise,"Доброе утро");
        }
        else if (currentTime>=11&currentTime<=17){
            return new Hello(R.drawable.day,"Добрый день");
        }
        else if (currentTime>=18&currentTime<=21){
            return new Hello(R.drawable.evening,"Добрый вечер");
        }
        else {
            return new Hello(R.drawable.night,"Доброй ночи");
        }
    }
}
