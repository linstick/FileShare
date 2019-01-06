package com.luoruiyong.fileshare.model;

import android.util.Log;

import com.luoruiyong.fileshare.bean.ShareFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private static final String TAG = "DataSource";
    private static List<ShareFile> sDownloadShareFileList = new ArrayList<>();
    private static List<ShareFile> sMySharedFileList = new ArrayList<>();

    public static void preload() {
        sDownloadShareFileList = getDownloadShareFileList();
    }

    public static List<ShareFile> getDownloadShareFileList() {
        if (sDownloadShareFileList.size() == 0) {
            File[] files = FileUtil.scanFiles(FileUtil.DEFAULT_DOWNLOAD_PATH);
            for (File file : files) {
                ShareFile shareFile = new ShareFile();
                shareFile.setName(file.getName());
                shareFile.setSize(FileUtil.getFileSize(file));
                shareFile.setUrl(file.getAbsolutePath());
                shareFile.setStatus(ShareFile.STATUS_DOWNLOADED);
                sDownloadShareFileList.add(shareFile);
            }
        }
        return sDownloadShareFileList;
    }

    public static void addDownloadShareFile(ShareFile shareFile) {
        sDownloadShareFileList.add(shareFile);
    }

    public static void deleteDownloadShareFile(int position) {
        if (position < 0 || position > sDownloadShareFileList.size()) {
            return;
        }
        ShareFile shareFile = sDownloadShareFileList.get(position);
        sDownloadShareFileList.remove(position);
        File file = new File(shareFile.getUrl());
        if (file.exists()) {
            file.delete();
        }
    }

    public static List<ShareFile> getMySharedFileList() {
        return sMySharedFileList;
    }

    public static void addMySharedFile(ShareFile shareFile) {
        sMySharedFileList.add(shareFile);
    }

    public static void removeMySharedFile(int position) {
        if (position >= 0 && position < sMySharedFileList.size()) {
            sMySharedFileList.remove(position);
        }
    }
}