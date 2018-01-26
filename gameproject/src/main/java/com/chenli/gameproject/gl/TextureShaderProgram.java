package com.chenli.gameproject.gl;

import android.content.Context;
import android.content.UriMatcher;
import android.opengl.GLES20;

/**
 * Created by Administrator on 2018/1/26.
 */

public class TextureShaderProgram extends ShaderProgram {

    private int uMatrixLocation;
    private int uTextureUnitLocation;
    private int aPositionLocation;
    private int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        super(context, vertexShaderResourceId, fragmentShaderResourceId);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program,U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program,A_TEXTURE_COORDINATES);

    }

    public void setUniforms(float[] matrix, int textureId){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);
        GLES20.glUniform1i(uTextureUnitLocation,0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getaTextureCoordinatesAttributeLocation(){
        return aTextureCoordinatesLocation;
    }

}
