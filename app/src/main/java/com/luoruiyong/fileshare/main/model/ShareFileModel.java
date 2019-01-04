package com.luoruiyong.fileshare.main.model;

import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.ArrayList;
import java.util.List;

public class ShareFileModel {

    private OnShareFileDataReceiveListener mListener;

    public void setOnShareFileDataReceiveListener(OnShareFileDataReceiveListener listener) {
        this.mListener = listener;
    }

    public void sentShareFileRequest(final Host host) {
        // 向对应的主机发送获取共享文件列表请求
        // 使用的是TCP链接
        // 收到回传的数据列表,解析

        // 模拟
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<ShareFile> list = new ArrayList<>();
                for (int i = 0; i < host.getFileCount(); i++) {
                    ShareFile file = new ShareFile();
                    file.setName("Android 从入门到放弃 " + (i + 1));
                    file.setSize((int)(Math.random() * 100000));
                    file.setDownload(i % 4 == 0);
                    list.add(file);
                }
                if (mListener != null) {
                    mListener.onReceiveFileList(list);
                }
            }
        }).start();
    }

    public void downloadFile() {

    }


    public interface OnShareFileDataReceiveListener {
        void onReceiveFileList(List<ShareFile> list);
    }
}
