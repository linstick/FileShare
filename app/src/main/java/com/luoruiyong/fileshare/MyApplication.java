package com.luoruiyong.fileshare;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Config.init(this);
    }
}
