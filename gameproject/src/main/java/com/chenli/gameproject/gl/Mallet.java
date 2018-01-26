package com.chenli.gameproject.gl;

import com.chenli.commenlib.util.gameutil.ShaderUtils;

/**
 * Created by Administrator on 2018/1/26.
 */

public class Mallet {

    private static int position_component_count = 2;
    private static int color_component_count = 2 ;
    private static int stride = (position_component_count + color_component_count)*4;
    private VertexArray vertexArray;
    private static float[] vertex_data = {
            0f,-0.4f,0f,0f,1f,
            0f,0.4f,1f,0f,0f
    };

    public Mallet(){
        vertexArray = new VertexArray(vertex_data);
    }

    public void bindData(){

    }


}
