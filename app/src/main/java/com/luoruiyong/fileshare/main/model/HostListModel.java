package com.luoruiyong.fileshare.main.model;

import com.google.gson.Gson;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.PacketContent;
import com.luoruiyong.fileshare.eventbus.DataChangeEvent;
import com.luoruiyong.fileshare.model.UDPReceiver;
import com.luoruiyong.fileshare.model.UDPUtil;

import org.greenrobot.eventbus.EventBus;

import static com.luoruiyong.fileshare.Config.WAIT_FOR_RESPONSE_TIMEOUT;

public class HostListModel {
    private static final String TAG = "HostListModel";

    private OnHostDataReceiveListener mListener;

    public void setOnHostDataReceiveListener(OnHostDataReceiveListener listener) {
        this.mListener = listener;
    }

    public void sendShareHostRequest() {
        // 发送广播，查询哪些主机可以对外提供共享文件
        PacketContent content = new PacketContent();
        content.setFunction(PacketContent.FUNCTION_REQUEST_SEARCH_SHARE_HOST);
        UDPReceiver.getInstance().setHostResponseTimeOut(false);
        UDPUtil.sendBroadcastPacket(new Gson().toJson(content));

        // 广播发送之后，等待时间，防止无用户响应时界面一直在加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(WAIT_FOR_RESPONSE_TIMEOUT);
                    UDPReceiver.getInstance().setHostResponseTimeOut(true);
                    EventBus.getDefault().post(DataChangeEvent.HOST_DATA_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        // 模拟
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 5; i++) {
//                    Host host = new Host();
//                    host.setName("主机" + (i + 1));
//                    host.setIpAddress("192.168.1." + (i + 10));
//                    host.setFileCount(i + 5);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    // 模拟收到一个主机回传的数据
//                    Log.d(TAG, "收到主机回传数据： " + host);
//                    if (mListener != null) {
//                        mListener.onHostDataReceive(host);
//                    }
//                }
//            }
//        }).start();
    }


    public interface OnHostDataReceiveListener {
        void onHostDataReceive(Host host);
    }

}
