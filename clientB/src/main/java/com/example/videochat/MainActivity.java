package com.example.videochat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SocketLive.SocketCallback {

    private SurfaceView removeSurfaceView;
    private LocalSurfaceView localSurfaceView;
    private DecodecPlayerLiveH265 decodecPlayerLiveH265;
    private Surface surface;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        initView();
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }

    private void initView() {
        removeSurfaceView = findViewById(R.id.remoteSurfaceView);
        localSurfaceView = findViewById(R.id.localSurfaceView);
        removeSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                surface = holder.getSurface();
                decodecPlayerLiveH265 = new DecodecPlayerLiveH265();
                decodecPlayerLiveH265.initDecoder(surface);

            }
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            }
        });
    }

    public void OnConnect(View view) {
        localSurfaceView.startCapture(this);
    }

    @Override
    public void callBack(byte[] data) {
        if (decodecPlayerLiveH265 != null) {
            decodecPlayerLiveH265.callback(data);
        }
    }

    public void OnDisconnect(View view) {
        localSurfaceView.stopLive();
    }
}