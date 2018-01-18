package com.chenli.commenlib.jni;

/**
 * Created by Administrator on 2018/1/17.
 */

public class JNICall {
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("native-lib");
    }

    public static native String getStringFromJNI();

    public static native int[] getIntArrayFromJNI();

    public static native String getStringFromProtocol();

    public static native String getStringFormatInfo();

    public static native String getstringAvcodeInfo();

    public static native String getstringFilterInfo();


    public static native void setPersonToJNI(Person person);

    public static native Person getPersonFromJNI();

}
