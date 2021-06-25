package com.example.videochat;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * Created by Zach on 2021/6/10 21:43
 */
public class SocketLive {
    private static final String TAG = "SocketLive";

    private SocketCallback socketCallback;
    private MyWebSocketClient webSocketClient;

    private int port;

    public SocketLive(SocketCallback socketCallback, int port) {
        this.socketCallback = socketCallback;
        this.port = port;
    }

    public void start() {
        try {
            URI url = new URI("ws://192.168.1.103:" + port);
            webSocketClient = new MyWebSocketClient(url);
            webSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
        }
    }


    public void sendData(byte[] bytes) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(bytes);
        }
    }



    private class MyWebSocketClient extends WebSocketClient {

        public MyWebSocketClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            Log.i(TAG, "open socket: " + serverHandshake.getHttpStatusMessage());
        }

        @Override
        public void onMessage(String message) {
            Log.i(TAG, "onMessage: " + message);
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            Log.i(TAG, "onMessage len: " + bytes.remaining());
            byte[] buf = new byte[bytes.remaining()];
            bytes.get(buf);

            socketCallback.callBack(buf);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.e(TAG, "onClose: " + reason);
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    }

    public interface SocketCallback {
        void callBack(byte[] data);
    }
}
