package com.chenli.gameproject.newgl;

import android.opengl.GLES20;

import com.chenli.commenlib.util.mainutil.LogUtils;

/**
 * Created by Administrator on 2018/2/3.
 */

public class ShaderHelper {

    private static final String TAG = "chenli";

    public static int compileVertexShader(String shaderCode){
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){
        return compileShader(GLES20.GL_FRAGMENT_SHADER,shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        int i = GLES20.glCreateShader(type);
        if (i == 0){
            LogUtils.e("chenli", "could not create new shader");
            return 0;
        }
        GLES20.glShaderSource(i,shaderCode);
        GLES20.glCompileShader(i);
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(i,GLES20.GL_COMPILE_STATUS,compileStatus,0);
        if (compileStatus[0] == 0){
            GLES20.glDeleteShader(i);
            LogUtils.e("chenli", "compileation of shader failed");
            return 0;
        }
        return i;
    }


    public static int linkProgram(int vertexShaderId, int fragmentShaderId){
        int program = GLES20.glCreateProgram();
        if (program == 0){
            LogUtils.e("chenli","could not create new program");
            return 0;
        }
        //附上着色器
        GLES20.glAttachShader(program,vertexShaderId);
        GLES20.glAttachShader(program,fragmentShaderId);
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS,linkStatus,0);
        if (linkStatus[0] == 0){
            GLES20.glDeleteProgram(program);
            LogUtils.e("chenli", "linking of program failed");
            return 0;
        }
        return program;
    }


}
