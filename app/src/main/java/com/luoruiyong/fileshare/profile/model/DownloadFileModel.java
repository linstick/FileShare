package com.luoruiyong.fileshare.profile.model;

import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.ArrayList;
import java.util.List;

public class DownloadFileModel {

    private OnScanDownloadFileFinishListener mListener;

    public void setOnScanDownloadFileFinishListener(OnScanDownloadFileFinishListener listener) {
        this.mListener = listener;
    }

    public void deleteLocalFile(String url) {

    }

    public void startScanDownloadFiles() {
        // IO操作

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
                for (int i = 0; i < 10; i++) {
                    ShareFile file = new ShareFile();
                    file.setName("Android 从入门到放弃 " + (i + 1));
                    file.setSize((int)(Math.random() * 100000));
                    list.add(file);
                }
                if (mListener != null) {
                    mListener.onScanDownloadFileFinish(list);
                }

            }
        }).start();
    }

    public interface OnScanDownloadFileFinishListener {
        void onScanDownloadFileFinish(List<ShareFile> list);
    }

}
