package ru.mihassu.testapplication.browser;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

class CustomWebChromeClient extends WebChromeClient {

    private OnProgressChangeListener onProgressChangeListener;

    public CustomWebChromeClient(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        onProgressChangeListener.updateProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }


    interface OnProgressChangeListener {
        void updateProgress(int progress);
    }

}
