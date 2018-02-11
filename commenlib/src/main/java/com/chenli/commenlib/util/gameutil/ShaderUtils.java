package com.chenli.commenlib.util.gameutil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.chenli.commenlib.util.mainutil.CloseUtils;
import com.chenli.commenlib.util.mainutil.FileIOUtils;
import com.chenli.commenlib.util.mainutil.FileUtils;
import com.chenli.commenlib.util.mainutil.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/1/16.
 */

//加载顶点Shader与片元Shader的工具类
public class ShaderUtils {

    //加载制定shader的方法
    public static int loadShader(int shaderType, String source){
        int shader = GLES20.glCreateShader(shaderType);
        //若创建成功则加载shader
        if (shader != 0){
            //加载shader的源代码
            GLES20.glShaderSource(shader,source);
            //编译shader
            GLES20.glCompileShader(shader);
            int[] params = new int[1];
            GLES20.glGetShaderiv(shader,GLES20.GL_COMPILE_STATUS,params,0);
            if (params[0] == 0){
                Log.e("chenli","Could not compile shader " + shaderType + " : " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0 ;
            }
        }
        return shader;
    }

    public static int compileVertexShader(String shaderSource){
        int shader;
        shader = loadShader(GLES20.GL_VERTEX_SHADER, shaderSource);
        return shader;
    }

    public static int compileFragmentShader(String shaderSource){
        int shader;
        shader = loadShader(GLES20.GL_FRAGMENT_SHADER, shaderSource);
        return shader;
    }


    //创建shader程序的方法
    public static int createProgram(String vertexSource, String fragmentSource){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0){
            return 0;
        }
        //加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        if (pixelShader == 0){
            return 0;
        }
        //创建程序
        int program = GLES20.glCreateProgram();
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0){
            //向程序中加入顶点着色器
            GLES20.glAttachShader(program,vertexShader);
            //checkGlError("glAttachShader");
            //向程序中加入片元着色器
            GLES20.glAttachShader(program, pixelShader);
            //checkGlError("glAttachShader");
            //连接程序
            GLES20.glLinkProgram(program);
            //存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            //获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS,linkStatus,0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE) {
                LogUtils.e("chenli","Could not link program: " + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private static void checkGlError(String op) {
        int error ;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource){
        int program;
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);
        program = linkProgram(vertexShader, fragmentShader);
        return program;
    }

    private static int linkProgram(int vertexShader, int fragmentShader) {
        int program = GLES20.glCreateProgram();
        if (program != 0){
            GLES20.glAttachShader(program,vertexShader);
            GLES20.glAttachShader(program,fragmentShader);
            GLES20.glLinkProgram(program);
            return program;
        }
        return 0;
    }


    /**
     * 从资源文件中读取
     * @param context
     * @param resourceId
     * @return
     */
    public static String readTextFileFromResource(Context context,int resourceId){
        StringBuilder body = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null){
                body.append(line);
                body.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
            inputStream.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();
    }


    //从sh脚本中加载shader内容的方法
    public static String loadFromAssetsFile(String fname,Resources r){
        String result = null;
        try {
            InputStream in = r.getAssets().open(fname);
            int ch = 0 ;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1){
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result = new String(buff,"utf-8");
            result = result.replace("\\r\\n","\n");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public static int[] loadTexture(Bitmap bitmap){
        int[] textureId = new int[1];
        //生成一个纹理
        GLES20.glGenTextures(1,textureId,0);
        int[] result = null;
        if (textureId[0] != 0){
            result = new int[3];
            result[0] = textureId[0];
            result[1] = bitmap.getWidth();
            result[2] = bitmap.getHeight();
            //bind to the texture in opengl
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId[0]);
            //set filtering
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER
                    ,GLES20.GL_LINEAR_MIPMAP_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //让图片和纹理关联起来，加载到OpenGl空间中
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
            bitmap.recycle();
            //纹理加载完成，解除绑定,0表示解绑
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        }else {
            throw new RuntimeException("Error loading texture");
        }
        return result;
    }

    public static int createTextureID(){
        int[] texture = new int[1];
        GLES20.glGenTextures(1,texture,0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

}
