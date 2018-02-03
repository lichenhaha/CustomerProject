package com.chenli.yuvlib;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2018/2/1.
 */

public class YUVlib {
    private static final String TAG = "chenli";
    static {
        System.loadLibrary("libyuv");
    }
    private YUVlib(){}
    public static YUVlib getInstance(){
        return YUVlibHelper.instance;
    }
    static class YUVlibHelper {
        public static YUVlib instance = new YUVlib();
    }

    public byte[] NV21ToARGB(byte[] srcData,int width, int height){
        byte[] argb = new byte[width*height*4];
        NV21ToARGB(srcData,argb,width,height);
        Log.e(TAG, "NV21ToARGB:  ++++++++++" );
        return argb;
    }

    private native void NV21ToARGB(byte[] src_data,byte[] dst_data,int width, int height);

    //public static native int RGBAToI420(byte[] rgba, byte[] yuv, int width, int height);

    public static native void ARGBToI420(byte[] array, byte[] yuv, int width, int height) ;

}
