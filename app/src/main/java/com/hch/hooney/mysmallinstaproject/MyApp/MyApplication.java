package com.hch.hooney.mysmallinstaproject.MyApp;

import android.app.Application;

import com.parse.Parse;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //SDK Init
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("CHiF4GUlnzFgl3RmAUJeVOyOTQX1Ui4hBWGkkGRy")
                .clientKey("TCbkL4gDRLirY5ayeD5Ii6WeAwmpQaSNi9kl3GcU")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
