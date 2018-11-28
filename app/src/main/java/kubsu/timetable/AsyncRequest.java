package kubsu.timetable;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

import java.util.Calendar;
import java.util.List;

public class AsyncRequest extends AsyncTask<String,Void,List<Day>> {
    @Override
    protected List<Day> doInBackground(String... strings) {
        String [] stringDayArray;
        String path;
        String myGroup = strings[0];
        //"http://timetable-fktpm.ru/index.php?option=mInfo&gruppa=4kmm&nday=1"
        try {
            stringDayArray = new String[14];
            Calendar now = Calendar.getInstance();
            Calendar startClasses=Calendar.getInstance();
            startClasses.set(Calendar.YEAR,2018);
            startClasses.set(Calendar.MONTH,8);
            startClasses.set(Calendar.DATE,1);
            int startClassesWeek = startClasses.get(Calendar.WEEK_OF_YEAR);
            int currentWeek = now.get(Calendar.WEEK_OF_YEAR);
            int parityCurrentWeek = currentWeek-startClassesWeek;
            path = "http://timetable-fktpm.ru/index.php?option=mInfo&gruppa="+myGroup+"&nday=";
            if (parityCurrentWeek%2==1){
                //если числитель
                for (int i = 1; i <=stringDayArray.length; i++) {
                    String stringDay = "";
                    org.jsoup.nodes.Document doc = Jsoup.connect(path+Integer.toString(i)).get();
                    stringDay=doc.select("timetable").text();
                    stringDayArray[i-1]=stringDay;
                }
            }
            else {
                //если знаменатель
                for (int i = 1; i <=7; i++) {
                    String stringDay = "";
                    org.jsoup.nodes.Document doc = Jsoup.connect(path+Integer.toString(i+7)).get();
                    stringDay=doc.select("timetable").text();
                    stringDayArray[i-1]=stringDay;
                }
                for (int i = 8; i <=stringDayArray.length; i++) {
                    String stringDay = "";
                    org.jsoup.nodes.Document doc = Jsoup.connect(path+Integer.toString(i-7)).get();
                    stringDay=doc.select("timetable").text();
                    stringDayArray[i-1]=stringDay;
                }
            }
            return new DaysFromJSONarray(stringDayArray).create();
        }
        catch (Exception e){
            Log.i("myapp",e.toString());
        }
        return null;
    }

}
