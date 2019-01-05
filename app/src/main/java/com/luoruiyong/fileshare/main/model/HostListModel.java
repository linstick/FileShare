package com.luoruiyong.fileshare.main.model;

import android.util.Log;

import com.luoruiyong.fileshare.bean.Host;

public class HostListModel {
    private static final String TAG = "HostListModel";

    private OnHostDataReceiveListener mListener;

    public void setOnHostDataReceiveListener(OnHostDataReceiveListener listener) {
        this.mListener = listener;
    }

    public void sendShareHostRequest() {
        // 发送广播，查询哪些主机可以对外提供共享文件

        // 收到回传的广播，回调listener的接口

        // 模拟
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    Host host = new Host();
                    host.setName("主机" + (i + 1));
                    host.setIpAddress("192.168.1." + (i + 10));
                    host.setFileCount(i + 5);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 模拟收到一个主机回传的数据
                    Log.d(TAG, "收到主机回传数据： " + host);
                    if (mListener != null) {
                        mListener.onHostDataReceive(host);
                    }
                }
            }
        }).start();
    }


    public interface OnHostDataReceiveListener {
        void onHostDataReceive(Host host);
    }

}
