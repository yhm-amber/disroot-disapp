package org.disroot.disrootapp.webviews;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;


/**
 * Created by jackson on 02/11/15.
 */
public class DisWebChromeClient extends WebChromeClient {

    private final WebView webView;
    private final FrameLayout frameLayoutContainer;
    private View viewCustom;
    private CustomViewCallback customViewCallback;

    public DisWebChromeClient(WebView webView, FrameLayout frameLayoutContainer) {
        this.webView = webView;
        this.frameLayoutContainer = frameLayoutContainer;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        if (viewCustom == null) {
            viewCustom = view;
            webView.setVisibility(View.GONE);
            frameLayoutContainer.setVisibility(View.VISIBLE);
            frameLayoutContainer.addView(view);
            customViewCallback = callback;
        } else {
            callback.onCustomViewHidden();
        }
    }

       @Override
    public void onHideCustomView() {
        super.onHideCustomView();
        if (viewCustom != null) {
            setWebViewVisible();
            removeCustomViewFromParent();
        }
    }

    private void setWebViewVisible() {
        webView.setVisibility(View.VISIBLE);
        frameLayoutContainer.setVisibility(View.GONE);
        viewCustom.setVisibility(View.GONE);
    }

    private void removeCustomViewFromParent() {
        frameLayoutContainer.removeView(viewCustom);
        customViewCallback.onCustomViewHidden();
        viewCustom = null;
    }

    public boolean hideCustomView() {
        if (viewCustom == null) {
            return false;
        } else {
            onHideCustomView();
            return true;
        }
    }
}
