package com.chenli.gameproject;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chenli.gameproject.demo.CameraPreviewByGL;
import com.chenli.gameproject.demo.TexturePicture;
import com.chenli.gameproject.demo.TexturePicture1;

/**
 * Created by Administrator on 2018/1/16.
 */

public class SurfaceViewActivity extends AppCompatActivity {

    //private MyGLSurfaceView surfaceView;
//    private TexturePicture1 surfaceView;
    private CameraPreviewByGL surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new CameraPreviewByGL(this);
        setContentView(surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }
}
