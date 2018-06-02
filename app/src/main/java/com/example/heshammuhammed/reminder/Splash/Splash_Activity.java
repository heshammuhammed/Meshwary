package com.example.heshammuhammed.reminder.Splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.heshammuhammed.reminder.HomePage;
import com.example.heshammuhammed.reminder.MainHome.MainActivity;

public class Splash_Activity extends Activity {
    SharedPreferences share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splash_);
        share = getSharedPreferences("mypre", 0);
        if (share .getString("logged", "").toString().equals("logged")) {

            Intent intent = new Intent(Splash_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {

            Intent intent = new Intent(Splash_Activity.this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }
}
