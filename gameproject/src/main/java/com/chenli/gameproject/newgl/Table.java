package com.chenli.gameproject.newgl;

import android.opengl.GLES20;
import android.view.View;


/**
 * Created by Administrator on 2018/2/4.
 */

public class Table {

    public static final int COLOR_COMPONENT_COUNT = 3;
    private static final int POSITION_COMPONENT_COUNT = 2 ;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT)*Constants.BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            0f,   0f,   0.5f,0.5f,
            -0.5f,-0.8f,0f,  0.9f,
            0.5f, -0.8f,1f,  0.9f,
            0.5f, 0.8f, 1f,  0.1f,
            -0.5f,0.8f, 0f,  0.1f,
            -0.5f,-0.8f,0f,  0.9f
    };
    private final VertexArray vertexArray;

    public Table(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureShaderProgram){
        vertexArray.setVertexAttribPointer(0,textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,
                textureShaderProgram.getPositionAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,STRIDE);
    }

    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
    }

}
