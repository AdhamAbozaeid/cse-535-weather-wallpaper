package com.example.wallpaper_weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class preferencesActivity extends AppCompatActivity {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, preferencesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }

    public void applySettings(View view) {
        //startActivity(new Intent(this, MainActivity.class) );
    }
}
