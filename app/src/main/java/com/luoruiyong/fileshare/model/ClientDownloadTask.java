package com.luoruiyong.fileshare.model;

import android.util.Log;

import com.luoruiyong.fileshare.Config;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.eventbus.DataChangeEvent;
import com.luoruiyong.fileshare.eventbus.DownloadEvent;
import com.luoruiyong.fileshare.utils.SourceUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientDownloadTask implements Runnable {
    private static final String TAG = "ClientDownloadTask";


    private String mHost;
    private String mUrl;
    private String mName;

    public ClientDownloadTask(String host, String url, String name) {
        this.mHost = host;
        this.mUrl = url;
        this.mName = name;
    }

    @Override
    public void run() {
        Socket socket = null;
        DataOutputStream dos = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            socket = new Socket(mHost, Config.TCP_PORT);
            // 发送文件路径
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(mUrl);

            // 接收文件
            is = socket.getInputStream();
            File f = new File(FileUtil.DEFAULT_DOWNLOAD_PATH);
            if (!f.exists()) {
                f.mkdirs();
            }
            os = new FileOutputStream(FileUtil.DEFAULT_DOWNLOAD_PATH + mName);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            os.flush();
            handleDownloadSuccess();
            Log.d(TAG, "download finish");
        } catch (IOException e) {
            handleDownloadFail();
            Log.d(TAG, "error: " + e.getMessage());
        } finally {
            SourceUtil.close(dos);
            SourceUtil.close(is);
            SourceUtil.close(is);
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleDownloadSuccess() {
        ShareFile shareFile = new ShareFile();
        shareFile.setName(mName);
        shareFile.setUrl(FileUtil.DEFAULT_DOWNLOAD_PATH + mName);
        shareFile.setSize(FileUtil.getFileSize(new File(shareFile.getUrl())));
        shareFile.setStatus(ShareFile.STATUS_DOWNLOADED);
        DataSource.addDownloadShareFile(shareFile);
        // 构造事件，通知UI更新
        DownloadEvent event = new DownloadEvent();
        event.setFileName(mName);
        event.setIpAddress(mHost);
        event.setSuccess(true);
        EventBus.getDefault().post(event);
    }

    private void handleDownloadFail() {
        DownloadEvent event = new DownloadEvent();
        event.setFileName(mName);
        event.setIpAddress(mHost);
        event.setSuccess(false);
        EventBus.getDefault().post(event);
    }
}
