package com.chenli.gameproject.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.chenli.commenlib.util.gameutil.ShaderUtils;
import com.chenli.gameproject.R;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/2/11.
 */

public class CameraPreviewByGL extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private Context context;
    private SurfaceTexture surfaceTexture;
    private Camera camera;

    private final String vertexShaderCode =
                         "uniform mat4 vMatrix;"+
                         "attribute vec4 vPosition;" +
                         "attribute vec2 inputTextureCoordinate;" +
                         "varying vec2 textureCoordinate;" +
                         "void main()" +
                         "{"+
                             "gl_Position = vMatrix*vPosition;"+
                             "textureCoordinate = inputTextureCoordinate;" +
                         "}";

    private final String fragmentShaderCode =
                         "#extension GL_OES_EGL_image_external : require\n"+
                         "precision mediump float;" +
                         "varying vec2 textureCoordinate;\n" +
                         "uniform samplerExternalOES s_texture;\n" +
                         "void main() {" +
                         "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                         "}";


    float[] mProjectMatrix = new float[16];

    float squareCoords[] = {
            -0.5f, 0.5f,
            0.5f,  0.5f  ,
            0.5f, -0.5f  ,
            -0.5f, -0.5f
    };
    float textureVertices[] = {
            0.0f, 0.0f,
            1.0f, 0f,
            1.0f, 1f,
            0f, 1f
     };

    private int textureID;
    private int vMatrix;

    public CameraPreviewByGL(Context context) {
        super(context);
        this.context = context;
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//被动渲染
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        initGLPoint();
        //openCamera();
    }

    private void initGLPoint() {
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(squareCoords.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(squareCoords);
        vertexBuffer.position(0);

        FloatBuffer textureBuffer = ByteBuffer.allocateDirect(textureVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertices);
        textureBuffer.position(0);

        int program = ShaderUtils.createProgram(ShaderUtils.readTextFileFromResource(getContext(), R.raw.vertexshader),
                ShaderUtils.readTextFileFromResource(getContext(),R.raw.fragmentshader));
        GLES20.glUseProgram(program);

        //GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureID);

        int attribLocation = GLES20.glGetAttribLocation(program, "a_position");
        int textureCoordinate = GLES20.glGetAttribLocation(program, "a_texCoord");
        int s_texture = GLES20.glGetUniformLocation(program, "u_samplerTexture");
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img4);
        int[] texture = ShaderUtils.loadTexture(bitmap);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
        GLES20.glUniform1i(s_texture,0);

        GLES20.glVertexAttribPointer(attribLocation,2,GLES20.GL_FLOAT,false,0,vertexBuffer);
        GLES20.glEnableVertexAttribArray(attribLocation);

        GLES20.glVertexAttribPointer(textureCoordinate,2,GLES20.GL_FLOAT,false,0,textureBuffer);
        GLES20.glEnableVertexAttribArray(textureCoordinate);

    }

    private void openCamera() {
        if (camera == null){
            camera = Camera.open();
        }else {
            camera.stopPreview();
            camera.release();
            camera = Camera.open();
        }
        try {
            surfaceTexture = new SurfaceTexture(textureID);
            surfaceTexture.setOnFrameAvailableListener(this);
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
//        camera.stopPreview();
//        camera.release();
//        camera = null;

        float aspectRatio = width>height?width*1.0f/height:height*1.0f/width;
        Matrix.setRotateM(mProjectMatrix,0,30,0,0,1);
        if (width > height){
            Matrix.orthoM(mProjectMatrix,0,-aspectRatio,aspectRatio,-1,1,-1,1);
        }else {
            Matrix.orthoM(mProjectMatrix,0,-1,1,-aspectRatio,aspectRatio,-1,1);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);

        //surfaceTexture.updateTexImage();

        GLES20.glUniformMatrix4fv(vMatrix,1,false,mProjectMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,4);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
    }
}
