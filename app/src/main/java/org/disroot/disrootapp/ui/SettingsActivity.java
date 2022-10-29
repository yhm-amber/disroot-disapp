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
        toolbar.setNavigationOnClickListener( v -> onBackPressed() );

        ////buttons visibility preference list
        BtnPreference = getSharedPreferences( "MailBtn", Context.MODE_PRIVATE );//mail
        BtnPreference = getSharedPreferences( "CloudBtn", Context.MODE_PRIVATE );//cloud
        BtnPreference = getSharedPreferences( "ForumBtn", Context.MODE_PRIVATE );//forum
        BtnPreference = getSharedPreferences( "ChatBtn", Context.MODE_PRIVATE );//chat
        BtnPreference = getSharedPreferences( "PadBtn", Context.MODE_PRIVATE );//pad
        BtnPreference = getSharedPreferences( "CryptpadBtn", Context.MODE_PRIVATE );//Cryptpad
        BtnPreference = getSharedPreferences( "BinBtn", Context.MODE_PRIVATE );//bin
        BtnPreference = getSharedPreferences( "UploadBtn", Context.MODE_PRIVATE );//upload
        BtnPreference = getSharedPreferences( "SearxBtn", Context.MODE_PRIVATE );//searx
        BtnPreference = getSharedPreferences( "BoardBtn", Context.MODE_PRIVATE );//Board
        BtnPreference = getSharedPreferences( "CallsBtn", Context.MODE_PRIVATE );//calls
        BtnPreference = getSharedPreferences( "NotesBtn", Context.MODE_PRIVATE );//notes
        BtnPreference = getSharedPreferences( "GitBtn", Context.MODE_PRIVATE );//git
        BtnPreference = getSharedPreferences( "UserBtn", Context.MODE_PRIVATE );//user
        BtnPreference = getSharedPreferences( "HowToBtn", Context.MODE_PRIVATE );//howTo
        BtnPreference = getSharedPreferences( "AboutBtn", Context.MODE_PRIVATE );//about

        //checkbox list
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
        final CheckBox checkCryptpadBtn = iconSettings.findViewById( R.id.cryptpadBtnPreference );//cryptpad
        final CheckBox checkBinBtn = iconSettings.findViewById( R.id.binBtnPreference );//bin
        final CheckBox checkUploadBtn = iconSettings.findViewById( R.id.uploadBtnPreference );//upload
        final CheckBox checkSearxBtn = iconSettings.findViewById( R.id.searxBtnPreference );//search
        final CheckBox checkBoardBtn = iconSettings.findViewById( R.id.boardBtnPreference );//board
        final CheckBox checkCallsBtn = iconSettings.findViewById( R.id.callsBtnPreference );//calls
        final CheckBox checkNotesBtn = iconSettings.findViewById( R.id.notesBtnPreference );//notes
        final CheckBox checkGitBtn = iconSettings.findViewById( R.id.gitBtnPreference );//git
        final CheckBox checkUserBtn = iconSettings.findViewById( R.id.userBtnPreference );//user
        final CheckBox checkHowToBtn = iconSettings.findViewById( R.id.howToBtnPreference );//howTo
        final CheckBox checkAboutBtn = iconSettings.findViewById( R.id.aboutBtnPreference );//about

        //Set checked if visibility is true
        if (BtnPreference.getBoolean( "MailBtn", true )) checkMailBtn.setChecked( true );//mail
        if (BtnPreference.getBoolean( "CloudBtn", true )) checkCloudBtn.setChecked( true );//cloud
        if (BtnPreference.getBoolean( "ForumBtn", true )) checkForumBtn.setChecked( true );//forum
        if (BtnPreference.getBoolean( "ChatBtn", true )) checkChatBtn.setChecked( true );//chat
        if (BtnPreference.getBoolean( "PadBtn", true )) checkPadBtn.setChecked( true );//pad
        if (BtnPreference.getBoolean( "CryptpadBtn", true )) checkCryptpadBtn.setChecked( true );//cryptpad
        if (BtnPreference.getBoolean( "BinBtn", true )) checkBinBtn.setChecked( true );//bin
        if (BtnPreference.getBoolean( "UploadBtn", true )) checkUploadBtn.setChecked( true );//upload
        if (BtnPreference.getBoolean( "SearxBtn", true )) checkSearxBtn.setChecked( true );//search
        if (BtnPreference.getBoolean( "BoardBtn", true )) checkBoardBtn.setChecked( true );//board
        if (BtnPreference.getBoolean( "CallsBtn", true )) checkCallsBtn.setChecked( true );//calls
        if (BtnPreference.getBoolean( "NotesBtn", true )) checkNotesBtn.setChecked( true );//notes
        if (BtnPreference.getBoolean( "GitBtn", true )) checkGitBtn.setChecked( true );//git
        if (BtnPreference.getBoolean( "UserBtn", true )) checkUserBtn.setChecked( true );//user
        if (BtnPreference.getBoolean( "HowToBtn", true )) checkHowToBtn.setChecked( true );//howTo
        if (BtnPreference.getBoolean( "AboutBtn", true )) checkAboutBtn.setChecked( true );//about

        //Mail
        checkMailBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkMailBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "MailBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "MailBtn", false ).apply();
            }
        } );

        //Cloud
        checkCloudBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkCloudBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "CloudBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "CloudBtn", false ).apply();
            }
        } );

        //forum
        checkForumBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkForumBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "ForumBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "ForumBtn", false ).apply();
            }
        } );

        //chat
        checkChatBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkChatBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "ChatBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "ChatBtn", false ).apply();
            }
        } );

        //pad
        checkPadBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkPadBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "PadBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "PadBtn", false ).apply();
            }
        } );

        //cryptpad
        checkCryptpadBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkCryptpadBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "CryptpadBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "CryptpadBtn", false ).apply();
            }
        } );

        //bin
        checkBinBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkBinBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "BinBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "BinBtn", false ).apply();
            }
        } );

        //upload
        checkUploadBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkUploadBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "UploadBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "UploadBtn", false ).apply();
            }
        } );

        //search
        checkSearxBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkSearxBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "SearxBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "SearxBtn", false ).apply();
            }
        } );

        //board
        checkBoardBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkBoardBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "BoardBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "BoardBtn", false ).apply();
            }
        } );

        //board
        checkCallsBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkCallsBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "CallsBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "CallsBtn", false ).apply();
            }
        } );

        //notes
        checkNotesBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkNotesBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "NotesBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "NotesBtn", false ).apply();
            }
        } );

        //git
        checkGitBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkGitBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "GitBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "GitBtn", false ).apply();
            }
        } );

        //user
        checkUserBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkUserBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "UserBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "UserBtn", false ).apply();
            }
        } );

        //howTo
        checkHowToBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkHowToBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "HowToBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "HowToBtn", false ).apply();
            }
        } );

        //about
        checkAboutBtn.setOnCheckedChangeListener( (view, isChecked) -> {
            if (checkAboutBtn.isChecked()) {
                BtnPreference.edit().putBoolean( "AboutBtn", true ).apply();
            } else {
                BtnPreference.edit().putBoolean( "AboutBtn", false ).apply();
            }
        } );
    }

    @Override //make sure changes are applied when going back
    public void onBackPressed() {
        Intent goHome = new Intent( SettingsActivity.this, MainActivity.class );
        SettingsActivity.this.startActivity( goHome );
    }
}
