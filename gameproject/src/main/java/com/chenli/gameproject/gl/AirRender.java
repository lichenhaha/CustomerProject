package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Process;

import com.chenli.commenlib.util.gameutil.ShaderUtils;
import com.chenli.gameproject.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2018/1/25.
 */

public class AirRender {

    private static final int BYTES_PER_FLOAT = 4;

    private float[] tableVertices = {
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,

            -0.5f,-0.5f,
            0.5f,-0.5f,
            0.5f, 0.5f,

            -0.5f,0f,
            0.5f,0f,

            0f,-0.25f,
            0f,0.25f

    };
    private final int program;
    private final FloatBuffer vertexData;
    private final int u_color;


    public AirRender(Context context){
        vertexData = ByteBuffer
                .allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVertices);
        vertexData.position(0);

        String shader = ShaderUtils.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragment = ShaderUtils.readTextFileFromResource(context,R.raw.simple_fragment_shader);
        program = ShaderUtils.createProgram(shader, fragment);

        u_color = GLES20.glGetUniformLocation(program, "u_Color");
        int a_position = GLES20.glGetAttribLocation(program, "a_Position");
        GLES20.glVertexAttribPointer(a_position,2,GLES20.GL_FLOAT,false,0,vertexData);//关联属性与顶点
        GLES20.glEnableVertexAttribArray(a_position);//使能

    }

    public void drawSelf(){
        GLES20.glUseProgram(program);//制定使用某套shader程序
        GLES20.glUniform4f(u_color,1.0f,1.0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,6);

        GLES20.glUniform4f(u_color,1.0f,0.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);
//
        GLES20.glUniform4f(u_color,0.0f,0.0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);
//
        GLES20.glUniform4f(u_color,1.0f,0.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);

    }


}
