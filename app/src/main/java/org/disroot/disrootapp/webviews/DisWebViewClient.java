package org.disroot.disrootapp.webviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.webview.R;

import org.disroot.disrootapp.ui.MainActivity;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by jackson on 02/11/15.
 */
public class DisWebViewClient extends WebViewClient {

    private final Bundle savedInstanceState;
    private final ViewGroup viewLoading;
    private boolean isViewLoadingNotStarted = true;

    public DisWebViewClient(Bundle savedInstanceState, ViewGroup viewLoading) {
        this.savedInstanceState = savedInstanceState;
        this.viewLoading = viewLoading;
    }
Context context;
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



    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (isViewLoadingNotStarted) {
            viewLoading.setVisibility(View.VISIBLE);
            isViewLoadingNotStarted = false;
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (!isViewLoadingNotStarted) {
            viewLoading.setVisibility(View.INVISIBLE);
            isViewLoadingNotStarted = true;
        }
        view.setVisibility(View.VISIBLE);
    }
}
