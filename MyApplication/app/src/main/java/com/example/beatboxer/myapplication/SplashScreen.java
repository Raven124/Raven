package com.example.beatboxer.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainintent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainintent);
                SplashScreen.this.finish();

            }
        }, SPLASH);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }
}
