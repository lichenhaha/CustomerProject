package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/1/18.
 */

public class GlSurface2 extends GLSurfaceView implements GLSurfaceView.Renderer {

    private Triangle3 mTriangle3;
    private float[] mProjectMatrix=new float[16];
    private float[] mViewMatrix=new float[16];
    private Circle mCircle;
    private Cube mCube;

    public GlSurface2(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//渲染模式为主动渲染
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        //mTriangle3 = new Triangle3(getContext());
        //mCircle = new Circle(getContext());
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mCube = new Cube(getContext());

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float ratio = (float) (width*1.0/height);
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio, -1, 1, 3, 20);
        Matrix.setLookAtM(mViewMatrix,0, 5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mCube.mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        //mTriangle3.drawSelf();
        //mCircle.drawSelf();
        mCube.drawSelf();
    }
}
