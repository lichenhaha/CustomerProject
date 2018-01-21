package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;

import com.chenli.commenlib.util.gameutil.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class Triangle3 {

//    float triangleCoords[] = {
//            0.5f,0.5f,0.0f,
//            -0.5f,-0.5f,0.0f,
//            0.5f,-0.5f,0.0f
//    };

    float triangleCoords[] = {
            -0.5f,0.5f,0.0f,
            -0.5f,-0.5f,0.0f,
            0.5f,-0.5f,0.0f,
            0.5f,0.5f,0.0f
    };

    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };

//    float color[] = {
//            0.0f, 1.0f, 0.0f, 1.0f ,
//            1.0f, 0.0f, 0.0f, 1.0f,
//            0.0f, 0.0f, 1.0f, 1.0f
//    };

    //绘制顺序
    static short index[]={
            0,1,2,0,2,3
    };

    private final int mProgram;
    private final FloatBuffer mVertexBuffer;
    public float[] mMVPMatrix=new float[16];
    //private final FloatBuffer mColorBuffer;
    private static final int COORDS_PER_VERTEX = 3;
    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节
    //顶点个数
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final ShortBuffer mIndexBuffer;

    public Triangle3(Context context){

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(triangleCoords);
        mVertexBuffer.position(0);

        ByteBuffer cc = ByteBuffer.allocateDirect(index.length * 2);
        cc.order(ByteOrder.nativeOrder());
        mIndexBuffer = cc.asShortBuffer();
        mIndexBuffer.put(index);
        mIndexBuffer.position(0);

//        ByteBuffer dd = ByteBuffer.allocateDirect(color.length * 4);
//        dd.order(ByteOrder.nativeOrder());
//        mColorBuffer = dd.asFloatBuffer();
//        mColorBuffer.put(color);
//        mColorBuffer.position(0);

        String s = ShaderUtils.loadFromAssetsFile("shader3.glsl", context.getResources());
        String s1 = ShaderUtils.loadFromAssetsFile("fragment3.glsl", context.getResources());
        mProgram = ShaderUtils.createProgram(s, s1);

    }





    public void drawSelf(){
        GLES20.glUseProgram(mProgram);

        int mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);

        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,GLES20.GL_FLOAT,
                false,vertexStride,mVertexBuffer);
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //绘制颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        //GLES20.glEnableVertexAttribArray(mColorHandle);
        //GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,0,mColorBuffer);
        //绘制三角形
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length,GLES20.GL_UNSIGNED_SHORT,mIndexBuffer);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }



}
