package com.chenli.gameproject.gl;

/**
 * Created by Administrator on 2018/1/26.
 */

public class Table {

    private static int position_component_count = 2;
    private static int texture_coordinates_component_count = 2 ;
    private static int stride = (position_component_count + texture_coordinates_component_count)*4;

    private static float[] vertex_data = {
            0f,0f,0.5f,0.5f,
            -0.5f,-0.8f,0f,0.9f,
            0.5f,-0.8f,1f,0.9f,
            0.5f,0.8f,1f,0.1f,
            -0.5f,0.8f,0f,0.1f,
            -0.5f,-0.8f,0f,0.9f
    };

    public Table(){
        VertexArray vertexArray = new VertexArray(vertex_data);
    }

    public void bindData(){

    }





}
