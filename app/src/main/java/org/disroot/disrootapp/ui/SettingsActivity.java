package org.disroot.disrootapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.disroot.disrootapp.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences BtnPreference;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_about, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent goHome = new Intent( SettingsActivity.this, MainActivity.class );
            SettingsActivity.this.startActivity( goHome );
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setNavigationIcon( R.drawable.ic_arrow_back );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        ////buttons visiblility preference list
        BtnPreference = getSharedPreferences( "MailBtn", Context.MODE_PRIVATE );//mail
        BtnPreference = getSharedPreferences( "CloudBtn", Context.MODE_PRIVATE );//cloud
        BtnPreference = getSharedPreferences( "ForumBtn", Context.MODE_PRIVATE );//forum
        BtnPreference = getSharedPreferences( "ChatBtn", Context.MODE_PRIVATE );//chat
        BtnPreference = getSharedPreferences( "PadBtn", Context.MODE_PRIVATE );//pad
        BtnPreference = getSharedPreferences( "CalcBtn", Context.MODE_PRIVATE );//calc
        BtnPreference = getSharedPreferences( "BinBtn", Context.MODE_PRIVATE );//bin
        BtnPreference = getSharedPreferences( "UploadBtn", Context.MODE_PRIVATE );//upload
        BtnPreference = getSharedPreferences( "SearxBtn", Context.MODE_PRIVATE );//search
        BtnPreference = getSharedPreferences( "PollsBtn", Context.MODE_PRIVATE );//polls

        //checkboxlist
        checkPrefBox();
    }

    //Visibility preference functions
    public void checkPrefBox() {
        //define checkBoxes
        View iconSettings =findViewById( R.id.iconSettings );
        final CheckBox checkMailBtn = iconSettings.findViewById( R.id.mailBtnPreference );//mail
        final CheckBox checkCloudBtn = iconSettings.findViewById( R.id.cloudBtnPreference );//cloud
        final CheckBox checkForumBtn = iconSettings.findViewById( R.id.foumBtnPreference );//forum
        final CheckBox checkChatBtn = iconSettings.findViewById( R.id.chatBtnPreference );//chat
        final CheckBox checkPadBtn = iconSettings.findViewById( R.id.padBtnPreference );//pad
        final CheckBox checkCalcBtn = iconSettings.findViewById( R.id.calcBtnPreference );//calc
        final CheckBox checkBinBtn = iconSettings.findViewById( R.id.binBtnPreference );//bin
        final CheckBox checkUploadBtn = iconSettings.findViewById( R.id.uploadBtnPreference );//upload
        final CheckBox checkSearxBtn = iconSettings.findViewById( R.id.searxBtnPreference );//search
        final CheckBox checkpollsBtn = iconSettings.findViewById( R.id.pollsBtnPreference );//polls

        //Set checked if visibility is true
        if (BtnPreference.getBoolean( "MailBtn", true )) checkMailBtn.setChecked( true );//mail
        if (BtnPreference.getBoolean( "CloudBtn", true )) checkCloudBtn.setChecked( true );//cloud
        if (BtnPreference.getBoolean( "ForumBtn", true )) checkForumBtn.setChecked( true );//forum
        if (BtnPreference.getBoolean( "ChatBtn", true )) checkChatBtn.setChecked( true );//chat
        if (BtnPreference.getBoolean( "PadBtn", true )) checkPadBtn.setChecked( true );//pad
        if (BtnPreference.getBoolean( "CalcBtn", true )) checkCalcBtn.setChecked( true );//calc
        if (BtnPreference.getBoolean( "BinBtn", true )) checkBinBtn.setChecked( true );//bin
        if (BtnPreference.getBoolean( "UploadBtn", true )) checkUploadBtn.setChecked( true );//upload
        if (BtnPreference.getBoolean( "SearxBtn", true )) checkSearxBtn.setChecked( true );//search
        if (BtnPreference.getBoolean( "pollsBtn", true )) checkSearxBtn.setChecked( true );//polls

        //Mail
        checkMailBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkMailBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "MailBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "MailBtn", false ).apply();
                }
            }
        } );

        //Cloud
        checkCloudBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkCloudBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "CloudBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "CloudBtn", false ).apply();
                }
            }
        } );

        //forum
        checkForumBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkForumBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "ForumBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "ForumBtn", false ).apply();
                }
            }
        } );

        //chat
        checkChatBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkChatBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "ChatBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "ChatBtn", false ).apply();
                }
            }
        } );

        //pad
        checkPadBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkPadBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "PadBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "PadBtn", false ).apply();
                }
            }
        } );

        //calc
        checkCalcBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkCalcBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "CalcBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "CalcBtn", false ).apply();
                }
            }
        } );

        //bin
        checkBinBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkBinBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "BinBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "BinBtn", false ).apply();
                }
            }
        } );

        //upload
        checkUploadBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkUploadBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "UploadBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "UploadBtn", false ).apply();
                }
            }
        } );

        //search
        checkSearxBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkSearxBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "SearxBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "SearxBtn", false ).apply();
                }
            }
        } );

        //polls
        checkpollsBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkpollsBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "PollsBtn", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "pollsBtn", false ).apply();
                }
            }
        } );

    }

    @Override //make sure changes are applyed when going back
    public void onBackPressed() {
        Intent goHome = new Intent( SettingsActivity.this, MainActivity.class );
        SettingsActivity.this.startActivity( goHome );
    }
}
