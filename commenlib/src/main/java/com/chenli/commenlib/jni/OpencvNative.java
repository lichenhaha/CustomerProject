package com.chenli.commenlib.jni;

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

}
