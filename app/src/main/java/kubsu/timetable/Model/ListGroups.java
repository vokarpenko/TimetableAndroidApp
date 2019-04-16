package kubsu.timetable.Model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import kubsu.timetable.Model.Group;

public class ListGroups {

    public List<Group> createFromString(String stringGroups){
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
                    if (!returnList.get(getIndexGroup(returnList,nam_gruppa)).getSubgroups().contains(subgroup)) {
                        returnList.get(getIndexGroup(returnList,nam_gruppa)).getSubgroups().add(subgroup);
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

    private boolean containInList(List<Group> list, String group){
        if (list.size()==0) return false;
        for (int i = 0; i <list.size() ; i++) {
            if (list.get(i).getGroup().equals(group)) return true;
        }
        return false;
    }

    private int getIndexGroup(List<Group> list, String group){
        if (list.size()==0) return -1;
        for (int i = 0; i <list.size() ; i++) {
            if (list.get(i).getGroup().equals(group)) return i;
        }
        return -1;
    }
}
