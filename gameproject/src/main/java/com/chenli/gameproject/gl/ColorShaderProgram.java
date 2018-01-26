package com.chenli.gameproject.gl;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ColorShaderProgram extends ShaderProgram {

    private int uMatrixLocation;
    private int aPositionLocation;
    private int aColorLocation;

    public ColorShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        super(context, vertexShaderResourceId, fragmentShaderResourceId);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = GLES20.glGetUniformLocation(program,A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
    }

    public void setUniforms(float[] matrix, int textureId){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return aColorLocation;
    }

}
