package com.chenli.gameproject.gl;

import android.content.Context;
import android.support.annotation.ColorRes;

/**
 * Created by Administrator on 2018/1/26.
 */

public class AirHockeyRenderer {

    private Context context;
    private float[] projectionMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;

    public AirHockeyRenderer(Context context){
        this.context = context;
    }

}
