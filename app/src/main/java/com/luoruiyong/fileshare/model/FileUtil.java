package com.luoruiyong.fileshare.model;

import android.os.Environment;

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
