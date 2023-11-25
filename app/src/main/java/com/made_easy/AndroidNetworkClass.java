package com.made_easy;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class AndroidNetworkClass extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
