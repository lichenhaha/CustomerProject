package com.chenli.gameproject.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.chenli.commenlib.util.gameutil.ShaderUtils;
import com.chenli.gameproject.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/2/7.
 */

public class TexturePicture1 extends GLSurfaceView implements GLSurfaceView.Renderer {

    float[] mProjectMatrix = new float[16];
    float[] mViewMatrix = new float[16];
    float[] mMVPMatrix = new float[16];


    private final float[] sPos = {
            -0.5f, 0.5f,
            0.5f,  0.5f  ,
            0.5f, -0.5f  ,
            -0.5f, -0.5f
    };

    private final float[] sCoord = {
            0.0f, 0.0f,
            1.0f, 0f,
            1.0f, 1f,
            0f, 1f
    };
    private final Bitmap bitmap;
    private FloatBuffer vertex;
    private FloatBuffer index;
    private int program;
    private int[] loadTexture;
    private int attribPosition;
    private int attribTexCoord;
    private int uniformTexture;
    private int vMatrix;


    public TexturePicture1(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        loadVertex();
        initShader();
    }

    private void loadVertex() {
        vertex = ByteBuffer.allocateDirect(sPos.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertex.put(sPos).position(0);
        index = ByteBuffer.allocateDirect(sCoord.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        index.put(sCoord).position(0);
    }

    private void initShader() {
        String vertexSource = ShaderUtils.readTextFileFromResource(getContext(), R.raw.vertexshader);
        String fragmentSource = ShaderUtils.readTextFileFromResource(getContext(),R.raw.fragmentshader);

        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        GLES20.glUseProgram(program);

        attribPosition = GLES20.glGetAttribLocation(program, "a_position");
        attribTexCoord = GLES20.glGetAttribLocation(program, "a_texCoord");
        uniformTexture = GLES20.glGetUniformLocation(program, "u_samplerTexture");
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
        loadTexture = ShaderUtils.loadTexture(bitmap);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,loadTexture[0]);
        GLES20.glUniform1i(uniformTexture,0);

        GLES20.glVertexAttribPointer(attribPosition,2,GLES20.GL_FLOAT,
                false,0,vertex);
        GLES20.glEnableVertexAttribArray(attribPosition);
        GLES20.glVertexAttribPointer(attribTexCoord,2,GLES20.GL_FLOAT,
                false,0,index);
        GLES20.glEnableVertexAttribArray(attribTexCoord);
    }

    private void loadTexture() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
        loadTexture = ShaderUtils.loadTexture(bitmap);
        if (bitmap!= null){
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        float sWH = w*1.0f/h;
//        float sWidthHeight = width*1.0f/height;
//        if (width > height){
//            if (sWH > sWidthHeight){
//                Matrix.orthoM(mProjectMatrix,0,-sWH*sWidthHeight,sWidthHeight*sWH,-1,1,3,7);
//            }else {
//                Matrix.orthoM(mProjectMatrix,0,sWidthHeight/sWH,sWidthHeight/sWH,-1,1,3,7);
//            }
//        }else {
//            if(sWH>sWidthHeight){
//                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/sWidthHeight*sWH, 1/sWidthHeight*sWH,3, 7);
//            }else{
//                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH/sWidthHeight, sWH/sWidthHeight,3, 7);
//            }
//        }
//        //设置相机位置
//        Matrix.setLookAtM(mViewMatrix,0,0,0,7.0f,0,0,0,0,1.0f,0.0f);
//        //计算变换矩阵
//        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);

        float aspectRatio = width>height?width*1.0f/height:height*1.0f/width;
        if (width > height){
            Matrix.orthoM(mProjectMatrix,0,-aspectRatio,aspectRatio,-1,1,-1,1);
        }else {
            Matrix.orthoM(mProjectMatrix,0,-1,1,-aspectRatio,aspectRatio,-1,1);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(vMatrix,1,false,mProjectMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

    }
}
