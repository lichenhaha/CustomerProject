package com.chenli.gameproject.newgl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.chenli.gameproject.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/2/3.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer{

    public static final String A_COLOR = "a_Color";
    public static final String A_POSITION = "a_Position";
    public static final String UMATRIX = "u_Matrix";
    public final float[] projectMatrix = new float[16];
    public final float[] modelMatrix = new float[16];

    public static final int COLOR_COMPONENT_COUNT = 3;
    private static final int POSITION_COMPONENT_COUNT = 2 ;
    private static final int BYTES_PRE_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)*BYTES_PRE_FLOAT;
    private final FloatBuffer vertexData;
    private Context mContext;
    private int aColor;
    private int attribLocation;
    private int uMatrixLocation;

    public AirHockeyRenderer(Context context){
        mContext = context;
        float[] tableVerticesWithTriangles = {

//                0f,0f,0f,1.5f,1f,1f,1f,
//                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//                0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//                0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
//                -0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
//                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//
//                -0.5f,0f,0f,1.5f,1f,0f,0f,
//                0.5f,0f,0f,1.5f,1f,0f,0f,
//
//                0f,-0.4f,0f,1.25f,0f,0f,1f,
//                0f,0.4f,0f,1.75f,1f,0f,0f

                0f,0f,1f,1f,1f,
                -0.5f,-0.8f,0.7f,0.7f,0.7f,
                0.5f,-0.8f,0.7f,0.7f,0.7f,
                0.5f,0.8f,0.7f,0.7f,0.7f,
                -0.5f,0.8f,0.7f,0.7f,0.7f,
                -0.5f,-0.8f,0.7f,0.7f,0.7f,

                -0.5f,0f,1f,0f,0f,
                0.5f,0f,1f,0f,0f,

                0f,-0.4f,0f,0f,1f,
                0f,0.4f,1f,0f,0f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length*BYTES_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
        vertexData.position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f,0f,0f,0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        int program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        GLES20.glUseProgram(program);

        aColor = GLES20.glGetAttribLocation(program,A_COLOR);
        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColor,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,
                false,STRIDE,vertexData);
        GLES20.glEnableVertexAttribArray(aColor);


        attribLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        vertexData.position(0);
        GLES20.glVertexAttribPointer(attribLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,false,
                STRIDE,vertexData);
        GLES20.glEnableVertexAttribArray(attribLocation);

        uMatrixLocation = GLES20.glGetUniformLocation(program, UMATRIX);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        Matrix.perspectiveM(projectMatrix,0,45,width*1.0f/height,1f,10f);

        Matrix.setIdentityM(modelMatrix,0);//设置单位矩阵
        Matrix.translateM(modelMatrix,0,0f,0f,-4f);
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f);
        float[] temp = new float[16];
        Matrix.multiplyMM(temp,0,projectMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectMatrix,0,temp.length);


//        float aspectRatio = width>height?width*1.0f/height:height*1.0f/width;
//        if (width>height){
//            Matrix.orthoM(projectMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
//        }else {
//            Matrix.orthoM(projectMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
//        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,projectMatrix,0);

        GLES20.glUniform4f(attribLocation,1f,1f,1f,1f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);

        GLES20.glUniform4f(attribLocation,1f,0f,0f,1f);
        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        GLES20.glUniform4f(attribLocation,1.0f,0f,0f,1f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

        GLES20.glUniform4f(attribLocation,1.0f,0f,0f,1f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);
    }
}
