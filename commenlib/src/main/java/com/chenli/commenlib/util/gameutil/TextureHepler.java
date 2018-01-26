package com.chenli.commenlib.util.gameutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES32;
import android.opengl.GLUtils;
import android.util.Log;

import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/1/26.
 */

public class TextureHepler {

    private static final String TAG = "chenli";

    public static int loadTexture(Context context, int resourceId){
        int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        // 结果为0表调用纹理不成功。
        if (textureObjectIds[0] == 0){
            Log.e(TAG, "添加纹理失败");
            return 0;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        if (bitmap == null){
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        //绑定2维纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectIds[0]);
        //设置过滤,放大过滤和缩小过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //加载位图数据到当前纹理opengl中并返回id
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        //加载进去了，bitmap就没有用了。
        bitmap.recycle();
        bitmap = null;
        //生成mip贴图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解除绑定，0代表解除
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        //返回纹理对象ID
        return textureObjectIds[0];
    }

}
