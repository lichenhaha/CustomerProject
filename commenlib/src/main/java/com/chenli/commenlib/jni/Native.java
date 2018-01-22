package com.chenli.commenlib.jni;

import com.chenli.commenlib.util.mainutil.LogUtils;

/**
 * Created by Administrator on 2018/1/22.
 */

public class Native {
    public native void nativeInitilize();
    public native void nativeThreadStart();
    public native void nativeThreadStop();

    public void onNativeCallback(int count){
        LogUtils.e("chenli","onNativeCallback count = " + count + " threadName = " +Thread.currentThread());

    }

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("native-lib");
    }

}
