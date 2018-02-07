package com.chenli.gameproject.newgl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;


import com.chenli.gameproject.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/2/3.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer{

    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private Table table;
    private Mallet mallet;
    private Puck puck;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;

    public AirHockeyRenderer(Context context){
        this.context = context;
    }

//    public static final String A_COLOR = "a_Color";
//    public static final String A_POSITION = "a_Position";
//    public static final String UMATRIX = "u_Matrix";
//    public final float[] projectMatrix = new float[16];
//    public final float[] modelMatrix = new float[16];
//
//    public static final int COLOR_COMPONENT_COUNT = 3;
//    private static final int POSITION_COMPONENT_COUNT = 2 ;
//    private static final int BYTES_PRE_FLOAT = 4;
//    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)*BYTES_PRE_FLOAT;
//    private final FloatBuffer vertexData;
//    private Context mContext;
//    private int aColor;
//    private int attribLocation;
//    private int uMatrixLocation;
//
//    public AirHockeyRenderer(Context context){
//        mContext = context;
//        float[] tableVerticesWithTriangles = {
//
////                0f,0f,0f,1.5f,1f,1f,1f,
////                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
////                0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
////                0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
////                -0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
////                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
////
////                -0.5f,0f,0f,1.5f,1f,0f,0f,
////                0.5f,0f,0f,1.5f,1f,0f,0f,
////
////                0f,-0.4f,0f,1.25f,0f,0f,1f,
////                0f,0.4f,0f,1.75f,1f,0f,0f
//
//                0f,0f,1f,1f,1f,
//                -0.5f,-0.8f,0.7f,0.7f,0.7f,
//                0.5f,-0.8f,0.7f,0.7f,0.7f,
//                0.5f,0.8f,0.7f,0.7f,0.7f,
//                -0.5f,0.8f,0.7f,0.7f,0.7f,
//                -0.5f,-0.8f,0.7f,0.7f,0.7f,
//
//                -0.5f,0f,1f,0f,0f,
//                0.5f,0f,1f,0f,0f,
//
//                0f,-0.4f,0f,0f,1f,
//                0f,0.4f,1f,0f,0f
//        };
//
//        vertexData = ByteBuffer
//                .allocateDirect(tableVerticesWithTriangles.length*BYTES_PRE_FLOAT)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        vertexData.put(tableVerticesWithTriangles);
//        vertexData.position(0);
//
//    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f,0f,0f,0f);

        table = new Table();
        mallet = new Mallet(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);

        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);


//        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
//        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext,R.raw.simple_fragment_shader);
//        int vertexshader = ShaderHelper.compileVertexShader(vertexShaderSource);
//        int fragmentshader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
//        int program = ShaderHelper.linkProgram(vertexshader, fragmentshader);
//
//        GLES20.glUseProgram(program);
//
//        aColor = GLES20.glGetAttribLocation(program,A_COLOR);
//        vertexData.position(POSITION_COMPONENT_COUNT);
//        GLES20.glVertexAttribPointer(aColor,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,
//                false,STRIDE,vertexData);
//        GLES20.glEnableVertexAttribArray(aColor);
//
//
//        attribLocation = GLES20.glGetAttribLocation(program, A_POSITION);
//        vertexData.position(0);
//        GLES20.glVertexAttribPointer(attribLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,false,
//                STRIDE,vertexData);
//        GLES20.glEnableVertexAttribArray(attribLocation);
//
//        uMatrixLocation = GLES20.glGetUniformLocation(program, UMATRIX);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        Matrix.perspectiveM(projectionMatrix,0,45,width*1.0f/height,1f,10f);
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);


//        Matrix.setIdentityM(modelMatrix,0);//设置单位矩阵
//        Matrix.translateM(modelMatrix,0,0f,0f,-3.5f);
//        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f);
//        float[] temp = new float[16];
//        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
//        System.arraycopy(temp,0,projectionMatrix,0,temp.length);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        positionTableInScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        // Draw the mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        positionObjectInScene(0f, mallet.height / 2f, 0.4f);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        // Note that we don't have to define the object data twice -- we just
        // draw the same mallet again but in a different position and with a
        // different color.
        mallet.draw();

        // Draw the puck.
        positionObjectInScene(0f, puck.height / 2f, 0f);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorShaderProgram);
        puck.draw();

//        colorShaderProgram.useProgram();
//        colorShaderProgram.setUniforms(projectionMatrix);
//        mallet.bindData(colorShaderProgram);
//        mallet.draw();


//        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,projectMatrix,0);
//
//        GLES20.glUniform4f(attribLocation,1f,1f,1f,1f);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
//
//        GLES20.glUniform4f(attribLocation,1f,0f,0f,1f);
//        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);
//
//        GLES20.glUniform4f(attribLocation,1.0f,0f,0f,1f);
//        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);
//
//        GLES20.glUniform4f(attribLocation,1.0f,0f,0f,1f);
//        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);
    }

    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }

    private void positionTableInScene() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }
}
