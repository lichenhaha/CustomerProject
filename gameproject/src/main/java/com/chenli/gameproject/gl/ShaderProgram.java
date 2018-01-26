package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;
import android.os.Process;

import com.chenli.commenlib.util.gameutil.ShaderUtils;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ShaderProgram {

    protected static String U_MATRIX = "u_Matrix";
    protected static String U_TEXTURE_UNIT = "u_TextureUnit";

    protected static String A_POSITION = "a_Position";
    protected static String A_COLOR = "a_Color";
    protected static String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected int program;
    public ShaderProgram(Context context, int vertexShaderResourceId,
                         int fragmentShaderResourceId){
        program = ShaderUtils.buildProgram(ShaderUtils.readTextFileFromResource(context,vertexShaderResourceId),
                ShaderUtils.readTextFileFromResource(context,fragmentShaderResourceId));

    }

    public void useProgram(){
        GLES20.glUseProgram(program);
    }

}
