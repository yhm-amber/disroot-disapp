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
        BtnPreference = getSharedPreferences( "mailBtnVisibility", Context.MODE_PRIVATE );//mail
        BtnPreference = getSharedPreferences( "cloudBtnVisibility", Context.MODE_PRIVATE );//cloud
        BtnPreference = getSharedPreferences( "forumBtnVisibility", Context.MODE_PRIVATE );//forum
        BtnPreference = getSharedPreferences( "chatBtnVisibility", Context.MODE_PRIVATE );//chat

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

        //Set checked if visibility is true
        if (BtnPreference.getBoolean( "mailBtnVisibility", true )) checkMailBtn.setChecked( true );//mail
        if (BtnPreference.getBoolean( "cloudBtnVisibility", true )) checkCloudBtn.setChecked( true );//cloud
        if (BtnPreference.getBoolean( "forumBtnVisibility", true )) checkForumBtn.setChecked( true );//forum
        if (BtnPreference.getBoolean( "chatBtnVisibility", true )) checkChatBtn.setChecked( true );//chat

        //Mail
        checkMailBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkMailBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "mailBtnVisibility", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "mailBtnVisibility", false ).apply();
                }
            }
        } );

        //Cloud
        checkCloudBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkCloudBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "cloudBtnVisibility", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "cloudBtnVisibility", false ).apply();
                }
            }
        } );

        //forum
        checkForumBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkForumBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "forumBtnVisibility", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "forumBtnVisibility", false ).apply();
                }
            }
        } );

        //chat
        checkChatBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (checkChatBtn.isChecked()) {
                    BtnPreference.edit().putBoolean( "chatBtnVisibility", true ).apply();
                } else {
                    BtnPreference.edit().putBoolean( "chatBtnVisibility", false ).apply();
                }
            }
        } );


    }

    @Override
    public void onBackPressed() {
        Intent goHome = new Intent( SettingsActivity.this, MainActivity.class );
        SettingsActivity.this.startActivity( goHome );
    }
}
