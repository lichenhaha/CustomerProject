package com.chenli.commenlib.jni;


import com.chenli.commenlib.util.mainutil.LogUtils;

/**
 * Created by Administrator on 2018/1/17.
 */

public class JNICall {

    public static final int TEST_BUFFER_SIZE = 128;
    private byte[] mTestBuffer1;
    private byte[] mTestbuffer2;

    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("test-lib");
        System.loadLibrary("native-lib");
    }

    public JNICall(){
        mTestBuffer1 = new byte[TEST_BUFFER_SIZE];
        mTestbuffer2 = new byte[TEST_BUFFER_SIZE];
    }

    public static native String getStringFromJNI();

    public static native int[] getIntArrayFromJNI();

    public static native String getStringFromProtocol();

    public static native String getStringFormatInfo();

    public static native String getstringAvcodeInfo();

    public static native String getstringFilterInfo();


    public static native void setPersonToJNI(Person person);

    public static native Person getPersonFromJNI();

    public native void nativeSetBuffer1(byte[] buffer, int len);

    public native void nativeSetBuffer2(byte[] buffer, int len);

    private native byte[] nativeGetByteArray();

    public native void getMediaInfo(String srcPath);


    public void test(){
        nativeSetBuffer1(mTestBuffer1,TEST_BUFFER_SIZE);
        printBuffer("native1",mTestBuffer1);

        nativeSetBuffer2(mTestbuffer2, TEST_BUFFER_SIZE);
        printBuffer("native2", mTestbuffer2);

        byte[] buffer = nativeGetByteArray();
        printBuffer("nativeGetByteArray",buffer);

    }

    private void printBuffer(String tag, byte[] mTestBuffer) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mTestBuffer.length; i++) {
            builder.append(mTestBuffer[i]);
            builder.append(" ");
        }
        LogUtils.e("chenli", builder.toString());
    }

    public native void setDataSource(String srcPath);
    public native void pauseAudioPlayer();
    public native void stopAudioPlayer();
    public native void startAudioPlayer();






}
