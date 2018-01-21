package com.chenli.gameproject.gl;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/1/21 0021.
 */

public class BmpPicture {

    private Bitmap mBitmap;


    private final float[] sPos={
            -1.0f,1.0f,    //左上角
            -1.0f,-1.0f,   //左下角
            1.0f,1.0f,     //右上角
            1.0f,-1.0f     //右下角
    };
    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };

    public BmpPicture(Context context, Bitmap bitmap){
        mBitmap = bitmap;
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        float sWH = w/h;
    }


}
