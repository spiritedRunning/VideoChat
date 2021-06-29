package com.example.videochat;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Zach on 2021/6/25 20:47
 */
public class LocalSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera.Size size;
    private Camera mCamera;
    private EncodePushLiveH265 encodePushLiveH265;

    private byte[] buffer;

    public LocalSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview();
    }

    private void startPreview() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters parameters = mCamera.getParameters();
        size = parameters.getPreviewSize();

        try {
            mCamera.setPreviewDisplay(getHolder());
            mCamera.setDisplayOrientation(90);
            buffer = new byte[size.width * size.height * 3 / 2];
            mCamera.addCallbackBuffer(buffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        try {
            if (encodePushLiveH265 != null) {
                encodePushLiveH265.encodeFrame(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCamera.addCallbackBuffer(bytes);
    }


    public void startCapture(SocketLive.SocketCallback socketCallback) {
        encodePushLiveH265 = new EncodePushLiveH265(socketCallback, size.width, size.height);
        encodePushLiveH265.startLive();
    }

    public void stopLive() {
        encodePushLiveH265.stopLive();
    }
}
