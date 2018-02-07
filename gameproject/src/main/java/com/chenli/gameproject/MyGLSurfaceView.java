package com.chenli.gameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;


import com.chenli.commenlib.util.gameutil.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/1/16.
 */

public class MyGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private Context mContext;

    private float[] projMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] mViewPjMatrix;//总变换矩阵
    private float[] matrixs = new float[16];

    private float[] quadVertex = new float[]{
            -0.5f , 0.5f, 0.0f, // Position 0
            0f     , 1.0f,       // TexCoord 0

            -0.5f , -0.5f, 0.0f,//Position 1
            0f,0f,                //TexCoord 1

            0.5f, -0.5f ,0.0f,  //Position 2
            1.0f , 0f ,          //TexCoord 2

            0.5f, 0.5f ,0f ,    //Position 3
            1.0f, 1.0f ,        //TexCoord 3
    };

    private short[] quadIndex = new short[]{
            (short) 0,
            (short)1,
            (short)2,

            (short)2,
            (short)3,
            (short)0,
    };
    private FloatBuffer vertex;
    private ShortBuffer index;
    private int program;
    private int[] loadTexture;
    private int attribPosition;
    private int attribTexCoord;
    private int uniformTexture;

    public MyGLSurfaceView(Context context) {
        this(context, null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        loadVertex();
        initShader();
        loadTexture();


    }

    private void loadVertex() {
        vertex = ByteBuffer.allocateDirect(quadVertex.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertex.put(quadVertex).position(0);
        index = ByteBuffer.allocateDirect(quadIndex.length*2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        index.put(quadIndex).position(0);
    }

    private void initShader() {
        String vertexSource = ShaderUtils.readTextFileFromResource(mContext, R.raw.vertexshader);
        String fragmentSource = ShaderUtils.readTextFileFromResource(mContext,R.raw.fragmentshader);
        program = ShaderUtils.createProgram(vertexSource, fragmentSource);
        attribPosition = GLES20.glGetAttribLocation(program, "a_position");
        attribTexCoord = GLES20.glGetAttribLocation(program, "a_texCoord");
        uniformTexture = GLES20.glGetUniformLocation(program, "u_samplerTexture");
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(attribPosition);
        GLES20.glEnableVertexAttribArray(attribTexCoord);
        // Set the sampler to texture unit 0
        GLES20.glUniform1i(uniformTexture,0);
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

//        float ratio = width*1.0f/height;
//        Matrix.frustumM(projMatrix,0,-ratio,ratio,-1,1,1,10);
//        //摄像机坐标设置
//        Matrix.setLookAtM(viewMatrix,0,0,0,3,0,0,0,0.0f,1.0f,0.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,loadTexture[0]);
        vertex.position(0);
        GLES20.glVertexAttribPointer(attribPosition,3,GLES20.GL_FLOAT,
                false,5*4,vertex);
        vertex.position(3);
        GLES20.glVertexAttribPointer(attribTexCoord,2,GLES20.GL_FLOAT,
                false,20,vertex);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,6,GLES20.GL_UNSIGNED_SHORT,index);
    }
}
