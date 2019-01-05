package com.luoruiyong.fileshare;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {

    // SharedPreferences中的key
    private static final String KEY_IS_FIRST_RUN = "pref_is_first_run";
    private static final String KEY_IS_SHARE_HOST = "pref_is_share_host";

    // TCP端口号，当用户选定某一台共享主机时，建立TCP连接来进行通信
    public final static int TCP_PORT = 10240;
    // UDP端口号，用户查询当前内网中有哪些共享主机时使用，或直接查询某一个文件时广播使用
    public final static int UDP_PORT = 10720;

    // 保存基本的设备数据到SharedPreferences中
    private static SharedPreferences mPref;

    public static void init(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveRunState(boolean isFirstRun) {
        mPref.edit().putBoolean(KEY_IS_FIRST_RUN, isFirstRun).apply();
    }

    public static boolean isFirstRun() {
        return mPref.getBoolean(KEY_IS_FIRST_RUN, true);
    }

    public static void saveShareHostState(boolean isShareHost) {
        mPref.edit().putBoolean(KEY_IS_SHARE_HOST, isShareHost).apply();
    }

    public static boolean isShareHost() {
        return mPref.getBoolean(KEY_IS_SHARE_HOST, false);
    }
}
