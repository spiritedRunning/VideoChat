package com.example.videochat;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * Created by Zach on 2021/6/10 21:43
 */
public class SocketLive {
    private static final String TAG = "SocketLive";

    private WebSocket webSocket;
    private WebSocketServer webSocketServer;
    private int port;

    private SocketCallback socketCallBack;

    public SocketLive(SocketCallback socketCallBack, int port) {
        this.socketCallBack = socketCallBack;
        this.port = port;
    }

    public void start() {
        InetSocketAddress serverHost = new InetSocketAddress("192.168.1.103", port);
        webSocketServer = new MyWebSocketServer(serverHost);
        webSocketServer.start();

    }

    public void close() {
        try {
            if (webSocket != null && webSocketServer != null) {
                Log.i(TAG, "stop websocket");
                webSocket.close();
                webSocketServer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] bytes) {
        if (webSocket != null && webSocket.isOpen()) {
//            Log.e(TAG, "send data: " + DataUtil.byte2hex(bytes));
            webSocket.send(bytes);
        }
    }

    public class MyWebSocketServer extends WebSocketServer {
        MyWebSocketServer(InetSocketAddress host) {
            super(host);
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            SocketLive.this.webSocket = conn;
            Log.i(TAG, "onOpen, client addr: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            Log.i(TAG, "websocket close, reason: " + reason);
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // http://www.websocket-test.com/  文本发送测试
            Log.d(TAG, "onMessage message: " + message);
        }

        @Override
        public void onMessage(WebSocket conn, ByteBuffer message) {
            Log.d(TAG, "onMessage data len = " + message.remaining());

            byte[] buf = new byte[message.remaining()];
            message.get(buf);
            socketCallBack.callBack(buf);
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            Log.i(TAG, "websocket onError: " + ex);
        }

        @Override
        public void onStart() {
            Log.i(TAG, "websocket start succ!");
        }
    }

    public interface SocketCallback {
        void callBack(byte[] data);
    }
}
