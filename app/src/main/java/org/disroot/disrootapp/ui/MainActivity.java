package org.disroot.disrootapp.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webview.R;

import org.disroot.disrootapp.utils.Constants;
import org.disroot.disrootapp.utils.DeviceProvider;
import org.disroot.disrootapp.webviews.DisWebChromeClient;
import org.disroot.disrootapp.webviews.DisWebViewClient;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private WebView webView;
    private DisWebChromeClient disWebChromeClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        FrameLayout frameLayoutContainer = (FrameLayout) findViewById(R.id.framelayout_container);
        ViewGroup viewLoading = (ViewGroup) findViewById(R.id.linearlayout_view_loading_container);
        setupWebView(savedInstanceState, frameLayoutContainer, viewLoading);
        // enables the activity icon as a 'home' button. required if "android:targetSdkVersion" > 14
        //getActionBar().setHomeButtonEnabled(true);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
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
                Method m = menu.getClass().getDeclaredMethod(
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
        switch (item.getItemId()) {
            case R.id.action_share:
                shareCurrentPage();
                return true;
            case R.id.action_home:
                webView.loadUrl(Constants.URL_DisApp_MAIN_PAGE);
                return true;
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
                return true;
            case R.id.action_calc:
                webView.loadUrl(Constants.URL_DisApp_CALC);
                return true;
            case R.id.action_bin:
                webView.loadUrl(Constants.URL_DisApp_BIN);
                return true;
            case R.id.action_upload:
                webView.loadUrl(Constants.URL_DisApp_UPLOAD);
                return true;
            case R.id.action_searx:
                webView.loadUrl(Constants.URL_DisApp_SEARX);
                return true;
            case R.id.action_poll:
                webView.loadUrl(Constants.URL_DisApp_POLL);
                return true;
            case R.id.action_board:
                webView.loadUrl(Constants.URL_DisApp_BOARD);
                return true;
            case R.id.action_user:
                webView.loadUrl(Constants.URL_DisApp_USER);
                return true;
            case R.id.action_about:
                showAppInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) toolbar.findViewById(R.id.textview_toolbar_title)).setText(R.string.app_name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");
    }

    private void setupWebView(Bundle savedInstanceState, FrameLayout customViewContainer, ViewGroup viewLoading) {
        disWebChromeClient = new DisWebChromeClient(this, webView, customViewContainer);
        webView = (WebView) findViewById(R.id.webView_content);
        webView.setWebViewClient(new DisWebViewClient(savedInstanceState, viewLoading));
        webView.setWebChromeClient(disWebChromeClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
        webView.loadUrl(Constants.URL_DisApp_MAIN_PAGE);
        webView.setOnLongClickListener(this);
    }

    public void shareCurrentPage() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void showAppInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(getString(R.string.activity_main_manteiners, DeviceProvider.getAppVersion(this)));
        builder.setPositiveButton(R.string.global_ok, null);
        builder.show();
    }

    @Override
    public boolean onLongClick(View view) {
        Toast.makeText(view.getContext(), R.string.activity_main_share_info, Toast.LENGTH_LONG).show();
        return false;
    }

}
