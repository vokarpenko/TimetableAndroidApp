package kubsu.timetable;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AsyncRequestListGroup extends AsyncTask<Void,Void,List<AsyncRequestListGroup.Group>> {
    String path = "http://timetable-fktpm.ru/?option=mGrupp";

    @Override
    protected List<Group> doInBackground(Void... params) {
        org.jsoup.nodes.Document doc;
        try {
            doc = Jsoup.connect(path).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String groupsString = doc.select("timetable").text();

        return new ListGroupFromString(groupsString).create();
    }

    class Group{
        private String group;
        private List<String> subgroups;
        private String course;

        public String getGroup() {
            return group;
        }

        public List<String> getSubgroups() {
            return subgroups;
        }

        public String getCourse() {
            return course;
        }

        public Group(String group, List<String> subgroups, String course) {
            this.group = group;
            this.subgroups = subgroups;
            this.course = course;
        }
    }
    class ListGroupFromString{
        String stringGroups;

        ListGroupFromString( String stringGroups){
            this.stringGroups=stringGroups;
        }

        List<Group> create(){
            List<Group> returnList = new ArrayList<>();
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(stringGroups);
                int arrayLenght = jsonArray.length();
                for (int i = 0; i <arrayLenght ; i++) {
                    String nam_gruppa = jsonArray.getJSONObject(i).getString("nam_gruppa");
                    String subgroup = jsonArray.getJSONObject(i).getString("subgroup");
                    //если группа уже есть, то добавляеям к ней подгруппу
                    if (containInList(returnList,nam_gruppa)){
                        //если подгруппа новая для данной группы то добавляем ее к группе
                        if (!returnList.get(getIndexGroup(returnList,nam_gruppa)).subgroups.contains(subgroup)) {
                            returnList.get(getIndexGroup(returnList,nam_gruppa)).subgroups.add(subgroup);
                        }
                    }
                    //если данная группа новая,то добавляем ее в список всех групп вместе с подгруппой
                    else {
                        List<String> subList = new ArrayList<>();
                        if (!subgroup.equals("")) subList.add(subgroup);
                        returnList.add(new Group(nam_gruppa,subList,nam_gruppa.substring(0,1)));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return returnList;
        }

        boolean containInList(List<Group> list, String group ){
            if (list.size()==0) return false;
            for (int i = 0; i <list.size() ; i++) {
                if (list.get(i).group.equals(group)) return true;
            }
            return false;
        }

        int getIndexGroup(List<Group> list, String group){
            if (list.size()==0) return -1;
            for (int i = 0; i <list.size() ; i++) {
                if (list.get(i).group.equals(group)) return i;
            }
            return -1;
        }
    }
}
