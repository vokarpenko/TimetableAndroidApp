package kubsu.timetable.teacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.jsoup.Jsoup;

import java.io.IOException;

import kubsu.timetable.R;
import kubsu.timetable.news.NewsFragment;
import kubsu.timetable.start.StartActivity;
import kubsu.timetable.timetable.TimetableFragment;
import kubsu.timetable.teacher.wishes.WishesFragment;

import static kubsu.timetable.utility.Constant.EXTRA_TIMETABLE_TYPE;

public class TeacherActivity extends AppCompatActivity implements TeacherView {
    public static String[] listDepartment;
    private Toolbar toolbar;
    private TeacherPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        init();
        presenter.setData();
    }

    private void init() {
        if (presenter==null)
            presenter = new TeacherPresenter(this,new TeacherRepository(getApplicationContext()));
        initToolbar();
        initViewPager();
    }

    private void initViewPager() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.teacher_container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.teacher_tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    void initToolbar(){
        toolbar = findViewById(R.id.toolbar_teacher);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher, menu);
        return true;
    }


       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();
           if (id == R.id.action_teacher_logout) {
               presenter.logOut();
           }
           return super.onOptionsItemSelected(item);
       }

    @Override
    public void setTittle(String text) {
        toolbar.setTitle(text);
    }

    @Override
    public void openStartActivity() {
        startActivity(new Intent(this,StartActivity.class));
        finish();
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
    }



        /*@Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            assert getArguments() != null;
            int number = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (number){
                case 1:
                    View rootView = inflater.inflate(R.layout.fragment_teacher_timetable, container, false);
                    RecyclerView recyclerView = rootView.findViewById(R.id.rv_teacher);
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    TeacherTimetableAdapter adapter = new TeacherTimetableAdapter();
                    recyclerView.setAdapter(adapter);
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_wishes, container, false);
                    final Button buttonSendWish= rootView.findViewById(R.id.button_send_wish);
                    final TextView textLastName = rootView.findViewById(R.id.text_familia);
                    final TextView textName = rootView.findViewById(R.id.text_name);
                    final TextView textFatherName = rootView.findViewById(R.id.text_otchestvo);
                    final TextView textPhonenumber = rootView.findViewById(R.id.text_phonenumber);
                    PhoneNumberUtils.formatNumber(textPhonenumber.getText().toString());
                    final TextView textWish = rootView.findViewById(R.id.text_wish);
                    final AppCompatAutoCompleteTextView textDepartment = rootView.findViewById(R.id.text_department);//кафедра

                    try{
                        listDepartment = new AsyncGetDepartment().execute().get();
                }
                catch (Exception e){
                    Log.i("mytag",e.toString());
                }
                if (listDepartment!=null) {
                    textDepartment.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, listDepartment));
                }
                else {
                        Toast.makeText(getContext(), "Ошибка соединения с сервером", Toast.LENGTH_LONG).show();
                        buttonSendWish.setEnabled(false);
                    }

                    buttonSendWish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!textLastName.getText().toString().equals("") &&
                                    !textWish.getText().toString().equals("") &&
                                    !textName.getText().toString().equals("") &&
                                    !textFatherName.getText().toString().equals("") &&
                                    !textPhonenumber.getText().toString().equals("") &&
                                    !textDepartment.getText().toString().equals("")) {
                                if(Arrays.asList(listDepartment).contains(textDepartment.getText().toString())) {
                                        String path = "http://timetable-fktpm.ru/index.php?option=mWish&lastname=" + textLastName.getText() + "&firstname=" + textName.getText() +
                                                "&fathername=" + textFatherName.getText() + "&phonenumber=" + textPhonenumber.getText() + "&wish=" + textWish.getText() +
                                                "&department=" + textDepartment.getText().toString();
                                        try {
                                            new AsyncPostWish().execute(path);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(getContext(), "Пожелания успешно отправлены", Toast.LENGTH_LONG).show();
                                }
                            else Toast.makeText(getContext(), "Выберите кафедру из выпадающего списка", Toast.LENGTH_LONG).show();
                        }
                            else Toast.makeText(getContext(),"Заполните все поля",Toast.LENGTH_LONG).show();
                        }
                    });
                    return rootView;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_news, container, false);
                    *//*recyclerView = rootView.findViewById(R.id.rv_news);
                    linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    NewsAdapter newsAdapter = new newsAdapter();
                    recyclerView.setAdapter(newsAdapter);*//*
                    return rootView;
                    default:return null;
            }
        }*/


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Fragment timetableFragment = new TimetableFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt(EXTRA_TIMETABLE_TYPE,1);
                    timetableFragment.setArguments(arguments);
                    return timetableFragment;
                case 1:
                    return new NewsFragment();
                case 2:
                    return new WishesFragment();
                    default:return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class AsyncPostWish extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... path) {
            try {
                Jsoup.connect(path[0]).timeout(3000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class AsyncGetDepartment extends AsyncTask<Void,Void,String[]>{
        @Override
        protected String[] doInBackground(Void... voids) {
            try {
                org.jsoup.nodes.Document doc = Jsoup.connect("http://timetable-fktpm.ru/index.php?option=mDepartment").timeout(2000).get();
                String json = doc.text();
                JSONArray arr = new JSONArray(json);
                listDepartment = new String[arr.length()];
                for(int i = 0; i < arr.length(); i++){
                    listDepartment[i]=(arr.getJSONObject(i).getString("nam_kafedra"));
                }
                return listDepartment;
            }
            catch (Exception e){
                Log.i("mytag",e.toString());

                return null;
            }
        }
    }
}
