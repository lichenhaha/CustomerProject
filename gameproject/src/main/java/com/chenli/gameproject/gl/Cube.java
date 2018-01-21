package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;

import com.chenli.commenlib.util.gameutil.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class Cube {


    final float[] cubePositions = {
            -1.0f,1.0f,1.0f,
            -1.0f,-1.0f,1.0f,
            1.0f,-1.0f,1.0f,
            1.0f,1.0f,1.0f,
            -1.0f,1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f,1.0f,-1.0f,
    };
    short[] index = {
            0,3,2,0,2,1,
            0,1,5,0,5,4,
            0,7,3,0,4,7,
            6,7,4,6,4,5,    //后面
            6,3,7,6,2,3,    //右面
            6,5,1,6,1,2     //下面
    };

    //八个顶点的颜色，与顶点坐标一一对应
    float color[] = {
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
    };

    public float[] mMVPMatrix=new float[16];
    private final ShortBuffer mIndexBuffer;
    private final FloatBuffer mVertexBuffer;
    private int mProgram;
    private final FloatBuffer mColorBuffer;

    public Cube(Context context){
        ByteBuffer bb = ByteBuffer.allocateDirect(cubePositions.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(cubePositions);
        mVertexBuffer.position(0);

        ByteBuffer dd = ByteBuffer.allocateDirect(
                color.length * 4);
        dd.order(ByteOrder.nativeOrder());
        mColorBuffer = dd.asFloatBuffer();
        mColorBuffer.put(color);
        mColorBuffer.position(0);

        ByteBuffer cc= ByteBuffer.allocateDirect(index.length*2);
        cc.order(ByteOrder.nativeOrder());
        mIndexBuffer = cc.asShortBuffer();
        mIndexBuffer.put(index);
        mIndexBuffer.position(0);

        String s = ShaderUtils.loadFromAssetsFile("shader3.glsl", context.getResources());
        String s1 = ShaderUtils.loadFromAssetsFile("fragment3.glsl", context.getResources());
        mProgram = ShaderUtils.createProgram(s, s1);
    }

    public void drawSelf(){
        GLES20.glUseProgram(mProgram);
        int vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mMVPMatrix,0);
        int vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition,3,GLES20.GL_FLOAT,false,0,mVertexBuffer);

        int aColor = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(aColor);
        GLES20.glVertexAttribPointer(aColor,4,GLES20.GL_FLOAT,false,0,mColorBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,index.length,GLES20.GL_UNSIGNED_SHORT,mIndexBuffer);
        GLES20.glDisableVertexAttribArray(vPosition);

    }


}
