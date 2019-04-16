package kubsu.timetable.AsyncTask;

import android.os.AsyncTask;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

import kubsu.timetable.Model.Group;
import kubsu.timetable.Model.ListGroups;

public class AsyncRequestListGroup extends AsyncTask<Void,Void,List<Group>> {

    @Override
    protected List<Group> doInBackground(Void... params) {
        org.jsoup.nodes.Document doc;
        try {
            String path = "http://timetable-fktpm.ru/?option=mGrupp";
            doc = Jsoup.connect(path).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String groupsString = doc.select("timetable").text();

        return new ListGroups().createFromString(groupsString);
    }



}
