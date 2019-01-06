package com.luoruiyong.fileshare.bean;

public class ShareFile {

    public final static int STATUS_DOWNLOADING = 1;
    public final static int STATUS_DOWNLOADED = 2;
    public final static int STATUS_SHARED = 3;

    private String mName;
    private long mSize;
    private String mUrl;
    private int mStatus;

    public ShareFile() {
    }

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

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    @Override
    public String toString() {
        return "ShareFile{" +
                "mName='" + mName + '\'' +
                ", mSize=" + mSize +
                ", mUrl='" + mUrl + '\'' +
                ", mStatus=" + mStatus +
                '}';
    }
}
