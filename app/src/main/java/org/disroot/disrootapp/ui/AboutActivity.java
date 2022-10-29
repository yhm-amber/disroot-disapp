package org.disroot.disrootapp.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import org.disroot.disrootapp.utils.Contributors;

import static org.disroot.disrootapp.BuildConfig.APPLICATION_ID;
import static org.disroot.disrootapp.BuildConfig.VERSION_CODE;
import static org.disroot.disrootapp.BuildConfig.VERSION_NAME;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener( v -> onBackPressed() );
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
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager() );

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
        fab.setOnClickListener( view -> {
            Uri uri = Uri.parse(Constants.URL_SUPPORT);
            Intent support = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
            startActivity(support);
        } );
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
                    ContributeBtn.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_SOURCE);
                        Intent code = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(code);
                    } );

                    final Button TranslateBtn = rootView.findViewById(R.id.TranslateBtn);//TranslateBtn
                    TranslateBtn.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_TRANSLATE);
                        Intent translate = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(translate);
                    } );

                    final Button FeedbackBtn1;//FeedbackBtn1
                    FeedbackBtn1 = rootView.findViewById(R.id.FeedbackBtn1);
                    FeedbackBtn1.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_BUGS);
                        Intent feedback1 = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(feedback1);
                    } );

                    final Button FeedbackBtn2 = rootView.findViewById(R.id.FeedbackBtn2);//FeedbackBtn2
                    FeedbackBtn2.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_XMPP);
                        Intent feedback2 = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(feedback2);
                    } );
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_about_about, container, false);
                    final ImageButton fDroidBtn = rootView.findViewById(R.id.fDroidBtn);//fDroidBtn
                    fDroidBtn.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_FDROID);
                        Intent fDroid = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(fDroid);
                    } );
                    final ImageButton homeBtn;//DisrootBtn
                    homeBtn = rootView.findViewById(R.id.homeBtn);
                    homeBtn.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_DISROOT);
                        Intent home = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(home);
                    } );
                    final TextView PackageName= rootView.findViewById(R.id.PackageName);
                    PackageName.setText(getString( R.string.id ) + " "+ APPLICATION_ID);

                    final TextView AppVersion= rootView.findViewById(R.id.AppVersion);
                    AppVersion.setText(getString( R.string.version ) + " " + VERSION_NAME + "(" + VERSION_CODE + ")");

                    final TextView AndroidVersion= rootView.findViewById(R.id.AndroidVersion);
                    AndroidVersion.setText(getString( R.string.androidVersion ) + " " + Build.VERSION.RELEASE);

                    final TextView Device;
                    Device = rootView.findViewById(R.id.Device);
                    Device.setText(getString( R.string.deviceName ) + " " + Build.MANUFACTURER + Build.MODEL);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_about_license, container, false);
                    //Contributors
                    //Admins
                    final TextView contributors_text;
                    contributors_text = rootView.findViewById(R.id.admins_text);
                    contributors_text.setMovementMethod( LinkMovementMethod.getInstance());
                    String admins="";
                    for(String admin: Contributors.admins) {
                        admins+="&bull; "+admin+"</a><br>";
                        contributors_text.setText( Html.fromHtml(admins));
                    }
                    //devs
                    final TextView devs_text;
                    devs_text = rootView.findViewById(R.id.devs_text);
                    devs_text.setMovementMethod( LinkMovementMethod.getInstance());
                    String devs="";
                    for(String dev: Contributors.devs) {
                        devs+="&bull; "+dev+"</a><br>";
                        devs_text.setText( Html.fromHtml(devs));
                    }
                    //translators
                    final TextView translators_text;
                    translators_text = rootView.findViewById(R.id.translators_text);
                    translators_text.setMovementMethod( LinkMovementMethod.getInstance());
                    String translators="";
                    for(String translator: Contributors.translators) {
                        translators+="&bull; "+translator+"</a><br>";
                        translators_text.setText( Html.fromHtml(translators));
                    }
                    //artworks
                    final TextView artworks_text;
                    artworks_text = rootView.findViewById(R.id.artworks_text);
                    artworks_text.setMovementMethod( LinkMovementMethod.getInstance());
                    String artworks="";
                    for(String artwork: Contributors.artworks) {
                        artworks+="&bull; "+artwork+"</a><br>";
                        artworks_text.setText( Html.fromHtml(artworks));
                    }

                    //Buttons
                    final Button licenseBtn;//LicenseBtn
                    licenseBtn = rootView.findViewById(R.id.license_button);
                    licenseBtn.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_LICENSE);
                        Intent license = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(license);
                    } );
                    final TextView disrootBtn;//DisrootBtn
                    disrootBtn = rootView.findViewById(R.id.disrootUrl);
                    disrootBtn.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_DISROOT);
                        Intent disroot = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(disroot);
                    } );
                    final TextView dioBtn;//DioBtn
                    dioBtn = rootView.findViewById(R.id.dioBtn);
                    dioBtn.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse(Constants.URL_DIO);
                        Intent dio = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(dio);
                    } );
                    final TextView htmlToTextView = rootView.findViewById(R.id.ThirdParty);// textview links clickable
                    htmlToTextView.setMovementMethod( LinkMovementMethod.getInstance());// textview links clickable
                    break;
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

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
