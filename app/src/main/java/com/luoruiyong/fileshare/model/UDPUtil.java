package com.luoruiyong.fileshare.model;

import com.google.gson.Gson;
import com.luoruiyong.fileshare.Config;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPUtil {

    public static void startUDPReceiver() {
        new Thread(UDPReceiver.getInstance()).start();
    }

    public static void sendBroadcastPacket(String content) {
        sendNormalPacket(Config.BROADCAST_IP_ADDRESS, content);
    }

    public static void sendNormalPacket(String ipAddress, String content) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            sendNormalPacket(address, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendNormalPacket(InetAddress address, String content) {
        DatagramPacket packet = new DatagramPacket(content.getBytes(), content.getBytes().length, address, Config.UDP_PORT);
        sendPacket(packet);
    }

    private static void sendPacket(final DatagramPacket packet) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket();
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        }).start();
    }
}
