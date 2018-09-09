package org.disroot.disrootapp.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.webview.R;

import org.disroot.disrootapp.utils.Constants;
import org.disroot.disrootapp.webviews.DisWebChromeClient;
import org.disroot.disrootapp.webviews.DisWebViewClient;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private WebView webView;
    private DisWebChromeClient disWebChromeClient;
    Button button;
    SharedPreferences firstStart = null;//first start



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayoutContainer = (FrameLayout) findViewById(R.id.framelayout_container);
        ViewGroup viewLoading = (ViewGroup) findViewById(R.id.linearlayout_view_loading_container);
        setupWebView(savedInstanceState, frameLayoutContainer, viewLoading);
        firstStart = getSharedPreferences("org.disroot.disrootap", MODE_PRIVATE);//fisrt start
        // enables the activity icon as a 'home' button. required if "android:targetSdkVersion" > 14
        //getActionBar().setHomeButtonEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ScrollView dashboard = (ScrollView)findViewById(R.id.dashboard);

        //Set buttons
        // Locate the button in activity_main.xml
        button = (Button) findViewById(R.id.MailBtn);//MailBtn
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
                if(mail == null) {
                    mail = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+k9));
                }
                startActivity(mail);
            }

        });

        button = (Button) findViewById(R.id.CloudBtn);//CloudBtn
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
                }
                startActivity(cloud);
            }

        });

        button = (Button) findViewById(R.id.DiasporaBtn);//DiasporaBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String Diaspora = "com.github.dfa.diaspora_android";
                Intent pod = getPackageManager().getLaunchIntentForPackage(Diaspora);
                if(pod == null) {
                    pod = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Diaspora));
                }
                startActivity(pod);
            }

        });

        button = (Button) findViewById(R.id.ForumBtn);//ForumBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_FORUM);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.ChatBtn);//ChatBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String Conversations = "eu.siacs.conversations";
                Intent xmpp = getPackageManager().getLaunchIntentForPackage(Conversations);
                if(xmpp == null) {
                    xmpp = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Conversations));
                }
                startActivity(xmpp);
            }

        });

        button = (Button) findViewById(R.id.PadBtn);//PadBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String Padland = "com.mikifus.padland";
                Intent pad = getPackageManager().getLaunchIntentForPackage(Padland);
                if(pad == null) {
                    pad = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Padland));
                }
                startActivity(pad);
            }

        });

        button = (Button) findViewById(R.id.CalcBtn);//CalcBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_CALC);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.BinBtn);//BinBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_BIN);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.UploadBtn);//UploadBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_UPLOAD);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.SearxBtn);//SearxBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_SEARX);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.PollsBtn);//PollsBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_POLL);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.BoardBtn);//BoardBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_BOARD);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.UserBtn);//UserBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_USER);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.StateBtn);//UserBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_STATE);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.HowtoBtn);//AboutBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                webView.loadUrl(Constants.URL_DisApp_HOWTO);
                webView.setVisibility(View.VISIBLE);
                dashboard.setVisibility(View.GONE);
            }

        });

        button = (Button) findViewById(R.id.AboudBtn);//AboutBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent goAbout = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(goAbout);
            }

        });
    }

    private void showCloudInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.CloudInfoTitle);
        //builder.setMessage(getString(R.string.activity_main_manteiners, DeviceProvider.getAppVersion(this)));
        builder.setMessage(getString(R.string.CloudInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.show();
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
        final ScrollView dashboard = (ScrollView)findViewById(R.id.dashboard);
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
            showWelcome();
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
        ScrollView dashboard = (ScrollView)findViewById(R.id.dashboard);
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

            /**
        case R.id.action_mail:
            String k9 = "com.fsck.k9";
            Intent mail = getPackageManager().getLaunchIntentForPackage(k9);
            if(mail == null) {
                mail = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+k9));
            }
            startActivity(mail);
            return true;
        case R.id.action_cloud:
            String nc = "com.nextcloud.client";
            Intent cloud = getPackageManager().getLaunchIntentForPackage(nc);
            if(cloud == null) {
                cloud = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+nc));
            }
            startActivity(cloud);
            return true;
        case R.id.action_diaspora:
            String Diaspora = "com.github.dfa.diaspora_android";
            Intent pod = getPackageManager().getLaunchIntentForPackage(Diaspora);
            if(pod == null) {
                pod = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Diaspora));
            }
            startActivity(pod);
            return true;
        case R.id.action_forum:
            webView.loadUrl(Constants.URL_DisApp_FORUM);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
        case R.id.action_chat:
            String Conversations = "eu.siacs.conversations";
            Intent xmpp = getPackageManager().getLaunchIntentForPackage(Conversations);
            if(xmpp == null) {
                xmpp = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+Conversations));
            }
            startActivity(xmpp);
            return true;
        case R.id.action_pad:
            webView.loadUrl(Constants.URL_DisApp_PAD);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
        case R.id.action_calc:
            webView.loadUrl(Constants.URL_DisApp_CALC);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
        case R.id.action_bin:
            webView.loadUrl(Constants.URL_DisApp_BIN);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
        case R.id.action_upload:
            webView.loadUrl(Constants.URL_DisApp_UPLOAD);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
        case R.id.action_searx:
            webView.loadUrl(Constants.URL_DisApp_SEARX);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
        case R.id.action_poll:
            webView.loadUrl(Constants.URL_DisApp_POLL);
            webView.setVisibility(View.VISIBLE);
            dashboard.setVisibility(View.GONE);
            return true;
        case R.id.action_board:
            webView.loadUrl(Constants.URL_DisApp_BOARD);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
        case R.id.action_user:
            webView.loadUrl(Constants.URL_DisApp_USER);
            animatedown.setDuration(500);
            animatedown.setFillAfter(true);
            dashboard.startAnimation(animatedown);
            dashboard.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            return true;
             **/
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
    private void setupWebView(Bundle savedInstanceState, FrameLayout customViewContainer, ViewGroup viewLoading) {
        disWebChromeClient = new DisWebChromeClient(this, webView, customViewContainer);
        webView = (WebView) findViewById(R.id.webView_content);
        webView.setWebViewClient(new DisWebViewClient(savedInstanceState, viewLoading));
        webView.setWebChromeClient(disWebChromeClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);//solves taiga board \o/
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
       // webView.loadUrl(Constants.URL_DisApp_MAIN_PAGE);
        webView.setOnLongClickListener(this);
       // webView.setVisibility(View.GONE);;
    }

    public void shareCurrentPage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void showMailInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.MailInfoTitle);
        //builder.setMessage(getString(R.string.activity_main_manteiners, DeviceProvider.getAppVersion(this)));
        builder.setMessage(getString(R.string.MailInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.show();
    }

    private void showWelcome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.WelcomeTitle);
        builder.setMessage(getString(R.string.WelcomeInfo));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.show();
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(view.getContext(), R.string.activity_main_share_info, Toast.LENGTH_LONG).show();
        return false;
    }
}
