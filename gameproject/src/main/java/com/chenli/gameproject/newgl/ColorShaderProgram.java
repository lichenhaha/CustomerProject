package com.chenli.gameproject.newgl;

import android.content.Context;
import android.opengl.GLES20;

import com.chenli.gameproject.R;
import com.chenli.gameproject.util.ShaderProgram;

/**
 * Created by Administrator on 2018/2/4.
 */

public class ColorShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int aColorLocation;

    protected ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
    }

    public void setUniforms(float[] matrix,float r, float g, float b){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES20.glUniform4f(aColorLocation,r, g, b, 1f);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return aColorLocation;
    }

}
