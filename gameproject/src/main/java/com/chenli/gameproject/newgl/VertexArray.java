package com.chenli.gameproject.newgl;


import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2018/2/4.
 */

public class VertexArray {

    private FloatBuffer floatBuffer;
    public static final int COLOR_COMPONENT_COUNT = 3;
    private static final int POSITION_COMPONENT_COUNT = 2 ;

    public VertexArray(float[] vertexData){
        floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length*Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
                                       int componentCount, int strid){

        floatBuffer.position(dataOffset);
        GLES20.glVertexAttribPointer(attributeLocation,componentCount,GLES20.GL_FLOAT,
                false,strid,floatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);
    }


}
