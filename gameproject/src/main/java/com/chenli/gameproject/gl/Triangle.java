package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;

import com.chenli.commenlib.util.gameutil.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2018/1/16.
 */

public class Triangle {
    private Context context;
    private FloatBuffer vertexBuffer ;
    private final int vertexCount = triangleCoords.length/COORDS_PER_VERTEX;
    static final int COORDS_PER_VERTEX = 3 ;
    private final int vertexStride = COORDS_PER_VERTEX*4;
    static float triangleCoords[] = {
            0.0f,  0.622008459f, 0.0f,
            -0.5f, -0.311004243f, 0.0f,
            0.5f, -0.311004243f, 0.0f
    };

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    private int program;
    private int mPositionHandle;
    private String vertexShader;
    private int mColorHandle;

    public Triangle(Context context){
        this.context = context;
        ByteBuffer buffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer = buffer.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        initShader();
    }

    private void initShader() {
        vertexShader = ShaderUtils.loadFromAssetsFile("shader_vec4.glsl", context.getResources());
        String fragmentShader = ShaderUtils.loadFromAssetsFile("fragmentshader.glsl", context.getResources());
        program = ShaderUtils.createProgram(vertexShader, fragmentShader);

        mPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        mColorHandle = GLES20.glGetUniformLocation(program, "vColor");
    }

    public void draw(){
        GLES20.glUseProgram(program);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

}
