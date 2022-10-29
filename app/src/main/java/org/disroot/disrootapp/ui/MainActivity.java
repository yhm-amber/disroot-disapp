package org.disroot.disrootapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import org.disroot.disrootapp.R;
import org.disroot.disrootapp.StatusService;
import org.disroot.disrootapp.utils.Constants;
import org.disroot.disrootapp.utils.HttpHandler;
import org.disroot.disrootapp.webviews.DisWebChromeClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cketti.library.changelog.ChangeLog;

import static android.support.constraint.Constraints.TAG;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnLongClickListener,View.OnClickListener {

    SharedPreferences firstStart = null;//first start
    SharedPreferences check = null;
    SharedPreferences BtnPreference;
    WebChromeClient.FileChooserParams chooserParams;
    ValueCallback<Uri[]> chooserPathUri;
    Button button;
    private Button MailBtn,CloudBtn,ForumBtn,ChatBtn,PadBtn, CryptpadBtn,BinBtn,UploadBtn,SearxBtn,BoardBtn,CallsBtn,NotesBtn,GitBtn,UserBtn,StateBtn,HowToBtn,AboutBtn;//all buttons
    private String email,cloud,forum,etherpad,bin,upload,searx,taiga,user,xmpp,notes,git,cryptpad;
    private CookieManager cookieManager;
    private WebView webView;
    private DisWebChromeClient disWebChromeClient;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private Snackbar snackbarExitApp;
    private FragmentManager fm;
    private String mCameraPhotoPath;
    private String loadUrl;
    private int progressStatus = 0;
    ArrayList componentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayoutContainer = findViewById(R.id.framelayout_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon( R.drawable.ic_home );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView dashboard = findViewById(R.id.dashboard);
                TranslateAnimation animateup = new TranslateAnimation(0,0,-2*dashboard.getHeight(),0);
                TranslateAnimation animatedown = new TranslateAnimation(0,0,0,-dashboard.getHeight());
                if(webView.getVisibility()==View.VISIBLE){
                    //animation
                    animateup.setDuration(500);
                    animateup.setFillAfter(false);
                    dashboard.startAnimation(animateup);
                    dashboard.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    return;
                }
                if (webView.getVisibility()==View.GONE && webView.getUrl()!=null){
                    //animation
                    animatedown.setDuration(500);
                    animatedown.setFillAfter(false);
                    dashboard.startAnimation(animatedown);
                    hideDashboard();
                    return;
                }
                else
                    return;
            }
        } );
        componentList = new ArrayList<>();
        new GetList().execute();

        setupWebView(savedInstanceState, frameLayoutContainer);
        //settings
        firstStart = getSharedPreferences("org.disroot.disrootap", MODE_PRIVATE);//fisrt start
        check = getSharedPreferences("org.disroot.disrootapp", MODE_PRIVATE);
        //buttons visiblility preference
        BtnPreference = getSharedPreferences( "MailBtn", Context.MODE_PRIVATE );//mail
        BtnPreference = getSharedPreferences( "CloudBtn", Context.MODE_PRIVATE );//cloud
        BtnPreference = getSharedPreferences( "ForumBtn", Context.MODE_PRIVATE );//forum
        BtnPreference = getSharedPreferences( "ChatBtn", Context.MODE_PRIVATE );//chat
        BtnPreference = getSharedPreferences( "PadBtn", Context.MODE_PRIVATE );//pad
        BtnPreference = getSharedPreferences( "CryptpadBtn", Context.MODE_PRIVATE );//cryptpad
        BtnPreference = getSharedPreferences( "BinBtn", Context.MODE_PRIVATE );//bin
        BtnPreference = getSharedPreferences( "UploadBtn", Context.MODE_PRIVATE );//upload
        BtnPreference = getSharedPreferences( "SearxBtn", Context.MODE_PRIVATE );//search
        BtnPreference = getSharedPreferences( "BoardBtn", Context.MODE_PRIVATE );//board
        BtnPreference = getSharedPreferences( "CallsBtn", Context.MODE_PRIVATE );//calls
        BtnPreference = getSharedPreferences( "NotesBtn", Context.MODE_PRIVATE );//notes
        BtnPreference = getSharedPreferences( "GitBtn", Context.MODE_PRIVATE );//git
        BtnPreference = getSharedPreferences( "UserBtn", Context.MODE_PRIVATE );//user
        BtnPreference = getSharedPreferences( "HowToBtn", Context.MODE_PRIVATE );//howTo
        BtnPreference = getSharedPreferences( "AboutBtn", Context.MODE_PRIVATE );//about
        //Status service
        Intent intent = new Intent( MainActivity.this, StatusService.class);
        startService(intent);

        //progressbarLoading
        progressBar = findViewById(R.id.progressbarLoading);
        // Start long running operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //ckCangelog library
        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }

        //set booleans for checking Chat preference
        if (firstStart.getBoolean("firsttap", true)){
            check.edit().putBoolean("checkConv",false).apply();
            check.edit().putBoolean("checkPix",false).apply();
        }

        //pull to refresh
        final SwipeRefreshLayout swipe = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh(){
                swipe.setRefreshing( false );
                String url = webView.getUrl();
                webView.loadUrl(url);
            }
        });

        //Setup snackbar
        snackbarExitApp = Snackbar
                .make(findViewById(R.id.framelayout_container), R.string.do_you_want_to_exit, Snackbar.LENGTH_LONG)
                .setActionTextColor( Color.LTGRAY )
                .setAction(android.R.string.yes, new View.OnClickListener() {
                    public void onClick(View view) {
                        finish();
                        moveTaskToBack(true);
                    }
                });

        // Link the button in activity_main.xml
        MailBtn = findViewById( R.id.MailBtn );
        CloudBtn = findViewById( R.id.CloudBtn );
        //DiasporaBtn = findViewById( R.id.DiasporaBtn );//end of Disroot's Dandelion
        ForumBtn = findViewById( R.id.ForumBtn );
        ChatBtn = findViewById( R.id.ChatBtn );
        PadBtn = findViewById( R.id.PadBtn );
        CryptpadBtn = findViewById( R.id.CryptpadBtn );
        BinBtn = findViewById( R.id.BinBtn );
        UploadBtn = findViewById( R.id.UploadBtn );
        SearxBtn = findViewById( R.id.SearxBtn );
        BoardBtn = findViewById( R.id.BoardBtn );
        CallsBtn = findViewById( R.id.CallsBtn );
        NotesBtn = findViewById( R.id.NotesBtn );
        GitBtn = findViewById( R.id.GitBtn );
        UserBtn = findViewById( R.id.UserBtn );
        StateBtn = findViewById( R.id.StateBtn );
        HowToBtn = findViewById( R.id.HowToBtn );
        AboutBtn = findViewById( R.id.AboutBtn );

        Map<String, ?> allEntries = BtnPreference.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getValue().equals( false )){
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                for(int i=0; i<Constants.buttonIDs.length; i++) {
                    Button b = (Button) findViewById(Constants.buttonIDs[i]);
                    int resID = getResources().getIdentifier(entry.getKey(),
                            "id", getPackageName());
                    if(findViewById(resID)==b) {
                        viewGroup.removeView(b);
                    }
                }
            }
        }

        //get preferences


        //Set longclick buttons
        MailBtn.setOnLongClickListener( this );
        CloudBtn.setOnLongClickListener( this );
        //DiasporaBtn.setOnLongClickListener( this );//end of Disroot's Dandelion
        ForumBtn.setOnLongClickListener( this );
        ChatBtn.setOnLongClickListener( this );
        PadBtn.setOnLongClickListener( this );
        CryptpadBtn.setOnLongClickListener( this );
        BinBtn.setOnLongClickListener( this );
        UploadBtn.setOnLongClickListener( this );
        SearxBtn.setOnLongClickListener( this );
        BoardBtn.setOnLongClickListener( this );
        CallsBtn.setOnLongClickListener( this );
        NotesBtn.setOnLongClickListener( this );
        GitBtn.setOnLongClickListener( this );
        UserBtn.setOnLongClickListener( this );
        StateBtn.setOnLongClickListener( this );
        HowToBtn.setOnLongClickListener( this );
        AboutBtn.setOnLongClickListener( this );

        //set clickbuttons
        MailBtn.setOnClickListener( this );
        CloudBtn.setOnClickListener( this );
        //DiasporaBtn.setOnClickListener( this );//end of Disroot's Dandelion
        ForumBtn.setOnClickListener( this );
        ChatBtn.setOnClickListener( this );
        PadBtn.setOnClickListener( this );
        CryptpadBtn.setOnClickListener( this );
        BinBtn.setOnClickListener( this );
        UploadBtn.setOnClickListener( this );
        SearxBtn.setOnClickListener( this );
        BoardBtn.setOnClickListener( this );
        CallsBtn.setOnClickListener( this );
        NotesBtn.setOnClickListener( this );
        GitBtn.setOnClickListener( this );
        UserBtn.setOnClickListener( this );
        StateBtn.setOnClickListener( this );
        HowToBtn.setOnClickListener( this );
        AboutBtn.setOnClickListener( this );

        ImageButton imageButton = findViewById(R.id.logo);//LogoBtn
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showLogoInfo();
                return true;
            }
        });
    }

    @Override
    public void onClick(View view){
        if (firstStart.getBoolean("firsttap", true)){
            showFirstTap();
            firstStart.edit().putBoolean("firsttap", false).apply();
            return;
        }
        else {
            switch (view.getId()) {
                case R.id.MailBtn:
                    Intent mail = getPackageManager().getLaunchIntentForPackage( Constants.k9 );
                    if (mail == null) {
                        showMailDialog();
                        break;
                    } else startActivity(mail);
                    break;
                case R.id.CloudBtn:
                    Intent cloud = getPackageManager().getLaunchIntentForPackage(Constants.nc);
                    if(cloud == null) {
                        showCloudDialog();
                        break;
                    }
                    else startActivity(cloud);
                    break;
                /*case R.id.DiasporaBtn: //end ofDisroot's Dandelion
                        Intent pod = getPackageManager().getLaunchIntentForPackage(Diaspora);
                        if(getPackageManager().getLaunchIntentForPackage(Diaspora) == null) {
                            showDiaDialog();
                            break;
                        }
                        else startActivity(pod);
                    break;*/
                case R.id.ForumBtn:
                    webView.loadUrl(Constants.URL_DisApp_FORUM);
                    hideDashboard();
                    break;
                case R.id.ChatBtn:

                    Intent xmpp1 = getPackageManager().getLaunchIntentForPackage(Constants.Conversations);
                    Intent xmpp2 = getPackageManager().getLaunchIntentForPackage(Constants.PixArt);
                    if((xmpp1 == null)&&(xmpp2 == null)) {
                        showChatDialog();
                        break;
                    }
                    if((xmpp1 == null)&&(xmpp2 != null)) {
                        startActivity(xmpp2);
                        break;
                    }
                    if((xmpp1 != null)&&(xmpp2 != null)) {
                        if(check.getBoolean("checkConv", Boolean.parseBoolean(null))||check.getBoolean("checkConv", false)) {
                            startActivity(xmpp1);
                            break;
                        }
                        if(check.getBoolean("checkPix", Boolean.parseBoolean(null))||check.getBoolean("checkPix", false)) {
                            startActivity(xmpp2);
                            break;
                        }
                        else
                            showChoose();
                        break;
                    }
                    else
                        startActivity(xmpp1);
                    break;
                case R.id.PadBtn:
                    Intent pad = getPackageManager().getLaunchIntentForPackage(Constants.Padland);
                    if(pad == null) {
                        showPAdDialog();
                        break;
                    }
                    else startActivity(pad);
                    break;
                case R.id.CryptpadBtn:
                    webView.loadUrl(Constants.URL_DisApp_CRYPTPAD );
                    hideDashboard();
                    break;
                case R.id.BinBtn:
                    webView.loadUrl(Constants.URL_DisApp_BIN);
                    hideDashboard();
                    break;
                case R.id.UploadBtn:
                    webView.loadUrl(Constants.URL_DisApp_UPLOAD);
                    hideDashboard();
                    break;
                case R.id.SearxBtn:
                    webView.loadUrl(Constants.URL_DisApp_SEARX);
                    hideDashboard();
                    break;
                case R.id.BoardBtn:
                    webView.loadUrl(Constants.URL_DisApp_BOARD);
                    hideDashboard();
                    break;
                case R.id.CallsBtn:
                    Intent board = getPackageManager().getLaunchIntentForPackage(Constants.CallsApp);
                    if(board == null) {
                        showBoardDialog();
                        break;
                    }
                    else startActivity(board);
                    break;
                case R.id.NotesBtn:
                    Intent notes = getPackageManager().getLaunchIntentForPackage(Constants.NotesApp);
                    if(notes == null) {
                        showNotesDialog();
                        break;
                    }
                    else startActivity(notes);
                    break;
                case R.id.GitBtn:
                    Intent git = getPackageManager().getLaunchIntentForPackage(Constants.GitApp);
                    if(git == null) {
                        showGitDialog();
                        break;
                    }
                    else startActivity(git);
                    break;
                case R.id.UserBtn:
                    webView.loadUrl(Constants.URL_DisApp_USER);
                    hideDashboard();
                    break;
                case R.id.StateBtn:
                    Intent goState = new Intent(MainActivity.this, StateActivity.class);
                    MainActivity.this.startActivity(goState);
                    break;
                case R.id.HowToBtn:
                    webView.loadUrl(Constants.URL_DisApp_HOWTO);
                    hideDashboard();
                    break;
                case R.id.AboutBtn:
                    Intent goAbout = new Intent(MainActivity.this, AboutActivity.class);
                    MainActivity.this.startActivity(goAbout);
                    break;
                default:
                    break;
            }
        }
        return;
    }
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.MailBtn:
                showMailInfo();
                break;
            case R.id.CloudBtn:
                showCloudInfo();
                break;
            /*case R.id.DiasporaBtn://end of Disroot's Dandelion
                showDiaInfo();
                break;*/
            case R.id.ForumBtn:
                showForumInfo();
                break;
            case R.id.ChatBtn:
                showChatInfo();
                break;
            case R.id.PadBtn:
                showPadInfo();
                break;
            case R.id.CryptpadBtn:
                showCryptpadInfo();
                break;
            case R.id.BinBtn:
                showBinInfo();
                break;
            case R.id.UploadBtn:
                showUploadInfo();
                break;
            case R.id.SearxBtn:
                showSearxInfo();
                break;
            case R.id.BoardBtn:
                showBoardInfo();
                break;
            case R.id.CallsBtn:
                showCallsInfo();
                break;
            case R.id.NotesBtn:
                showNotesInfo();
                break;
            case R.id.GitBtn:
                showGitInfo();
                break;
            case R.id.UserBtn:
                showUserInfo();
                break;
            case R.id.StateBtn:
                showStateInfo();
                break;
            case R.id.HowToBtn:
                showHowToInfo();
                break;
            case R.id.AboutBtn:
                showAboutInfo();
                break;
            default:
                Toast.makeText(view.getContext(), R.string.activity_main_share_info, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //Dialog windows

    //hide Dashboard
    private void hideDashboard() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        webView.setVisibility( View.VISIBLE );
        dashboard.setVisibility( View.GONE );
    }

    //Show chat choice
    private void showChoose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.ChooseChatTitle)
                .setMessage(R.string.ChooseChat);
        View view = View.inflate(this, R.layout.check_remember, null);
        final CheckBox checkChat = (CheckBox) view.findViewById(R.id.checkChat);
        checkChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        builder.setPositiveButton(R.string.Conversations, new DialogInterface.OnClickListener() {
            Intent xmpp1 = getPackageManager().getLaunchIntentForPackage(Constants.Conversations);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (((CheckBox) checkChat).isChecked()) {
                    check.edit().putBoolean("checkConv", true).apply();
                    startActivity(xmpp1);
                    return;
                }
                else
                    startActivity(xmpp1);
                return;
            }
        });
        builder.setNegativeButton(R.string.PixArt, new DialogInterface.OnClickListener() {
            Intent xmpp2 = getPackageManager().getLaunchIntentForPackage(Constants.PixArt);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (((CheckBox) checkChat).isChecked()) {
                    check.edit().putBoolean("checkPix", true).apply();
                    startActivity(xmpp2);
                    return;
                }
                else
                    startActivity(xmpp2);
                return;
            }
        });
        builder.setView(view);
        builder.show();
    }

    private void showFirstTap() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.FirstTitle);
        builder.setMessage(getString(R.string.FirstInfo));
        builder.setPositiveButton(R.string.global_ok,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showOptimzation();
            }
        });
        builder.show();
    }

    private void showOptimzation() {
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService( Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations(packageName))
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        else {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
        }
        startActivity(intent);
    }

    private void showOptimzationInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.OptimizationTitle);
        builder.setMessage(getString(R.string.OptimizationInfo));
        builder.setPositiveButton(R.string.global_ok,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showOptimzation();
            }
        });
        builder.show();
    }

    //Mail Info
    private void showMailInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.MailInfoTitle);
        builder.setMessage(email + "\n\n" + getString(R.string.MailInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_K9HELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.MailBtn).getParent()!=null){
                    viewGroup.removeView(MailBtn);
                    BtnPreference.edit().putBoolean( "MailBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }
    private void showMailDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(getString(R.string.MailDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent mail = getPackageManager().getLaunchIntentForPackage(Constants.k9);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mail = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.k9));
                startActivity(mail);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }

    //Cloud Info
    private void showCloudInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.CloudInfoTitle);
        builder.setMessage(cloud + "\n\n" + getString(R.string.CloudInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_CLOUDHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.CloudBtn).getParent()!=null){
                    viewGroup.removeView(CloudBtn);
                    BtnPreference.edit().putBoolean( "CloudBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }
    private void showCloudDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(getString(R.string.CloudDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent cloud = getPackageManager().getLaunchIntentForPackage(Constants.nc);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cloud = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.nc));
                startActivity(cloud);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }

    /*End of Disroot's Dandelion*
    //Diaspora info
    private void showDiaInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiasporaTitle);
        builder.setMessage(getString(R.string.DiasporaInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_DIAHELP);
                hideDashboard();
            }
        });
        builder.show();
    }
    private void showDiaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(getString(R.string.DiasporaDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent pod = getPackageManager().getLaunchIntentForPackage(Diaspora);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pod = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Diaspora));
                startActivity(pod);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }*/

    private void showForumInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.ForumTitle);
        builder.setMessage(forum + "\n\n"+ getString(R.string.ForumInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_FORUMHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.ForumBtn).getParent()!=null){
                    viewGroup.removeView(ForumBtn);
                    BtnPreference.edit().putBoolean( "ForumBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showForget() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.ForgetTitle);
        if(check.getBoolean("checkConv", true)|| check.getBoolean("checkPix",true)) {
            View view = View.inflate(this, R.layout.check_forget, null);
            final CheckBox forgetChat = (CheckBox) view.findViewById(R.id.forgetChat);
            forgetChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    check.edit().putBoolean("checkConv", false).apply();
                    check.edit().putBoolean("checkPix", false).apply();
                }
            });
            builder.setView(view);
        }
        builder.setPositiveButton(R.string.global_ok, null);
        builder.show();
    }

    private void showChatInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.ChatTitle);
        builder.setMessage(xmpp +"\n\n"+ getString(R.string.ChatInfo));
        if(check.getBoolean("checkConv", true)|| check.getBoolean("checkPix",true)) {
            View view = View.inflate(this, R.layout.check_forget, null);
            final CheckBox forgetChat = (CheckBox) view.findViewById(R.id.forgetChat);
            forgetChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    check.edit().putBoolean("checkConv", false).apply();
                    check.edit().putBoolean("checkPix", false).apply();
                }
            });
            builder.setView(view);
        }
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_XMPPHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.ChatBtn).getParent()!=null){
                    viewGroup.removeView(ChatBtn);
                    BtnPreference.edit().putBoolean( "ChatBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }
    private void showChatDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(getString(R.string.ChatDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent xmpp1 = getPackageManager().getLaunchIntentForPackage(Constants.Conversations);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                xmpp1 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.Conversations));
                startActivity(xmpp1);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }

    private void showPadInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.PadTitle);
        builder.setMessage(etherpad +"\n\n"+ getString(R.string.PadInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_PADHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.PadBtn).getParent()!=null){
                    viewGroup.removeView(PadBtn);
                    BtnPreference.edit().putBoolean( "PadBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }
    private void showPAdDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(getString(R.string.PadDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent pad = getPackageManager().getLaunchIntentForPackage(Constants.Padland);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pad = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.Padland));
                startActivity(pad);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }

    private void showCryptpadInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.CryptpadTitle );
        builder.setMessage(cryptpad +"\n\n"+ getString(R.string.CryptpadInfo ));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_CRYPTPADHELP );
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.CryptpadBtn ).getParent()!=null){
                    viewGroup.removeView( CryptpadBtn );
                    BtnPreference.edit().putBoolean( "CryptpadBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showBinInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.BinTitle);
        builder.setMessage(bin +"\n\n"+ getString(R.string.BinInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_BINHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.BinBtn).getParent()!=null){
                    viewGroup.removeView(BinBtn);
                    BtnPreference.edit().putBoolean( "BinBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showUploadInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.UploadTitle);
        builder.setMessage(upload +"\n\n"+ getString(R.string.UploadInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_UPLOADHELP);
                webView.setVisibility(View.VISIBLE);
                findViewById(R.id.dashboard).setVisibility(View.GONE);
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.UploadBtn).getParent()!=null){
                    viewGroup.removeView(UploadBtn);
                    BtnPreference.edit().putBoolean( "UploadBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showSearxInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.SearxTitle);
        builder.setMessage(searx +"\n\n"+ getString(R.string.SearxInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_SEARXHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.SearxBtn).getParent()!=null){
                    viewGroup.removeView(SearxBtn);
                    BtnPreference.edit().putBoolean( "SearxBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showBoardInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.BoardTitle);
        builder.setMessage(taiga +"\n\n"+ getString(R.string.BoardInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_BOARDHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.BoardBtn).getParent()!=null){
                    viewGroup.removeView(BoardBtn);
                    BtnPreference.edit().putBoolean( "BoardBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showCallsInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.CallsTitle);
        builder.setMessage(getString(R.string.CallsInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_CALLSHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.CallsBtn).getParent()!=null){
                    viewGroup.removeView(CallsBtn);
                    BtnPreference.edit().putBoolean( "CallsBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }
    private void showBoardDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(taiga +"\n\n"+ getString(R.string.CallsDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent calls = getPackageManager().getLaunchIntentForPackage(Constants.CallsApp);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                calls = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.CallsApp));
                startActivity(calls);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }

    //There is no extra info about Nextcoud notes yet
    private void showNotesInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.NotesTitle);
        builder.setMessage(notes +"\n\n"+ getString(R.string.NotesInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_NOTESHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.NotesBtn).getParent()!=null){
                    viewGroup.removeView(NotesBtn);
                    BtnPreference.edit().putBoolean( "NotesBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }
    private void showNotesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(getString(R.string.NotesDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent notes = getPackageManager().getLaunchIntentForPackage(Constants.NotesApp);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notes = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.NotesApp));
                startActivity(notes);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }

    private void showGitInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.GitTitle);
        builder.setMessage(git +"\n\n"+ getString(R.string.GitInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_GITHELP);
                hideDashboard();
            }
        });
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.GitBtn).getParent()!=null){
                    viewGroup.removeView(GitBtn);
                    BtnPreference.edit().putBoolean( "GitBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }
    private void showGitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiaInstallTitle);
        builder.setMessage(getString(R.string.GitsDialog));
        builder.setPositiveButton(R.string.global_install, new DialogInterface.OnClickListener() {
            Intent git = getPackageManager().getLaunchIntentForPackage(Constants.GitApp);
            @Override
            public void onClick(DialogInterface dialog, int which) {
                git = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.GitApp));
                startActivity(git);
            }
        });
        builder.setNegativeButton(R.string.global_cancel , null);
        builder.show();
    }


    private void showUserInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false)
                .setTitle(R.string.UserTitle)
                .setMessage(user +"\n\n"+ getString(R.string.UserInfo))
                .setPositiveButton(R.string.global_ok, null);
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.UserBtn).getParent()!=null){
                    viewGroup.removeView(UserBtn);
                    BtnPreference.edit().putBoolean( "UserBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showStateInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false)
                .setTitle(R.string.StateTitle);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.state_dialog, (ViewGroup) findViewById(R.id.StateView));
        //xmppBtn
        Button xmppBtn = view.findViewById(R.id.xmppBtn);
        xmppBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_DisApp_STATEXMPP));
                Intent xmpp = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(xmpp);
            }

        });
        //MatrixBtn
        Button matrixBtn = view.findViewById(R.id.matrixBtn);
        matrixBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_DisApp_STATEMATRIX));
                Intent matrix = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(matrix);
            }

        });
        //SocialBtn
        Button SocialBtn = view.findViewById(R.id.SocialBtn);
        SocialBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_DisApp_STATESOCIAL));
                Intent social = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(social);
            }

        });
        //newsBtn
        Button NewsBtn = view.findViewById(R.id.NewsBtn);
        NewsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_DisApp_STATENEWS));
                Intent news = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(news);
            }

        });
        //rssBtn
        Button RssBtn = view.findViewById(R.id.RssBtn);
        RssBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_DisApp_STATERSS));
                Intent rss = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(rss);
            }

        });
        builder.setView(view)
                .setPositiveButton(R.string.global_ok, null)
                .show();
    }

    private void showHowToInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.HowToTitle);
        builder.setMessage(getString(R.string.HowToInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.HowToBtn).getParent()!=null){
                    viewGroup.removeView(HowToBtn);
                    BtnPreference.edit().putBoolean( "HowToBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showAboutInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.AboutTitle);
        builder.setMessage(getString(R.string.AboutInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNeutralButton( R.string.hide, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewGroup viewGroup =((ViewGroup)findViewById( R.id.StateBtn ).getParent());
                if (findViewById( R.id.AboutBtn).getParent()!=null){
                    viewGroup.removeView(AboutBtn);
                    BtnPreference.edit().putBoolean( "AboutBtn", false ).apply();
                    return;}
            }
        });
        builder.show();
    }

    private void showLogoInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.LogoTitle);
        builder.setMessage(getString(R.string.LogoInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.LogoBtn, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {int Counter = 0;
            @Override
            public void onClick(View v)
            {
                if (Counter < 10)
                    Counter++;
                //first time tap check
                if ((Counter == 10 )){
                    Intent goTap = new Intent(MainActivity.this, wsdfhjxc.taponium.MainActivity.class);
                    MainActivity.this.startActivity(goTap);
                    dialog.dismiss();
                }
            }
        });
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        pbutton.setTextColor(Color.BLACK);
    }


    public void shareCurrentPage() {
        ScrollView dashboard = findViewById(R.id.dashboard);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setAction(Intent.ACTION_SEND);
        if (dashboard.getVisibility() == View.GONE) intent.putExtra( Intent.EXTRA_TEXT, webView.getUrl() );
        else intent.putExtra( Intent.EXTRA_TEXT, Constants.URL_DisApp_DISAPP );
        intent.setType("text/plain");
        startActivity(intent);
    }

    //show snackbar to avoid exit on backpress
    @Override
    public void onBackPressed() {
        ScrollView dashboard = findViewById(R.id.dashboard);
        FragmentManager manager = getSupportFragmentManager();
        if (dashboard.getVisibility() == View.GONE){
            dashboard.setVisibility(View.VISIBLE);
            return;
        }
        if (manager.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            snackbarExitApp.show();
        }
        return;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
        //inetnt filter get url from external
        Uri url = getIntent().getData();
        if (url != null) {
            Log.d("TAG", "URL Foud");
            Log.d("TAG", "Url is :" + url);
            hideDashboard();
            webView.loadUrl(url.toString());
        }
        //first start
        if (firstStart.getBoolean("firstrun", true)) {
            Intent welcome = new Intent(MainActivity.this, WelcomeActivity.class);
            MainActivity.this.startActivity(welcome);
            firstStart.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disWebChromeClient.hideCustomView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (disWebChromeClient.hideCustomView()) {
                return true;
            } else if (!disWebChromeClient.hideCustomView() && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem register = menu.findItem(R.id.action_forget);
        if(check.getBoolean("checkConv", true)||check.getBoolean("checkPix", true)) {
            register.setVisible(true);
        }
        else
        {
            register.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // To show icons in the actionbar's overflow menu:
        // http://stackoverflow.com/questions/18374183/how-to-show-icons-in-overflow-menu-in-actionbar
        if(menu.getClass().getSimpleName().equals("MenuBuilder")) try {
            Method m = menu.getClass().getDeclaredMethod(
                    "setOptionalIconsVisible", Boolean.TYPE);
            m.setAccessible(true);
            m.invoke(menu, true);
        } catch (NoSuchMethodException e) {
            Log.e(Constants.TAG, "onMenuOpened", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ScrollView dashboard = findViewById(R.id.dashboard);
        TranslateAnimation animateup = new TranslateAnimation(0,0,-2*dashboard.getHeight(),0);
        TranslateAnimation animatedown = new TranslateAnimation(0,0,0,-dashboard.getHeight());
        switch (item.getItemId()) {
            case R.id.action_share:
                shareCurrentPage();
                return true;
            /*case R.id.action_home:
                if(webView.getVisibility()==View.VISIBLE){
                    //animation
                    animateup.setDuration(500);
                    animateup.setFillAfter(false);
                    dashboard.startAnimation(animateup);
                    dashboard.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    return true;
                }

                if (webView.getVisibility()==View.GONE && webView.getUrl()!=null){
                    //animation
                    animatedown.setDuration(500);
                    animatedown.setFillAfter(false);
                    dashboard.startAnimation(animatedown);
                    hideDashboard();
                    return true;
                }
                else
                    return true;*/
            case R.id.action_forget:
                showForget();

            case R.id.action_reload: {
                String url = webView.getUrl();
                webView.loadUrl(url);
                return true;
            }
            case R.id.action_optimization:
                showOptimzation();
                return true;
            case R.id.action_about:
                Intent goAbout = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(goAbout);
                return true;
            case R.id.action_set_icons:
                Intent goBtnSettings = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(goBtnSettings);
                return true;
            case R.id.action_clear_cookies: {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    CookieManager.getInstance().removeAllCookies(null);
                }else{
                    CookieManager.getInstance().removeAllCookie();
                }
            }
            return false;
            case R.id.action_exit: {
                moveTaskToBack(true);
                finish();
                return false;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupWebView(Bundle savedInstanceState, FrameLayout customViewContainer) {
        disWebChromeClient = new DisWebChromeClient(webView, customViewContainer);
        progressBar = findViewById(R.id.progressbarLoading);
        webView = findViewById(R.id.webView_content);
        webView.setWebChromeClient(disWebChromeClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);//solves taiga board \o/
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setOnLongClickListener(this);

        //enable cookies
        cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(webView.getContext());
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView,false);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
        CookieSyncManager syncManager = CookieSyncManager.createInstance(webView.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        String cookieString = "cookie_name=cookie_value; path=/";
        String baseUrl="disroot.org";
        cookieManager.setCookie(baseUrl, cookieString);
        syncManager.sync();
        String cookies = cookieManager.getCookie(baseUrl);
        if (cookies != null) {
            cookieManager.setCookie(baseUrl, cookies);
            for (String c : cookies.split(";")) {

            }
        }

        //Make download possible
        webView.setDownloadListener(new DownloadListener() {

            @TargetApi(Build.VERSION_CODES.M)
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                //open dialog for permissions
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission error","You have permission");
                } else {

                    Log.e("Permission error","You have asked for permission");
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                if (Uri.parse( url ).toString().startsWith( "blob" )){
                    webView.loadUrl("");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_DisApp_UPLOAD)));
                } else {
                    final String filename = URLUtil.guessFileName( url, contentDisposition, mimetype );
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse( url ) );
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility( DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED );
                    request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, filename );
                    DownloadManager dm = (DownloadManager) getSystemService( DOWNLOAD_SERVICE );
                    assert dm != null;
                    dm.enqueue( request );
                }

            }
        });

        //check permissions
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setWebChromeClient(new ChromeClient());
        webView.loadUrl(loadUrl);

        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("http")&&url.contains("disroot")&&!Uri.parse( url ).toString().startsWith( "blob" )) {
                    view.loadUrl(url);
                    return super.shouldOverrideUrlLoading(view, url);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                }
            }
        });
    }

    public boolean handleUrl(String url){

        if (url.startsWith("geo:") || url.startsWith("mailto:") || url.startsWith("tel:") || url.startsWith("sms:")|| url.startsWith("xmpp:")) {
            Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(searchAddress);
        }else
            webView.loadUrl(url);
        return true;
    }

    private  boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),Constants.REQUEST_ID_MULTIPLE_PERMISSIONS);
            Log.e(Constants.TAG, "Returned falseeeee-------");
            return false;
        }
        Log.d(Constants.TAG, "Permission returned trueeeee-------");
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(Constants.TAG, "Permission callback called-------");
        switch (requestCode) {
            case Constants.REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(Constants.TAG, "camera & Storage permission granted");
                        Toast.makeText(this, "Permissions granted! Try now.", Toast.LENGTH_SHORT).show();
                        //chromClt.openChooser(WebView, chooserPathUri, chooserParams);
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(Constants.TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            // proceed with logic by disabling the related features or quit the app.
                                            break;
                                    }
                                }
                            });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
                break;
            }
        }

    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Camera and Storage Permission required for this app")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != Constants.INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == MainActivity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }else {
                        if (data.getClipData() != null) {
                            final int numSelectedFiles = data.getClipData().getItemCount();

                            results = new Uri[numSelectedFiles];

                            for (int i = 0; i < numSelectedFiles; i++) {
                                results[i] = data.getClipData().getItemAt(i).getUri();
                            }
                        }
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != Constants.FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri result = null;
            try {
                if (resultCode != RESULT_OK) {
                    result = null;
                } else {
                    // retrieve from the private variable if the intent is null
                    result = data == null ? mCapturedImageURI : data.getData();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "activity :" + e,
                        Toast.LENGTH_LONG).show();
            }
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public class ChromeClient extends WebChromeClient {

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            // callback.invoke(String origin, boolean allow, boolean remember);
            Log.e(Constants.TAG, "onGeolocationPermissionsShowPrompt: " );
            callback.invoke(origin, true, false);
        }

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            chooserPathUri = filePath;
            chooserParams = fileChooserParams;

            if(checkAndRequestPermissions()){
                openChooser(chooserPathUri);
                return true;
            }else {
                return false;
            }
        }

        void openChooser(ValueCallback<Uri[]> filePath){
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;
            Intent takePictureIntent;

            takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e(Constants.TAG, "Unable to create Image File", ex);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            if (Build.VERSION.SDK_INT >= 18) {
                contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            contentSelectionIntent.setType("*/*");
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, Constants.INPUT_FILE_REQUEST_CODE);
        }

        /* openFileChooser for Android 3.0+ */
        @SuppressWarnings("ResultOfMethodCallIgnored")
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");
            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                boolean mkdirs = imageStorageDir.mkdirs();
            }
            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");
            mCapturedImageURI = Uri.fromFile(file);
            // Camera capture image intent
            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
            // On select image call onActivityResult method of activity
            chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(chooserIntent, Constants.FILECHOOSER_RESULTCODE);
        }

        // openFileChooser for Android < 3.0
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {
            openFileChooser(uploadMsg, acceptType);
        }
    }






    //components
    @SuppressLint("StaticFieldLeak")
    class GetList extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStringcomponents = sh.makeServiceCall( Constants.components );

            Log.e( TAG, "Response from url(Service): " + Constants.components );

            if (jsonStringcomponents != null) {//components page
                try {
                    JSONObject jsonObj = new JSONObject(jsonStringcomponents);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("systems");

                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        HashMap<String, String> serviceDetails = new HashMap<>();

                        //String id = c.getString("id");
                        String name = c.getString("name");
                        //String description = c.getString("description");

                        // tmp hash map for single service

                        // adding each child node to HashMap key => value
                        //serviceDetails.put("id", id);
                        serviceDetails.put("name", name);
                        if (c.has("description")&&!c.isNull("description")){
                            String description = c.getString("description");
                            serviceDetails.put("description", description);
                        }
                        else {
                            serviceDetails.put("description", "No Description");
                        }
                        //serviceDetails.put("description", description);

                        // adding service to service list
                        componentList.add(serviceDetails);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Is your internet connection ok?",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute( result );
            // Dismiss the progress dialog
            for (int a =0; a<componentList.size();a++)
            {
                HashMap<String, String> hashmap= (HashMap<String, String>) componentList.get(a);
                String hash = hashmap.get("name");
                String description = "";
                if (hashmap.get("description")!=null &&!hashmap.isEmpty()){//.has("description")&&!hasmap.isNull("description")
                    description = hashmap.get("description");
                }
                else {
                    description ="No Description";
                }
                switch (hash) {
                    case "Notes":
                        notes = description;
                        getNotes(notes);
                        break;
                    case "Mail Server":
                        email = description;
                        getEmail(email);
                        break;
                    case "Cloud":
                        cloud = description;
                        getCloud(cloud);
                        break;
                    case "Forum":
                        forum = description;
                        getForum(forum);
                        break;
                    case "Pad":
                        etherpad = description;
                        getEtherpad(etherpad);
                        break;
                    case "Bin":
                        bin = description;
                        getBin(bin);
                        break;
                    case "Upload":
                        upload = description;
                        getUpload(upload);
                        break;
                    case "Searx":
                        searx = description;
                        getSearx(searx);
                        break;
                    case "Project board":
                        taiga = description;
                        getTaiga(taiga);
                        break;
                    case "User Password management":
                        user = description;
                        getUser(user);
                        break;
                    case "XMPP Chat server":
                        xmpp = description;
                        getXmpp(xmpp);
                        break;
                    case "Git":
                        git = description;
                        getGit(git);
                        break;
                    case "Cryptpad":
                        cryptpad = description;
                        getCryptpad(cryptpad);
                        break;
                }
            }
        }
    }

    private void getEmail(String string){
        email = string;
    }
    private void getCloud(String string){
        cloud = string;
    }
    private void getForum(String string){
        forum = string;
    }
    private void getEtherpad(String string){
        etherpad = string;
    }
    private void getBin(String string){
        bin = string;
    }
    private void getUpload(String string){
        upload = string;
    }
    private void getSearx(String string){
        searx = string;
    }
    private void getTaiga(String string){
        taiga = string;
    }
    private void getUser(String string){
        user = string;
    }
    private void getXmpp(String string){
        xmpp = string;
    }
    private void getNotes(String string){
        notes = string;
    }
    private void getGit(String string){
        git = string;
    }
    private void getCryptpad(String string){
        cryptpad = string;
    }
}
