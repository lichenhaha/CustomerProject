package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;

import com.chenli.commenlib.util.gameutil.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2018/1/17.
 */

public class Triangle2 {
    static final int coords_per_vertex = 3 ;
    static float triangleCoords[] = {
        0.0f,0.622008459f,0.0f,
            -0.5f,-0.311004243f,0.0f,
            0.5f,-0.311004243f,0.0f
    };

    float color[] ={0.6367f,0.7695f,0.22f,1.0f};
    private final int program;
    private final FloatBuffer vertexBuffer;

    public Triangle2(Context context){
        ByteBuffer bb =  ByteBuffer.allocateDirect(triangleCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        String vertexShader = ShaderUtils.loadFromAssetsFile("shader_vec4.glsl", context.getResources());
        String fragmentShader = ShaderUtils.loadFromAssetsFile("fragmentshader.glsl", context.getResources());
        program = ShaderUtils.createProgram(vertexShader, fragmentShader);
    }

    public void draw(){
        GLES20.glUseProgram(program);
        int vPosition = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition,coords_per_vertex,GLES20.GL_FLOAT,false,12,vertexBuffer);

        int vColor = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(vColor,1,color,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);
        GLES20.glDisableVertexAttribArray(vPosition);


    }

}
