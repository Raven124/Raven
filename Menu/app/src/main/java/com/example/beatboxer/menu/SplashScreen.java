package com.example.beatboxer.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
    private final int SPLASH = 2000;
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



