package kubsu.timetable.student;

import android.content.Intent;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import kubsu.timetable.student.changeGroup.ChangeGroupFragment;
import kubsu.timetable.news.NewsFragment;
import kubsu.timetable.timetable.TimetableFragment;
import kubsu.timetable.R;
import kubsu.timetable.start.StartActivity;

public class StudentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,StudentView {
    private StudentPresenter presenter;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TextView headerText;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        init();
        presenter.checkData();
    }

    private void init() {
        if(presenter==null){
            presenter= new StudentPresenter(this,new StudentRepository(getApplicationContext()));
        }
        initProgressBar();
        initNavigationView();
        initTittle();
    }

    private void initProgressBar() {
        progressBar = new ProgressBar(this);
    }

    private void initTittle() {
        View view = navigationView.getHeaderView(0);
        headerText = view.findViewById(R.id.header_group);
        presenter.setHeader();
    }

    private void initNavigationView(){
        Toolbar toolbar = findViewById(R.id.toolbar_student);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                presenter.setFragment(TimetableFragment.class,false);
                presenter.setTittle(item.getTitle().toString());
                break;
            case R.id.news_item:
                presenter.setTittle(item.getTitle().toString());
                presenter.setFragment(NewsFragment.class,true);
                break;
            case R.id.change_group_item:
                presenter.setFragment(ChangeGroupFragment.class,false);
                presenter.setTittle(item.getTitle().toString());
                break;
            case R.id.start_activity_item:
                presenter.logOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTittle(String tittle) {
        setTitle(tittle);
    }

    @Override
    public void setFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null && !isDestroyed()) {
            fragmentManager.beginTransaction().replace(R.id.content_container, fragment).commit();
        }
    }

    @Override
    public void setHeader(String text) {
        headerText.setText(text);
    }

    @Override
    public void openStartActivity() {
        startActivity(new Intent(this,StartActivity.class));
        finish();
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
    }
}
