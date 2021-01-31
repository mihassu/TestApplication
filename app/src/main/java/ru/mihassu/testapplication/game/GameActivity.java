package ru.mihassu.testapplication.game;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.mihassu.testapplication.Logi;
import ru.mihassu.testapplication.R;
import ru.mihassu.testapplication.browser.BrowserActivity;

public class GameActivity extends AppCompatActivity {



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

    }
}
