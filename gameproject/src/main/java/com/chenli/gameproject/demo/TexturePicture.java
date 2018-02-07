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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/2/6.
 */

public class TexturePicture extends GLSurfaceView implements GLSurfaceView.Renderer {

    //矩形顶点坐标
    static float squareCoords[] = { //以三角形扇的形式绘制
            -0.5f,  0.5f , 0 , 0,   // top left
            0.5f,  0.5f  , 1,  0,// top right
            0.5f, -0.5f  , 1 , 1,// bottom right
            -0.5f, -0.5f , 0 , 1 };  // bottom left

    private FloatBuffer vertexBuffer;

    float[] projectMatrix = new float[16];
    private int vMatrix;
    private int v_color;
    private final Bitmap bitmap;

    public TexturePicture(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img4);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,0.5f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        loadVertex();
        initShader();
        //loadTexture();
    }

    private void loadVertex() {
        vertexBuffer = ByteBuffer.allocateDirect(squareCoords.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(squareCoords).position(0);
    }

    private void initShader() {
        String vertexSource = ShaderUtils.readTextFileFromResource(getContext(), R.raw.vertexshader1);
        String fragmentSource = ShaderUtils.readTextFileFromResource(getContext(),R.raw.fragmentshader1);

        int program = ShaderUtils.createProgram(vertexSource, fragmentSource);

        GLES20.glUseProgram(program);

        int attribPosition = GLES20.glGetAttribLocation(program, "vPosition");
        int attribTexCoord = GLES20.glGetAttribLocation(program, "aCoordinate");
        int uTextureUnitLocation  = GLES20.glGetUniformLocation(program, "vTexture");
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");

        int[] loadTexture = ShaderUtils.loadTexture(bitmap);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,loadTexture[0]);
        GLES20.glUniform1i(uTextureUnitLocation,0);

        GLES20.glVertexAttribPointer(attribPosition,2,GLES20.GL_FLOAT,false,16,vertexBuffer);
        GLES20.glEnableVertexAttribArray(attribPosition);

        vertexBuffer.position(4);
        GLES20.glVertexAttribPointer(attribTexCoord,2,GLES20.GL_FLOAT,false,16,vertexBuffer);
        GLES20.glEnableVertexAttribArray(attribTexCoord);

    }



    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float aspectRatio = width>height?width*1.0f/height:height*1.0f/width;
        if (width > height){
            Matrix.orthoM(projectMatrix,0,-aspectRatio,aspectRatio,-1,1,-1,1);
        }else {
            Matrix.orthoM(projectMatrix,0,-1,1,-aspectRatio,aspectRatio,-1,1);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(vMatrix,1,false,projectMatrix,0);
        //GLES20.glUniform4f(v_color,0f,0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,4);

    }
}
