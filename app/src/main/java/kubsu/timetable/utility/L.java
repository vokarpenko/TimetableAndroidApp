package kubsu.timetable.utility;

import android.util.Log;

public class L {
    public static void log(CharSequence a){
        Log.i("mytag",String.valueOf(a));
    }
    public static void log(int a){
        Log.i("mytag",String.valueOf(a));
    }
    public static void log(String a){
        Log.i("mytag",a);
    }
    public static void log(long a){
        Log.i("mytag",String.valueOf(a));
    }
}
