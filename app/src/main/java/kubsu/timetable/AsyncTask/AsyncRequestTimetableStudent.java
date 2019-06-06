package kubsu.timetable.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

import java.util.Calendar;
import java.util.List;


/*public class AsyncRequestTimetableStudent extends AsyncTask<String,Void,List<Day>> {
    @Override
    protected List<Day> doInBackground(String... strings) {
        String [] stringDayArray;
        String path;
        String group = strings[0];
        String subGroup=strings[1];
        //"http://timetable-fktpm.ru/index.php?option=mInfo&gruppa=35&subgroup=1&nday=1"
        try {
            stringDayArray = new String[14];
            Calendar now = Calendar.getInstance();
            Calendar startClasses=Calendar.getInstance();
            startClasses.set(Calendar.YEAR,2018);
            startClasses.set(Calendar.MONTH,9);
            startClasses.set(Calendar.DATE,1);
            int startClassesWeek = startClasses.get(Calendar.WEEK_OF_YEAR);
            int currentWeek = now.get(Calendar.WEEK_OF_YEAR);
            int parityCurrentWeek = currentWeek-startClassesWeek;
            Log.i("mytag",group);
            path = "http://timetable-fktpm.ru/index.php?option=mInfo&gruppa="+group+"&subgroup="+subGroup+"&nday=";
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
            return new ListDays(stringDayArray).getList();
        }
        catch (Exception e){
            Log.i("myapp",e.toString());
        }
        return null;
    }

}*/
