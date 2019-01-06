package com.luoruiyong.fileshare.main.model;

import com.google.gson.Gson;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.PacketContent;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.eventbus.DataChangeEvent;
import com.luoruiyong.fileshare.model.UDPReceiver;
import com.luoruiyong.fileshare.model.UDPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.luoruiyong.fileshare.Config.WAIT_FOR_RESPONSE_TIMEOUT;

public class OtherShareFileModel {

    private OnShareFileDataReceiveListener mListener;

    public void setOnShareFileDataReceiveListener(OnShareFileDataReceiveListener listener) {
        this.mListener = listener;
    }

    public void sentShareFileRequest(final Host host) {
        // 向对应的主机发送获取共享文件列表请求
        // 使用的是TCP链接
        // 收到回传的数据列表,解析

        PacketContent content = new PacketContent();
        content.setFunction(PacketContent.FUNCTION_REQUEST_SEARCH_SHARE_FILES);
        UDPReceiver.getInstance().setFileResponseTimeOut(false);
        UDPUtil.sendNormalPacket(host.getIpAddress(), new Gson().toJson(content));

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
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                List<ShareFile> list = new ArrayList<>();
//                for (int i = 0; i < host.getFileCount(); i++) {
//                    ShareFile file = new ShareFile();
//                    file.setName("Android 从入门到放弃 " + (i + 1));
//                    file.setSize((int)(Math.random() * 100000));
//                    file.setStatus(i % 4 == 0 ? ShareFile.STATUS_DOWNLOADED : ShareFile.STATUS_SHARED);
//                    list.add(file);
//                }
//                if (mListener != null) {
//                    mListener.onReceiveFileList(list);
//                }
//            }
//        }).start();
    }

    public void downloadFile() {

    }


    public interface OnShareFileDataReceiveListener {
        void onReceiveFileList(List<ShareFile> list);
    }
}
