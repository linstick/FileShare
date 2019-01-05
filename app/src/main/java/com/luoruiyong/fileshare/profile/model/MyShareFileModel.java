package com.luoruiyong.fileshare.profile.model;

import android.widget.LinearLayout;

import com.luoruiyong.fileshare.bean.ShareFile;

import java.util.ArrayList;
import java.util.List;

public class MyShareFileModel {

    private OnScanMyShareFileFinishListener mListener;
    public void setOnScanMyShareFileFinishListener(OnScanMyShareFileFinishListener listener) {
        this.mListener = listener;
    }

    public void startScanShareFiles() {
        // IO操作
        // 扫描自己共享的文件

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
                    file.setDownload(true);
                    list.add(file);
                }
                if (mListener != null) {
                    mListener.onScanMyShareFileFinish(list);
                }

            }
        }).start();
    }

    public void removeShareFile(ShareFile shareFile) {

    }

    public void addShareFile() {

    }

    public interface OnScanMyShareFileFinishListener {
        void onScanMyShareFileFinish(List<ShareFile> list);
    }
}
