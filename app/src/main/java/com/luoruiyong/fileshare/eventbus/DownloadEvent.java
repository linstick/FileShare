package com.luoruiyong.fileshare.eventbus;

public class DownloadEvent {
    private String mIpAddress;
    private String mFileName;
    private boolean mIsSuccess;

    public String getIpAddress() {
        return mIpAddress;
    }

    public void setIpAddress(String mIpAddress) {
        this.mIpAddress = mIpAddress;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public void setSuccess(boolean mIsSuccess) {
        this.mIsSuccess = mIsSuccess;
    }
}
