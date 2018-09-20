package org.disroot.disrootapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
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
import org.disroot.disrootapp.utils.Constants;
import org.disroot.disrootapp.webviews.DisWebChromeClient;

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

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private WebView webView;
    private DisWebChromeClient disWebChromeClient;
    Button button;
    SharedPreferences firstStart = null;//first start
    SharedPreferences check = null;
    private static final int INPUT_FILE_REQUEST_CODE = 1;//file upload
    private static final int FILECHOOSER_RESULTCODE = 1;
    String loadUrl;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    ValueCallback<Uri[]> chooserPathUri;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    WebView chooserWV;
    WebChromeClient.FileChooserParams chooserParams;

    public static final String CONTENT_HASHTAG = "content://org.disroot.disrootapp.ui.mainactivity/";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayoutContainer = findViewById(R.id.framelayout_container);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ViewGroup viewLoading = (ViewGroup) findViewById(R.id.linearlayout_view_loading_container);
        setupWebView(savedInstanceState, frameLayoutContainer);
        firstStart = getSharedPreferences("org.disroot.disrootap", MODE_PRIVATE);//fisrt start
        check = getSharedPreferences("org.disroot.disrootapp", MODE_PRIVATE);
        // enables the activity icon as a 'home' button. required if "android:targetSdkVersion" > 14
        //getActionBar().setHomeButtonEnabled(true);


        final ScrollView dashboard = findViewById(R.id.dashboard);

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
        //Set buttons
        // Locate the button in activity_main.xml
        button = findViewById(R.id.MailBtn);//MailBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                showMailInfo();
                return true;
            }
        });

        // Capture button clicks
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Start NewActivity.class
                String k9 = "com.fsck.k9";
                Intent mail = getPackageManager().getLaunchIntentForPackage(k9);
                if(mail == null&&(firstStart.getBoolean("firsttap", false))) {
                    mail = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+k9));
                }//first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                }
                else startActivity(mail);
            }
        });

        button = findViewById(R.id.CloudBtn);//CloudBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCloudInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String nc = "com.nextcloud.client";
                Intent cloud = getPackageManager().getLaunchIntentForPackage(nc);
                if(cloud == null) {
                    cloud = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+nc));
                }//first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                }
                else startActivity(cloud);
            }

        });

        button = findViewById(R.id.DiasporaBtn);//DiasporaBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDiaInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String Diaspora = "com.github.dfa.diaspora_android";
                Intent pod = getPackageManager().getLaunchIntentForPackage(Diaspora);
                if(pod == null) {
                    pod = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Diaspora));
                }//first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                }
                else startActivity(pod);
            }

        });

        button = findViewById(R.id.ForumBtn);//ForumBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showForumInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_FORUM);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.ChatBtn);//ChatBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showChatInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String Conversations = "eu.siacs.conversations";
                Intent xmpp1 = getPackageManager().getLaunchIntentForPackage(Conversations);
                String PixArt = "de.pixart.messenger";
                Intent xmpp2 = getPackageManager().getLaunchIntentForPackage(PixArt);
                if((xmpp1 == null)&&(xmpp2 == null)) {
                    xmpp1 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Conversations));
                }
                /* if((xmpp1 == null)&&(xmpp2 != null)) { */
                if((xmpp1 == null)&&(xmpp2 != null)) {//if(xmpp1 == null) {
                    startActivity(xmpp2);
                }
                //need to change to give user choise  || check.getBoolean("checkPix",false)
                if((xmpp1 != null)&&(xmpp2 != null)) {
                    if(check.getBoolean("checkConv", true)) {
                        startActivity(xmpp1);
                        return;
                    }
                    if(check.getBoolean("checkPix", true)) {
                        startActivity(xmpp2);
                        return;
                    }
                    else
                        showChoose();
                    return;
                    }
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                }
                else
                startActivity(xmpp1);
            }

        });

        button = findViewById(R.id.PadBtn);//PadBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPadInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String Padland = "com.mikifus.padland";
                Intent pad = getPackageManager().getLaunchIntentForPackage(Padland);
                if(pad == null) {
                    pad = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Padland));
                }
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                }
                else startActivity(pad);
            }

        });

        button = findViewById(R.id.CalcBtn);//CalcBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCalcInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_CALC);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.BinBtn);//BinBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showBinInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_BIN);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.UploadBtn);//UploadBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showUploadInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_UPLOAD);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.SearxBtn);//SearxBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showSearxInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_SEARX);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.PollsBtn);//PollsBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPollsInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_POLL);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.BoardBtn);//BoardBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showBoardInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_BOARD);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.UserBtn);//UserBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showUserInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_USER);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.StateBtn);//StateBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showStateInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_STATE);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.HowtoBtn);//HowToBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showHowToInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                    return;
                }
                else
                webView.loadUrl(Constants.URL_DisApp_HOWTO);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = findViewById(R.id.AboudBtn);//AboutBtn
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAboutInfo();
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent goAbout = new Intent(MainActivity.this, AboutActivity.class);
                //first time tap check
                if (firstStart.getBoolean("firsttap", true)){
                    showFirstTap();
                    firstStart.edit().putBoolean("firsttap", false).apply();
                }
                else
                MainActivity.this.startActivity(goAbout);
            }

        });

        ImageButton imageButton = findViewById(R.id.logo);//LogoBtn
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showLogoInfo();
                return true;
            }
        });
    }

    //Dialog windows
    private void showChoose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //builder.setCancelable(false);
        builder.setTitle(R.string.ChooseChatTitle)
                .setMessage(R.string.ChooseChat);
        //LayoutInflater inflater = getLayoutInflater();
        View view = View.inflate(this, R.layout.check_remember, null);
        final CheckBox checkChat = (CheckBox) view.findViewById(R.id.checkChat);
        checkChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        builder.setPositiveButton(R.string.Conversations, new DialogInterface.OnClickListener() {
            String Conversations = "eu.siacs.conversations";
            Intent xmpp1 = getPackageManager().getLaunchIntentForPackage(Conversations);
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
            String PixArt = "de.pixart.messenger";
            Intent xmpp2 = getPackageManager().getLaunchIntentForPackage(PixArt);
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
        builder.setPositiveButton(R.string.global_ok, null);
        builder.show();
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(view.getContext(), R.string.activity_main_share_info, Toast.LENGTH_LONG).show();
        return false;
    }

    //Mail Info
    private void showMailInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.MailInfoTitle);
        builder.setMessage(getString(R.string.MailInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_K9HELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }
    //Cloud Info
    private void showCloudInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.CloudInfoTitle);
        builder.setMessage(getString(R.string.CloudInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_CLOUDHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }
    //Diaspora info
    private void showDiaInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.DiasporaTitle);
        builder.setMessage(getString(R.string.DiasporaInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_DIAHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showForumInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.ForumTitle);
        builder.setMessage(getString(R.string.ForumInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_FORUMHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showForget() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
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
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.ChatTitle);
        builder.setMessage(getString(R.string.ChatInfo));
        // ||check.getBoolean("checkPix", true)==true
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
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showPadInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.PadTitle);
        builder.setMessage(getString(R.string.PadInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_PADHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showCalcInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.CalcTitle);
        builder.setMessage(getString(R.string.CalcInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_CALCHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showBinInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.BinTitle);
        builder.setMessage(getString(R.string.BinInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_BINHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showUploadInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.UploadTitle);
        builder.setMessage(getString(R.string.UploadInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_UPLOADHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showSearxInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.SearxTitle);
        builder.setMessage(getString(R.string.SearxInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_SEARXHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showPollsInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.PollsTitle);
        builder.setMessage(getString(R.string.PollsInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.more_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_POLLHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showBoardInfo() {
        final ScrollView dashboard = findViewById(R.id.dashboard);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.BoardTitle);
        builder.setMessage(getString(R.string.BoardInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.tell_more, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                webView.loadUrl(Constants.URL_DisApp_BOARDHELP);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    private void showUserInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false)
               .setTitle(R.string.UserTitle)
               .setMessage(getString(R.string.UserInfo))
               .setPositiveButton(R.string.global_ok, null);
        builder.show();
    }

    private void showStateInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false)
                .setTitle(R.string.StateTitle);
                //.setMessage(getString(R.string.StateInfo));
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


       /*
        final SpannableString s;
        s = new SpannableString(getString(R.string.StateInfo));
        final TextView tx1=new TextView(MainActivity.this);
        tx1.setText(s);
        tx1.setAutoLinkMask(RESULT_OK);
        tx1.setMovementMethod(LinkMovementMethod.getInstance());

        Linkify.addLinks(s, Linkify.WEB_URLS);
        builder.setTitle(R.string.StateTitle)
                .setCancelable(false)
                .setPositiveButton(R.string.global_ok, null);
                //.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                  //  public void onClick(DialogInterface dialog, int id) {
                  //      finish();
                  //  }
               // });
        builder.setView(tx1);
        builder.show();*/
       /* builder.setPositiveButton(R.string.global_ok, null);
        builder.setNegativeButton(R.string.state_help, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_DisApp_STATEXMPP));
                Intent xmpp = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(xmpp);
            }
        });
        builder.show();*/
    }

    private void showHowToInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.HowToTitle);
        builder.setMessage(getString(R.string.HowToInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.show();
    }

    private void showAboutInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.AboutTitle);
        builder.setMessage(getString(R.string.AboutInfo));
        builder.setPositiveButton(R.string.global_ok, null);
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
        final ScrollView dashboard = findViewById(R.id.dashboard);
        Uri url = getIntent().getData();
        if (url != null) {
            Log.d("TAG", "URL Foud");
            Log.d("TAG", "Url is :" + url);
            webView.setVisibility(View.VISIBLE);
            dashboard.setVisibility(View.GONE);
            webView.loadUrl(url.toString());
        }
        //first start
        if (firstStart.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            Intent welcome = new Intent(MainActivity.this, WelcomeActivity.class);
            MainActivity.this.startActivity(welcome);
            // using the following line to edit/commit prefs
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
        //if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
            try{
                @SuppressLint("PrivateApi") Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            }
            catch(NoSuchMethodException e){
                Log.e(TAG, "onMenuOpened", e);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        //}

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
            case R.id.action_home:
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
                    dashboard.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                return true;
                }
                else
                    return true;
            case R.id.action_forget: {
                if(check.getBoolean("checkConv", true)||check.getBoolean("checkPix", true)) {
                    setVisible(true);
                    showForget();

                }
            }
            case R.id.action_reload: {
                String url = webView.getUrl();
                webView.loadUrl(url);
                return true;
            }
            case R.id.action_about:
                Intent goAbout = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(goAbout);
                return true;
            case R.id.action_exit: {
                moveTaskToBack(true);
                finish();
                return false;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
/**
*    public void setupToolbar() {
 *       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  *      ((TextView) toolbar.findViewById(R.id.textview_toolbar_title)).setText(R.string.app_name);
   *     setSupportActionBar(toolbar);
    *    ActionBar actionBar = getSupportActionBar();
     *   if (actionBar != null)
      *      actionBar.setTitle("");
    }*/
    @SuppressLint("SetJavaScriptEnabled")
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
       // webView.loadUrl(Constants.URL_DisApp_MAIN_PAGE);
        webView.setOnLongClickListener(this);
       // webView.setVisibility(View.GONE);;

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
                final String filename= URLUtil.guessFileName(url, contentDisposition, mimetype);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                assert dm != null;
                dm.enqueue(request);

            }
        });

        //check permissions
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setWebChromeClient(new ChromeClient());
        webView.loadUrl(loadUrl); //change with your website

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
                if(url.startsWith("https")&&url.contains("disroot")) {
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            Log.e(TAG, "Returned falseeeee-------");
            return false;
        }
        Log.d(TAG, "Permission returned trueeeee-------");
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

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
                        Log.d(TAG, "camera & Storage permission granted");
                        Toast.makeText(this, "Permissions granted! Try now.", Toast.LENGTH_SHORT).show();
                        //chromClt.openChooser(chooserWV, chooserPathUri, chooserParams);
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
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
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
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
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
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
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
            Log.e(TAG, "onGeolocationPermissionsShowPrompt: " );
            callback.invoke(origin, true, false);
        }

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {

            chooserWV = view;
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
                    Log.e(TAG, "Unable to create Image File", ex);
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
            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
        }

        /* openFileChooser for Android 3.0+ */
        @SuppressWarnings("ResultOfMethodCallIgnored")
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
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
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
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

    //
    public void shareCurrentPage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        intent.setType("text/plain");
        startActivity(intent);
    }
}
