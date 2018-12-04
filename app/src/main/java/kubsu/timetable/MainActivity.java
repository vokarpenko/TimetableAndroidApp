package kubsu.timetable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String PREFS_FILE = "Setting";
    public static final String PREF_GROUP = "MyGroup";
    public SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        //уставнока группы
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(PREF_GROUP, "4kpm");
        prefEditor.apply();
        //установка тулбара
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.timetable_item:
                setFragment(TimetableFragment.class);
                setTitle(item.getTitle());
                break;
            case R.id.news_item:
                setTitle(item.getTitle());
                setFragment(NewsFragment.class);
                break;
            case R.id.setting_item:
               //startActivity(new Intent(this,SettingsActivity.class));
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
