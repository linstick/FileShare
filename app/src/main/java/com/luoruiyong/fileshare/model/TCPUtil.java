package com.luoruiyong.fileshare.model;

public class TCPUtil {
    public static void startTCPListener() {
        new Thread(TCPListener.getInstance()).start();
    }
}
