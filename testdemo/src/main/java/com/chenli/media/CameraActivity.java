package com.chenli.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;
import com.chenli.yuvlib.YUVlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/30.
 */

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.button3)
    Button button3;
    @Bind(R.id.framelayout)
    FrameLayout framelayout;
    private CameraPreview cameraPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_layout);
        ButterKnife.bind(this);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1){
            cameraPreview = new CameraPreview(this);
            framelayout.removeAllViews();
            framelayout.addView(cameraPreview);
            framelayout.setOnClickListener(cameraPreview);
        }else if (v.getId() == R.id.button2){
            cameraPreview.takePicture();
        }else if (v.getId() == R.id.button3){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img4);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            File file = new File(Environment.getExternalStorageDirectory()+ "/test.yuv");
            try {
                OutputStream os = new FileOutputStream(file);
                ByteBuffer buffer = ByteBuffer.allocate(width*height*4);
                bitmap.copyPixelsToBuffer(buffer);
                byte[] yuv = new byte[width*height*3/2];
                YUVlib.ARGBToI420(buffer.array(),yuv,width,height);
                os.write(yuv);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
