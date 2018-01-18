package com.chenli.gameproject.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Administrator on 2018/1/18.
 */

public class Fourangle {
    private static final float[] vertex = {
            1,1,0,
            -1,1,0,
            -1,-1,0,
            1,-1,0
    };
    private static final short[] VERTEX_INDEX = {0,1,2,0,2,3};
    private final ShortBuffer mVertexIndexBuffer ;

    public Fourangle(){
        FloatBuffer mVertexBuffer = ByteBuffer.allocateDirect(vertex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertex);
        mVertexBuffer.position(0);
        mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length*2).order(ByteOrder.nativeOrder()).asShortBuffer().put(VERTEX_INDEX);
        mVertexIndexBuffer.position(0);
    }

}
