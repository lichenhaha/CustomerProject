package com.chenli.commenlib.gif;

import android.bluetooth.le.ScanRecord;
import android.graphics.Bitmap;
import android.graphics.Path;

/**
 * Created by Administrator on 2018/1/25.
 */

public class GifLib {
    public static long ndkGifPoint;
    private static String path ;
    static {
        System.loadLibrary("libgif");
    }

    public static GifLib getInstance(String srcPath){
        path = srcPath;
        return GifLibHelper.instance;
    }
    private GifLib(){
        ndkGifPoint = setGifSource(path);
    }



    static class  GifLibHelper{
        private static GifLib instance = new GifLib();
    }

    private native long setGifSource(String srcPath);
    public native int getWidth(long ndkGif);
    public native int getHeight(long ndkGif);
    public native int updateFrame(long ndkGifPoint, Bitmap bitmap);

}
