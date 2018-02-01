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


    public static native String getStringFromJNI();


    public byte[] NV21ToARGB(byte[] srcData,int size, int width, int height){
        ByteBuffer byteBuffer = ByteBuffer.allocate(size).put(srcData);
        byteBuffer.position(0);
        byte[] argb = new byte[width*height];
        byte[] src_y = new byte[width*height];
        byte[] src_uv = new byte[width*height/4];
        int src_stride_y = width;
        int src_stride_uv = width+1;
        int argb_stride = width*4;
        byteBuffer.get(src_y,0,width*height);
        Log.e(TAG, "NV21ToARGB: " + src_uv.length);
        byteBuffer.position(width*height+1);
        byteBuffer.get(src_uv,0,src_uv.length);
        byteBuffer.position(0);
        NV21ToARGB(src_y,src_stride_y,src_uv,src_stride_uv,argb,argb_stride,width,height);
        Log.e(TAG, "NV21ToARGB:  ++++++++++" );
        return argb;
    }

    private native void NV21ToARGB(byte[] y,int stride_y,byte[] uv, int stride_uv, byte[] argb,int stride_argb,int width, int height);


}
