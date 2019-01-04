package com.luoruiyong.fileshare.bean;

import java.io.Serializable;

public class Host implements Serializable{

    private String mName;
    private String mIpAddress;
    private int mFileCount;

    public Host() {
    }

    public String getIpAddress() {
        return mIpAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.mIpAddress = ipAddress;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getFileCount() {
        return mFileCount;
    }

    public void setFileCount(int fileCount) {
        this.mFileCount = fileCount;
    }

    @Override
    public String toString() {
        return "Host{" +
                "mName='" + mName + '\'' +
                ", mIpAddress='" + mIpAddress + '\'' +
                ", mFileCount=" + mFileCount +
                '}';
    }
}
