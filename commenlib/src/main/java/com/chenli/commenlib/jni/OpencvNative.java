package com.chenli.commenlib.jni;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/1/24.
 */

public class OpencvNative {

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }
    private OpencvNative(){}

    public static OpencvNative getInstance(){
        return OpencvNativeHelper.instance;
    }

    static class OpencvNativeHelper{
        private static OpencvNative instance = new OpencvNative();
    }

    public native int[] grayPicture(int[] pixel, int w, int h);

    public native void grayPicture2(Bitmap bitmap);

    public native int[] grayPicture1(int[] pixel, int w, int h);

    /**
     * 平滑处理，滤镜效果
     */
    public native int[] blurPicture(int[] pixel, int w, int h, int size);

}
