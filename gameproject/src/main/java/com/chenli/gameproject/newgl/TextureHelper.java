package com.chenli.gameproject.newgl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.chenli.commenlib.util.mainutil.LogUtils;

/**
 * Created by Administrator on 2018/2/3.
 */

public class TextureHelper {

    public static int loadTexture(Context context, int resourceId){
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1,textureIds,0);
        if (textureIds[0] == 0){
            LogUtils.e("chenli","could not generate a new opengl texture");
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null){
            LogUtils.e("chenli","resourceId is not fount");
            GLES20.glDeleteTextures(1,textureIds,0);
            return 0;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureIds[0]);
        //过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
        //生成所有必要的级别
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        bitmap.recycle();
        //纹理加载完成，解除绑定,0表示解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        return textureIds[0];
    }


}
