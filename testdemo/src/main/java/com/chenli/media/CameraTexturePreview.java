package com.chenli.media;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;

import java.io.IOException;

/**
 * Created by Administrator on 2018/2/9.
 */

public class CameraTexturePreview extends TextureView implements TextureView.SurfaceTextureListener {
    private Context context;
    private Camera mCamera;
    private boolean isOpen ;

    public CameraTexturePreview(Context context) {
        super(context);
        this.context = context;
        setSurfaceTextureListener(this);
    }

    private void openCamera(SurfaceTexture surfaceTexture) {
        releaseCamera();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        try {
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            isOpen = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void releaseCamera() {
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            isOpen = false;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCamera(surface);
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        releaseCamera();
        return true;
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public boolean isPreviewing(){
        return isOpen;
    }

    public Camera getCamera(){
        return mCamera;
    }


}
