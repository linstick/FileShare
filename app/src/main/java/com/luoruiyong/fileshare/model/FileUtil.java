package com.luoruiyong.fileshare.model;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;

import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.utils.SourceUtil;

import java.io.File;
import java.io.FileInputStream;

public class FileUtil {

    public static final String DEFAULT_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath() + "/sharefile/download/";

    public static File[] scanFiles(String path) {
        File directory = new File(path);
        if (!directory.isDirectory()) {
            return null;
        }
        File[] files = directory.listFiles();
        return files;
    }

    public static ShareFile localFileToShareFile(String url) {
        File file = new File(url);
        if (file == null || !file.exists()) {
            return null;
        }
        ShareFile shareFile = new ShareFile();
        shareFile.setName(file.getName());
        shareFile.setSize(getFileSize(file));
        shareFile.setUrl(file.getAbsolutePath());
        shareFile.setStatus(ShareFile.STATUS_SHARED);
        return shareFile;
    }

    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SourceUtil.close(fis);
        }
        return size;
    }
}
