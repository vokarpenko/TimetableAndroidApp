package kubsu.timetable;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

import java.util.List;

public class AsyncRequest extends AsyncTask<String,Void,List<Day>> {
    @Override
    protected List<Day> doInBackground(String... strings) {
        String [] stringDayArray;
        String path;
        String myGroup = strings[0];
        //"http://timetable-fktpm.ru/RaspisanieServer/index.php?option=mInfo&gruppa=4kmm&nday=1"
        try {
            stringDayArray = new String[2];
            for (int i = 1; i < 3; i++) {
                String stringDay = "";
                path = "http://timetable-fktpm.ru/index.php?option=mInfo&gruppa="+myGroup+"&nday=";
                org.jsoup.nodes.Document doc = Jsoup.connect(path+Integer.toString(i)).get();
                stringDay=doc.select("timetable").text();
                stringDayArray[i-1]=stringDay;
            }
            return new DaysFromJSONarray(stringDayArray).create();
        }
        catch (Exception e){
            Log.i("myapp",e.toString());
        }
        return null;
    }

}
