package com.luoruiyong.fileshare;

import android.app.Application;

import com.luoruiyong.fileshare.model.TCPListener;
import com.luoruiyong.fileshare.model.TCPUtil;
import com.luoruiyong.fileshare.model.UDPUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Config.init(this);
        UDPUtil.startUDPReceiver();
        TCPUtil.startTCPListener();
    }
}
