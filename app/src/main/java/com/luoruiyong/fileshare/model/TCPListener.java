package com.luoruiyong.fileshare.model;

import android.util.Log;

import com.luoruiyong.fileshare.Config;
import com.luoruiyong.fileshare.utils.SourceUtil;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 充当服务端的文件下载请求监听器
 */
public class TCPListener implements Runnable {
    private static final String TAG = "TCPListener";

    private static volatile TCPListener sTCPListener;
    private boolean mIsStart = false;
    private ServerSocket mSocket = null;

    private TCPListener(){}

    public static TCPListener getInstance() {
        if (sTCPListener == null) {
            synchronized (TCPListener.class) {
                if (sTCPListener == null) {
                    sTCPListener = new TCPListener();
                }
            }
        }
        return sTCPListener;
    }

    @Override
    public void run() {
        if (mIsStart) {
            return;
        }
        try {
            mSocket = new ServerSocket(Config.TCP_PORT);
            Log.d(TAG, "server start to listen to tcp port");
            while (true) {
                // 监听特定端口，直到有客户端连接
                Socket clientSocket = mSocket.accept();
                Log.d(TAG, "server has receive a request");
                handleClientSocket(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSocket = null;
                mIsStart = false;
            }
        }
    }

    static class ServerDownloadTask implements Runnable {

        private Socket mClientSocket;
        public ServerDownloadTask(Socket socket) {
            this.mClientSocket = socket;
        }

        @Override
        public void run() {
            DataInputStream dis = null;
            InputStream is = null;
            OutputStream os = null;
            try {
                dis = new DataInputStream(mClientSocket.getInputStream());
                // 收到的request就是文件url
                String url = dis.readUTF();
                Log.d(TAG, "server get url: " + url);
                is = new FileInputStream(url);
                os = mClientSocket.getOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                }
                os.flush();
                Log.d(TAG, "server send file success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "server send file error");
            } finally {
                SourceUtil.close(dis);
                SourceUtil.close(is);
                SourceUtil.close(os);
                if (mClientSocket != null) {
                    try {
                        mClientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleClientSocket(Socket clientSocket) {
        new Thread(new ServerDownloadTask(clientSocket)).start();
    }
}
