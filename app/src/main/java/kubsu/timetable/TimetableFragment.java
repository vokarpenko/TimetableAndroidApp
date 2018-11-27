package kubsu.timetable;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static kubsu.timetable.MainActivity.PREFS_FILE;
import static kubsu.timetable.MainActivity.PREF_GROUP;

public class TimetableFragment extends Fragment {
    public SharedPreferences settings;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable_fragment, container, false);
        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(inflater.getContext());
        rv.setLayoutManager(llm);
        settings = inflater.getContext().getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        String myGroup = settings.getString(PREF_GROUP,"");
        AsyncRequest asyncRequest =new AsyncRequest();
        asyncRequest.execute(myGroup);
        try{
            List<Day> days;
            days = asyncRequest.get();
            RVAdapter adapter = new RVAdapter(days);
            rv.setAdapter(adapter);
            //rv.smoothScrollToPosition(getDayWeekNumber()+1);
        }
        catch (Exception e){
            Log.i("mytag",e.toString());
        }
        return view;
    }
}
