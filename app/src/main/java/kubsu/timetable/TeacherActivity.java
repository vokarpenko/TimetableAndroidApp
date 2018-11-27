package kubsu.timetable;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherActivity extends AppCompatActivity {

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
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_teacher_wish, container, false);
                    final Button buttonSendWish= rootView.findViewById(R.id.button_send_wish);
                    final TextView textFamilia = rootView.findViewById(R.id.text_familia);
                    final TextView textName = rootView.findViewById(R.id.text_name);
                    final TextView textOtchestvo = rootView.findViewById(R.id.text_otchestvo);
                    final TextView textPhonenumber = rootView.findViewById(R.id.text_phonenumber);
                    final TextView textWish = rootView.findViewById(R.id.text_wish);
                    buttonSendWish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!textFamilia.getText().toString().equals("")&&
                                    !textWish.getText().toString().equals("")&&
                                    !textName.getText().toString().equals("")&&
                                    !textOtchestvo.getText().toString().equals("")&&
                                    !textPhonenumber.getText().toString().equals("")){
                            String wish = textFamilia.getText()+" "+textName.getText()+" "+textOtchestvo.getText()+" "+textPhonenumber.getText()+" "+textWish.getText();
                            Log.i("mytag",wish);
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
}
