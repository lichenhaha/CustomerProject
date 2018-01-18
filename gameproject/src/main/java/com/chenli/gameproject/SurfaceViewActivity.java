package com.chenli.gameproject;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.chenli.gameproject.gl.GlSurface;


/**
 * Created by Administrator on 2018/1/16.
 */

public class SurfaceViewActivity extends AppCompatActivity {
    private GlSurface myGLSurfaceView;

//    private MyGLSurfaceView myGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //MySurfaceView surfaceView = new MySurfaceView(this);
        //setContentView(surfaceView);

        //myGLSurfaceView = new MyGLSurfaceView(this);

        myGLSurfaceView = new GlSurface(this);
        setContentView(myGLSurfaceView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        myGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myGLSurfaceView.onPause();
    }
}
