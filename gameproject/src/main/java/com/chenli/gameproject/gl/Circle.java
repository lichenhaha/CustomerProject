package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;

import com.chenli.commenlib.util.gameutil.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class Circle {
    private int n = 360;
    private float radius=1.0f;
    private int mProgram;
    public float[] mMVPMatrix=new float[16];
    private final FloatBuffer mVertexBuffer;

    //设置颜色，依次为红绿蓝和透明通道
    float color[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    private final float[] mShapePos;

    public Circle(Context context){
        mShapePos = createPosition();
        ByteBuffer bb = ByteBuffer.allocateDirect(mShapePos.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(mShapePos);
        mVertexBuffer.position(0);

        String s = ShaderUtils.loadFromAssetsFile("shader3.glsl", context.getResources());
        String s1 = ShaderUtils.loadFromAssetsFile("fragment3.glsl", context.getResources());
        mProgram = ShaderUtils.createProgram(s, s1);
    }

    public void drawSelf(){
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        int mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mMVPMatrix,0);
        //获取定点着色器的vPosition成员句柄
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形定点句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,0,mVertexBuffer);
        //获取片元着色器的vColor成员的句柄
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //绘制三角形颜色
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,mShapePos.length/3);
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    private float[] createPosition(){
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);
        data.add(0.0f);
        data.add(0.0f);
        float angDesSpan = 360f/n;
        for (float i = 0; i < 360+angDesSpan; i+=angDesSpan) {
            data.add((float) (radius*Math.sin(i*Math.PI/180f)));
            data.add((float) (radius*Math.cos(i*Math.PI/180f)));
            data.add(0.0f);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }
}
