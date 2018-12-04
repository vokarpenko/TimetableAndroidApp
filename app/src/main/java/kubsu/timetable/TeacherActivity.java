package kubsu.timetable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TeacherActivity extends AppCompatActivity {
    private static final int NOTIFY_ID = 101;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    /*
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
           if (id == R.id.action_settings) {
               return true;
           }

           return super.onOptionsItemSelected(item);
       }
        */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
                    Button getNotifButton = rootView.findViewById(R.id.get_notification);
                    getNotifButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent notificationIntent = new Intent(getContext(), MainActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                                    0, notificationIntent,
                                    PendingIntent.FLAG_CANCEL_CURRENT);

                            Resources res = getContext().getResources();

                            // до версии Android 8.0 API 26
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),new String());

                            builder.setContentIntent(contentIntent)
                                    // обязательные настройки
                                    .setSmallIcon(R.drawable.ic_info_black_24dp)
                                    //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                                    .setContentTitle("Уведомление")
                                    //.setContentText(res.getString(R.string.notifytext))
                                    .setContentText("Вы получили уведомление") // Текст уведомления
                                    // необязательные настройки
                                    .setAutoCancel(true); // автоматически закрыть уведомление после нажатия
                            Notification notification = builder.build();
                            //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            // Альтернативный вариант
                             NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                            notificationManager.notify(NOTIFY_ID, notification);
                        }
                    });

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
                    try {
                       AsyncGetDepartment asyncGetDepartment= new AsyncGetDepartment();
                       asyncGetDepartment.execute();
                       String[] listDepartment = asyncGetDepartment.get();
                        textDepartment.setAdapter(new  ArrayAdapter<>(
                                getContext(), android.R.layout.simple_dropdown_item_1line, listDepartment));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    buttonSendWish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!textLastName.getText().toString().equals("")&&
                                    !textWish.getText().toString().equals("")&&
                                    !textName.getText().toString().equals("")&&
                                    !textFatherName.getText().toString().equals("")&&
                                    !textPhonenumber.getText().toString().equals("")&&
                                    !textDepartment.getText().toString().equals(""))
                            {
                            String path = "http://timetable-fktpm.ru/index.php?option=mWish&lastname="+textLastName.getText()+"&firstname="+textName.getText()+
                                    "&fathername="+textFatherName.getText()+"&phonenumber="+textPhonenumber.getText()+"&wish="+textWish.getText()+
                                    "&department="+textDepartment.getText().toString();
                            new AsyncPostWish().execute(path);
                                Toast.makeText(getContext(),"Пожелания успешно отправлены",Toast.LENGTH_SHORT).show();}
                            else {
                                Toast.makeText(getContext(),"Заполните все поля",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return rootView;
                    default:return null;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
    public static class AsyncPostWish extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... path) {
            try {
                Jsoup.connect(path[0]).get();
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
                org.jsoup.nodes.Document doc = Jsoup.connect("http://timetable-fktpm.ru/index.php?option=mDepartment").get();
                String json = doc.text();
                JSONArray arr = new JSONArray(json);
                String[] departments = new String[arr.length()];
                for(int i = 0; i < arr.length(); i++){
                    departments[i]=(arr.getJSONObject(i).getString("nam_kafedra"));
                }
                return departments;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
