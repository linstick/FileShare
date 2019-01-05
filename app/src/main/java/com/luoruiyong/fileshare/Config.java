package com.luoruiyong.fileshare;

public class Config {

    // TCP端口号，当用户选定某一台共享主机时，建立TCP连接来进行通信
    public final static int TCP_PORT = 10240;
    // UDP端口号，用户查询当前内网中有哪些共享主机时使用，或直接查询某一个文件时广播使用
    public final static int UDP_PORT = 10720;
}
