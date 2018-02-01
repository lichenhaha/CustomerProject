package com.chenli.media;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.chenli.yuvlib.YUVlib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,View.OnClickListener{

    private static final String TAG = "chenli";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private static String currentPath;

    public CameraPreview(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void openCamera(SurfaceHolder holder) throws IOException {
        releaseCamera();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);//启动后置摄像头
        parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);//只是设置预览方向，拍照后的图片还是横向的。
        parameters.setRotation(90);//这句话设置了保存的图片才是旋转后的。

        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewFormat(ImageFormat.NV21);

        parameters.setPictureSize(640,480);
        parameters.setPreviewSize(640,480);
        //这两个属性 如果这两个属性设置的和真实手机的不一样时，就会报错

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
            //竖屏
            parameters.set("orientation","portrait");
            parameters.set("rotation",90);
        }else {
            parameters.set("orientation","landscape");
            mCamera.setDisplayOrientation(0);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.autoFocus(autoFocusCallback);
        mCamera.setParameters(parameters);
        mCamera.setPreviewDisplay(holder);
        mCamera.startPreview();

    }

    private void releaseCamera() {
        if (mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            openCamera(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    public void takePicture(){
        mCamera.takePicture(null,null,mPicture);
    }

    private boolean flag ;

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (!flag && success){
                flag  = true;
                mCamera.setOneShotPreviewCallback(mCallback);
            }
        }
    };

    private Camera.PreviewCallback mCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            String stringFromJNI = YUVlib.getStringFromJNI();
            Log.e(TAG, "onPreviewFrame: " + stringFromJNI);

            //int length = data.length;
            //Log.e(TAG, "length  " + length + " width = " + mCamera.getParameters().getPreviewSize().width + " height = " + mCamera.getParameters().getPreviewSize().height);

            byte[] bytes = YUVlib.getInstance().NV21ToARGB(data, data.length, mCamera.getParameters().getPreviewSize().width, mCamera.getParameters().getPreviewSize().height);



//            try {
//                File takePicture = getTakePicture();
//                FileOutputStream fos = new FileOutputStream(takePicture);
//                //Log.e(TAG, "onPreviewFrame: " + bytes.length);
//                fos.write(data);
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            //galleryAddPic();


            //YuvImage yuvImage = new YuvImage(data,ImageFormat.NV21,mCamera.getParameters().getPreviewSize().width,mCamera.getParameters().getPreviewSize().height,null);
//            try {
//                File takePicture = getTakePicture();
//                FileOutputStream fos = new FileOutputStream(takePicture);
//                yuvImage.compressToJpeg(new Rect(0,0,mCamera.getParameters().getPreviewSize().width,mCamera.getParameters().getPreviewSize().height),100,fos);
//                fos.flush();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    };


    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

    public static File getTakePicture() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMDD_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp + "_";
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",file);
        currentPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                File picture = getTakePicture();
                FileOutputStream fos = new FileOutputStream(picture);
                fos.write(data);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public Camera getCamera() {
        return mCamera;
    }


    @Override
    public void onClick(View v) {
        if (mCamera!= null){
            mCamera.cancelAutoFocus();
            mCamera.autoFocus(autoFocusCallback);
        }
    }
}
