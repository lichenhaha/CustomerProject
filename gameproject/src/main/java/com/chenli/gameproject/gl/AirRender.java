package com.chenli.gameproject.gl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.opengl.GLES20;

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
    private static final int position_component_count = 3;
    private static final int stride = (position_component_count + 3)*4;
    public final float[] projectionMatrix=new float[16];
    public final float[] modelMatrix = new float[16];

    private float[] tableVertices = {

//            0.0f,0.0f,0f,1.5f,1f,1f,1f,
//            -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//            0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//            0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
//            -0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
//            -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//
//            -0.5f,0f,0f,1.5f,1f,0f,0f,
//            0.5f,0f,0f,1.5f,0f,1f,0f,
//
//            0f,-0.4f,0f,1.25f,0f,0f,1f,
//            0f,0.4f,0f,1.75f,1f,0f,0f

            0.0f,0.0f,0f,1f,1f,1f,
            -0.5f,-0.8f,0f,0.7f,0.7f,0.7f,
            0.5f,-0.8f,0f,0.7f,0.7f,0.7f,
            0.5f,0.8f,0f,0.7f,0.7f,0.7f,
            -0.5f,0.8f,0f,0.7f,0.7f,0.7f,
            -0.5f,-0.8f,0f,0.7f,0.7f,0.7f,

            -0.5f,0f,0f,1f,0f,0f,
            0.5f,0f,0f,0f,1f,0f,

            0f,-0.4f,0f,0f,0f,1f,
            0f,0.4f,0f,1f,0f,0f

//            0.0f,0.0f,1f,1f,1f,
//            -0.5f,-0.8f,0.7f,0.7f,0.7f,
//            0.5f,-0.8f,0.7f,0.7f,0.7f,
//            0.5f,0.8f,0.7f,0.7f,0.7f,
//            -0.5f,0.8f,0.7f,0.7f,0.7f,
//            -0.5f,-0.8f,0.7f,0.7f,0.7f,
//
//            -0.5f,0f,1f,0f,0f,
//            0.5f,0f,0f,1f,0f,
//
//            0f,-0.4f,0f,0f,1f,
//            0f,0.4f,1f,0f,0f

    };
    private final int program;
    private final FloatBuffer vertexData;
    private final int u_color;
    private final int u_matrix;


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

        u_color = GLES20.glGetAttribLocation(program, "a_Color");
        int a_position = GLES20.glGetAttribLocation(program, "a_Position");
        GLES20.glVertexAttribPointer(a_position,position_component_count,GLES20.GL_FLOAT,false,stride,vertexData);//关联属性与顶点
        GLES20.glEnableVertexAttribArray(a_position);//使能

        vertexData.position(position_component_count);
        GLES20.glVertexAttribPointer(u_color,3,GLES20.GL_FLOAT,false,stride,vertexData);
        GLES20.glEnableVertexAttribArray(u_color);

        u_matrix = GLES20.glGetUniformLocation(program, "u_Matrix");


    }

    public void drawSelf(){
        GLES20.glUseProgram(program);//制定使用某套shader程序

        GLES20.glUniform4f(u_color,1.0f,1.0f,1.0f,1.0f);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);

        GLES20.glUniform4f(u_color,1.0f,0.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        GLES20.glUniform4f(u_color,0.0f,0.0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

        GLES20.glUniform4f(u_color,1.0f,0.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);

        GLES20.glUniformMatrix4fv(u_matrix,1,false,projectionMatrix,0);

    }


}
