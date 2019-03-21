package kubsu.timetable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.Arrays;

import static kubsu.timetable.StartActivity.HAS_VISITED;
import static kubsu.timetable.StudentActivity.PREFS_FILE;
import static kubsu.timetable.StudentActivity.PREF_TEACHER;

public class TeacherActivity extends AppCompatActivity {
    public static String[] listDepartment;
    private SharedPreferences settings;
    private String teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFS_FILE,MODE_PRIVATE);
        teacher = settings.getString(PREF_TEACHER,"");

        Log.i("mytag",teacher);
        setContentView(R.layout.activity_teacher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(teacher);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           // Inflate the menu; this adds items to the action bar if it is present.
           getMenuInflater().inflate(R.menu.menu_teacher, menu);
           return true;
       }


       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           // Handle action bar item clicks here. The action bar will
           // automatically handle clicks on the Home/Up button, so long
           // as you specify a parent activity in AndroidManifest.xml.
           int id = item.getItemId();

           //noinspection SimplifiableIfStatement
           if (id == R.id.action_start_activity) {
               settings.edit().putInt(HAS_VISITED,0).apply();
               startActivity(new Intent(this,StartActivity.class));
               finish();
               return true;
           }
           if(id==R.id.action_change_teacher){
               settings.edit().putString(PREF_TEACHER,"null").apply();
               startActivity(new Intent(this,ChangeTeacherActivity.class));
               finish();
               return true;
           }

           return super.onOptionsItemSelected(item);
       }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            assert getArguments() != null;
            int number = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (number){
                case 1:
                    View rootView = inflater.inflate(R.layout.fragment_teacher_timetable, container, false);
                    RecyclerView rv = rootView.findViewById(R.id.rv_teacher);
                    rv.setHasFixedSize(true);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    rv.setLayoutManager(llm);
                    RVTeacherAdapter adapter = new RVTeacherAdapter();
                    rv.setAdapter(adapter);
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_teacher_wish, container, false);
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
                    default:return null;
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    //отправка пожеланий преподователя
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
    //получение списка кафедр
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
