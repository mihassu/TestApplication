package ru.mihassu.testapplication.browser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import ru.mihassu.testapplication.Logi;
import ru.mihassu.testapplication.R;
import ru.mihassu.testapplication.network.AsyncRequester;

public class BrowserActivity extends AppCompatActivity {

    private final String WEB_VIEW_STATE = "webViewState";
    private WebView webView;
    private TextView urlField;
    private Button goButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        initViews();
        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(WEB_VIEW_STATE);
            if (bundle != null) {
                webView.restoreState(bundle);
            }
        }
        initWebView();

//        final AsyncRequester requester = new AsyncRequester(content ->
//                webView.loadData(content, "text/html; charset=utf-8", "utf-8")
//        );

    }

    private void initViews() {
        webView = findViewById(R.id.web_view);
        urlField = findViewById(R.id.et_url);
        goButton = findViewById(R.id.btn_go);
        goButton.setOnClickListener(v -> {
//                    requester.run(urlField.getText().toString());
                    webView.loadUrl(urlField.getText().toString());
                }
        );
        progressBar = findViewById(R.id.progress);
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient( progressValue -> {
            showProgress();
            updateProgress(progressValue);
        }));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Bundle bundle = new Bundle();
        webView.saveState(bundle);
        outState.putBundle(WEB_VIEW_STATE, bundle);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        webView.saveState(bundle);
        outState.putBundle(WEB_VIEW_STATE, bundle);
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void updateProgress(int value) {
        progressBar.setProgress(value);
        if (value >= 100) {
            hideProgress();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}