package com.made_easy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreen extends AppCompatActivity {

    private static Context mContext;
    private static SharedPreferenceConfig preferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext = this;
        preferenceConfig = new SharedPreferenceConfig(mContext);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferenceConfig.readLoginStatus())
                {
                    Intent intent = new Intent(mContext, MainPage.class);
                    mContext.startActivity(intent);
                    ((SplashScreen)mContext).finish();
                }
                else if(!preferenceConfig.readLoginStatus())
                {
                    Intent intent = new Intent(mContext , LogIn.class);
                    mContext.startActivity(intent);
                    ((SplashScreen)mContext).finish();
                }
            }
        }, 2500);
    }
}
