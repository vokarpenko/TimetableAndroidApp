package kubsu.timetable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import kubsu.timetable.Fragment.NewsFragment;
import kubsu.timetable.R;
import kubsu.timetable.Fragment.TimetableFragment;
import kubsu.timetable.Utility.Internet;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String PREFS_FILE = "Setting";
    public static final String PREF_GROUP = "group";
    public static final String PREF_SUBGROUP = "subGroup";
    public static final String PREF_OLD_GROUP = "oldGroup";
    public static final String PREF_OLD_SUBGROUP = "oldSubGroup";
    public static final String PREF_TEACHER = "teacher";
    public SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        String group = settings.getString(PREF_GROUP,"null");
        String subGroup = settings.getString(PREF_SUBGROUP,"null");
        if(group.equals("null")) {
            startActivity(new Intent(this,ChangeGroupActivity.class));
            finish();
        }
            setContentView(R.layout.activity_student);
        //установка тулбара
        Toolbar toolbar = findViewById(R.id.toolbar_student);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //уставнока группы
        View view = navigationView.getHeaderView(0);
        TextView headerGroup = view.findViewById(R.id.header_group);
        String textHeader = "Группа "+group+" "+subGroup;
        headerGroup.setText(textHeader);

        //Установка фрагмента расписание
        Class fragmentClass = TimetableFragment.class;
        Fragment fragment=null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.i("mytag",e.toString());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (fragment != null) {
                fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
                setTitle("Расписание");
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.timetable_item:
                setFragment(TimetableFragment.class);
                setTitle(item.getTitle());
                break;
            case R.id.news_item:
                if(new Internet(getApplicationContext()).isConnection()) {
                    setTitle(item.getTitle());
                    setFragment(NewsFragment.class);
                }
                else Toast.makeText(getApplicationContext(),"Нет соединения с интернетом",Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_group_item:
                if(new Internet(getApplicationContext()).isConnection()) {
                    Intent changeGroupIntent = new Intent(this, ChangeGroupActivity.class);
                    changeGroupIntent.putExtra("MyGroup", settings.getString(PREF_GROUP, ""));
                    startActivity(changeGroupIntent);
                }
                else Toast.makeText(getApplicationContext(),"Нет соединения с интернетом",Toast.LENGTH_SHORT).show();
                break;
            case R.id.start_activity_item:
                settings.edit().putInt("hasVisited",0).apply();
                startActivity(new Intent(this,StartActivity.class));
                finish();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.i("mytag", e.toString());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (fragment != null) {
                fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
            }
        }
    }
}
