package com.luoruiyong.fileshare.bean;

public class ShareFile {

    private String mName;
    private long mSize;
    private boolean mIsDownload;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        this.mSize = size;
    }

    public boolean isDownload() {
        return mIsDownload;
    }

    public void setDownload(boolean download) {
        mIsDownload = download;
    }

    @Override
    public String toString() {
        return "ShareFile{" +
                "mName='" + mName + '\'' +
                ", mSize=" + mSize +
                ", mIsDownload=" + mIsDownload +
                '}';
    }
}
