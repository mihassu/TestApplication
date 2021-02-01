package ru.mihassu.testapplication.start;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import ru.mihassu.testapplication.Logi;
import ru.mihassu.testapplication.R;
import ru.mihassu.testapplication.browser.BrowserActivity;
import ru.mihassu.testapplication.game.GameActivity;

public class StartActivity extends AppCompatActivity {

    private final String DEEP_LINK_SIGN = "testlink";
    private final String TOKEN_PREFERENCES_NAME = "tokenPref";
    private final String TOKEN_EXTRA = "tokenExtra";
    private final String USER_DATA_KEY = "userData";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
//        if (targetUrl != null) {
//            Logi.logIt("App Link Target URL: " + targetUrl.toString());
//        }

        // Получить токен и сохранить в SharedPreferences
        getToken();

        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        AppLinkData.fetchDeferredAppLinkData(this, new AppLinkData.CompletionHandler() {
            @Override
            public void onDeferredAppLinkDataFetched(@Nullable AppLinkData appLinkData) {
                if (appLinkData != null) {
                    Logi.logIt("PromotionCode: " + appLinkData.getPromotionCode() + "\n");
                    Logi.logIt("Ref: " + appLinkData.getRef() + "\n");
                    Logi.logIt("TargetUri: " + appLinkData.getTargetUri() + "\n");
                    Logi.logIt("currentThread: " + Thread.currentThread().getName() + "\n");
                } else {
                    Logi.logIt("appLinkData is null");
                }
            }
        });

        //Получить уведомление при свернутом приложении, при клике по уведомлению
        getNotificationFromIntent();

        //Проверить есть ли deepLink и открыть нужную активити
//        checkUriFromDeepLink();
    }

    private void checkUriFromDeepLink() {
        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Logi.logIt("Key: " + key + "; Value: " + value);
//            }
            Uri uri = getIntent().getData();
            String[] splitUri = uri.toString().split("/");
            for (String s: splitUri) {
                if (s.equals(DEEP_LINK_SIGN)) {
                    startBrowserActivity();
                    return;
                }
            }
        }
        startGameActivity();
    }

    private void startBrowserActivity() {
        Intent intent = new Intent(this, BrowserActivity.class);
        startActivity(intent);
        finish();
    }

    private void startGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    private void getToken() {
        String token = loadToken();
        if (token != null) {
            Logi.logIt("Токен: " + token);

        } else {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        showMessage(getString(R.string.token_receive_error));
                        return;
                    }

                    String token = task.getResult();
                    Logi.logIt("Токен: " + token);
                    saveToken(token);
                }
            });
        }
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(TOKEN_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_EXTRA, token);
        editor.apply();
    }

    private String loadToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(TOKEN_PREFERENCES_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_EXTRA, null);
        return token;
    }

    private AlertDialog createDialog(String title, String message) {
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_positive_button), (dialog, which) -> dialog.dismiss())
                .create();
    }

    private void getNotificationFromIntent() {
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                if (key.equals(USER_DATA_KEY)) {
                    createDialog(getString(R.string.notification_title), value.toString()).show();
                }
                Logi.logIt("Key: " + key + "; Value: " + value);
            }
        }
    }

    private void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
