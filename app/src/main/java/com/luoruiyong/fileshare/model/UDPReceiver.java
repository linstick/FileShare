package com.luoruiyong.fileshare.model;


import android.support.v7.widget.DialogTitle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoruiyong.fileshare.Config;
import com.luoruiyong.fileshare.bean.Host;
import com.luoruiyong.fileshare.bean.PacketContent;
import com.luoruiyong.fileshare.bean.ShareFile;
import com.luoruiyong.fileshare.profile.ProfileActivity;

import org.greenrobot.eventbus.EventBus;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.List;

import static com.luoruiyong.fileshare.eventbus.DataChangeEvent.HOST_DATA_CHANGE;
import static com.luoruiyong.fileshare.eventbus.DataChangeEvent.OTHER_SHARE_FILE_CHANGE;

/**
 * 应用启动时便启动，并持续监听
 * 回包条件，当前用户的共享文件数量大于0
 * 1. 处理主机查询广播数据
 * 2. 处理某一台主机查询共享文件列表的单播数据
 */
public class UDPReceiver implements Runnable {
    private static final String TAG = "UDPReceiver";

    private static UDPReceiver mReceiver;
    private boolean mIsStart = false;
    private boolean mIsHostResponseTimeOut = false;
    private boolean mIsFileResponseTimeOut = false;
    private MulticastSocket mReceiveSocket = null;
    private Gson mGson = new Gson();

    private UDPReceiver() {
    }

    public static UDPReceiver getInstance() {
        if (mReceiver == null) {
            synchronized (UDPReceiver.class) {
                if (mReceiver == null) {
                    mReceiver = new UDPReceiver();
                }
            }
        }
        return mReceiver;
    }

    private void parsePacketAndResponseIfNeed(DatagramPacket packet) {
        String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
        Log.d(TAG, "receive data: " + data);
        PacketContent content = mGson.fromJson(data, PacketContent.class);
        switch (content.getFunction()) {
            case PacketContent.FUNCTION_REQUEST_SEARCH_SHARE_HOST:
                handleRequestShareHost(packet.getAddress());
                break;
            case PacketContent.FUNCTION_REQUEST_SEARCH_SHARE_FILES:
                handleRequestShareFiles(packet.getAddress());
                break;
            case PacketContent.FUNCTION_RESPONSE_SEARCH_SHARE_HOST:
                handleResponseShareHost(packet, content.getMessage());
                break;
            case PacketContent.FUNCTION_RESPONSE_SEARCH_SHARE_FILES:
                handleResponseShareFiles(content.getMessage());
                break;
        }
    }

    @Override
    public void run() {
        if (mIsStart) {
            return;
        }
        try {
            mReceiveSocket = new MulticastSocket(Config.UDP_PORT);
            mReceiveSocket.joinGroup(InetAddress.getByName(Config.BROADCAST_IP_ADDRESS));
            mIsStart = true;
            Log.d(TAG, "start receive");
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(new byte[Config.UDP_PACKET_SIZE], Config.UDP_PACKET_SIZE);
                mReceiveSocket.receive(receivePacket);
                parsePacketAndResponseIfNeed(receivePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRequestShareHost(InetAddress targetAddress) {
        List<ShareFile> list = DataSource.getMySharedFileList();
        if (list != null && list.size() > 0) {
            // 有共享文件，回包
            Host host = new Host();
            host.setName("共享主机" + (int)(Math.random() * 100));
            host.setFileCount(list.size());
            // 构造回包
            PacketContent packetContent = new PacketContent();
            packetContent.setFunction(PacketContent.FUNCTION_RESPONSE_SEARCH_SHARE_HOST);
            packetContent.setMessage(mGson.toJson(host));
            UDPUtil.sendNormalPacket(targetAddress, mGson.toJson(packetContent));
            Log.d(TAG, "response share host: " + host);
        } else {
            Log.d(TAG, "no share file, not response");
        }
        // 无共享文件，不回包
    }

    private void handleRequestShareFiles(InetAddress targetAddress) {
        // 当接收到这个请求的时候，无论有没有共享文件，都回个包，因为这是个单播包
        List<ShareFile> list = DataSource.getMySharedFileList();
        // 构造回包
        PacketContent packetContent = new PacketContent();
        packetContent.setFunction(PacketContent.FUNCTION_RESPONSE_SEARCH_SHARE_FILES);
        packetContent.setMessage(mGson.toJson(list));
        UDPUtil.sendNormalPacket(targetAddress, mGson.toJson(packetContent));
        Log.d(TAG, "response share host: " + list);
    }

    private void handleResponseShareHost(DatagramPacket packet, String content) {
        // 回包的内容是一个共享主机
        if (!mIsHostResponseTimeOut) {
            // 解析
            Host host = mGson.fromJson(content, Host.class);
            host.setIpAddress(packet.getAddress().getHostAddress());
            // 添加到列表
            DataSource.addHost(host);
            // 通知UI更新
            EventBus.getDefault().post(HOST_DATA_CHANGE);
        }
    }

    private void handleResponseShareFiles(String content) {
        // 回传的数据是一个文件列表
        if (!mIsFileResponseTimeOut) {
            // 解析
            List<ShareFile> list = mGson.fromJson(content, new TypeToken<List<ShareFile>>(){}.getType());
            // 更新全局数据
            DataSource.updateOtherShareFileList(list);
            // 通知UI更新
            EventBus.getDefault().post(OTHER_SHARE_FILE_CHANGE);
        }

    }

    public boolean isHostResponseTimeOut() {
        return mIsHostResponseTimeOut;
    }

    public void setHostResponseTimeOut(boolean isTimeOut) {
        this.mIsHostResponseTimeOut = isTimeOut;
    }

    public boolean isFileResponseTimeOut() {
        return mIsFileResponseTimeOut;
    }

    public void setFileResponseTimeOut(boolean isTimeOut) {
        this.mIsFileResponseTimeOut = isTimeOut;
    }
}
