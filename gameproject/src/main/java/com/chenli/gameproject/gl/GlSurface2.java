package com.chenli.gameproject.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.mtp.MtpEvent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.gameproject.R;

import java.util.Arrays;

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
    private AirRender airRender;

    public GlSurface2(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//渲染模式为主动渲染
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
        //mTriangle3 = new Triangle3(getContext());
        //mCircle = new Circle(getContext());
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //mCube = new Cube(getContext());

//        airRender = new AirRender(getContext());

        Table table = new Table();
        Mallet mallet = new Mallet();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        Matrix.perspectiveM(airRender.projectionMatrix,0,45,width*1.0f/height,1f,10f);
        Matrix.setIdentityM(airRender.modelMatrix,0);//初始化矩阵为单位矩阵
        Matrix.translateM(airRender.modelMatrix,0,0,0,-3f);
        Matrix.rotateM(airRender.modelMatrix,0,-60,1f,0f,0f);
        float[] temp = new float[16];
        Matrix.multiplyMM(temp,0,airRender.projectionMatrix,0,airRender.modelMatrix,0);
        System.arraycopy(temp,0,airRender.projectionMatrix,0,temp.length);

//        float ratio = width > height?width*1.0f/height:height*1.0f/width;
//        if (width>height){
//            Matrix.orthoM(airRender.projectionMatrix,0,-ratio,ratio,-1f,1f,-1f,1f);
//        }else {
//            Matrix.orthoM(airRender.projectionMatrix,0,-1f,1f,-ratio,ratio,-1f,1f);
//        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        airRender.drawSelf();

    }
}
