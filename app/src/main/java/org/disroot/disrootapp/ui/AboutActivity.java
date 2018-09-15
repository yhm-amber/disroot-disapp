package org.disroot.disrootapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.disroot.disrootapp.R;
import org.disroot.disrootapp.utils.Constants;

import static org.disroot.disrootapp.BuildConfig.*;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_SUPPORT));
                Intent support = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(support);
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent goHome = new Intent(AboutActivity.this, MainActivity.class);
            AboutActivity.this.startActivity(goHome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
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
        @SuppressLint("SetTextI18n")
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            assert getArguments() != null;
            View rootView;
            rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){

                case 1:
                    rootView = inflater.inflate(R.layout.fragment_about_help, container, false);
                    //Buttons


                    final Button ContributeBtn = rootView.findViewById(R.id.ContributeBtn);//ContributeBtn
                    ContributeBtn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_SOURCE));
                            Intent code = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(code);
                        }
                    });

                    final Button TranslateBtn = rootView.findViewById(R.id.TranslateBtn);//TranslateBtn
                    TranslateBtn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_TRANSLATE));
                            Intent translate = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(translate);
                        }
                    });

                    final Button FeedbackBtn1;//FeedbackBtn1
                    FeedbackBtn1 = rootView.findViewById(R.id.FeedbackBtn1);
                    FeedbackBtn1.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_BUGS));
                            Intent feedback1 = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(feedback1);
                        }
                    });

                    final Button FeedbackBtn2 = rootView.findViewById(R.id.FeedbackBtn2);//FeedbackBtn2
                    FeedbackBtn2.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_XMPP));
                            Intent feedback2 = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(feedback2);
                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_about_about, container, false);
                    final ImageButton fDroidBtn = rootView.findViewById(R.id.fDroidBtn);//fDroidBtn
                    fDroidBtn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_FDROID));
                            Intent fDroid = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(fDroid);
                        }
                    });
                    final ImageButton homeBtn;//DisrootBtn
                    homeBtn = rootView.findViewById(R.id.homeBtn);
                    homeBtn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_DISROOT));
                            Intent home = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(home);
                        }
                    });
                    final TextView PackageName= rootView.findViewById(R.id.PackageName);
                    PackageName.setText("ID: " + APPLICATION_ID);

                    final TextView AppVersion= rootView.findViewById(R.id.AppVersion);
                    AppVersion.setText("Version: " + VERSION_NAME + "(" + VERSION_CODE + ")");

                    final TextView AndroidVersion= rootView.findViewById(R.id.AndroidVersion);
                    AndroidVersion.setText("Android version: " + Build.VERSION.RELEASE);

                    final TextView Device;
                    Device = rootView.findViewById(R.id.Device);
                    Device.setText("Device name: " + Build.MANUFACTURER + Build.MODEL);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_about_license, container, false);
                    //Buttons
                    final Button licenseBtn;//LicenseBtn
                    licenseBtn = rootView.findViewById(R.id.license_button);
                    licenseBtn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_LICENSE));
                            Intent license = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(license);
                        }
                    });
                    final TextView disrootBtn;//DisrootBtn
                    disrootBtn = rootView.findViewById(R.id.disrootUrl);
                    disrootBtn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_DISROOT));
                            Intent disroot = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(disroot);
                        }
                    });
                    final TextView dioBtn;//DiotBtn
                    dioBtn = rootView.findViewById(R.id.dioBtn);
                    dioBtn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View arg0) {
                            Uri uri = Uri.parse(String.valueOf(Constants.URL_DIO));
                            Intent dio = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            startActivity(dio);
                        }
                    });
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        final FragmentManager fm;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
