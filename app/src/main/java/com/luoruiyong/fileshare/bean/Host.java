package com.luoruiyong.fileshare.bean;

public class Host {

    private String mName;
    private String mIpAddress;

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


    @Override
    public String toString() {
        return "Host{" +
                "mName='" + mName + '\'' +
                ", mIp='" + mIpAddress + '\'' +
                '}';
    }
}
