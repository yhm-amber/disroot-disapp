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
    private boolean isViewLoadingNotStarted = true;

    public DisWebViewClient(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
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
}

