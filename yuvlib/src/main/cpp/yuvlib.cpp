//
// Created by Administrator on 2018/1/31.
//

#include <yuvlib.h>
#include <jni.h>
#include <string>
#include <iostream>
#include "libyuv.h"

using namespace std;

JNIEXPORT jstring JNICALL
Java_com_chenli_yuvlib_YUVlib_getStringFromJNI(JNIEnv *env, jclass instance){

    string str = " hello world";
    return env->NewStringUTF(str.c_str());

}


JNIEXPORT void JNICALL
Java_com_chenli_yuvlib_YUVlib_NV21ToARGB(JNIEnv *env,
                                                                                       jobject instance,
                                                                                       jbyteArray y_,
                                                                                       jint stride_y,
                                                                                       jbyteArray uv_,
                                                                                       jint stride_uv,
                                                                                       jbyteArray argb_,
                                                                                       jint stride_argb,
                                                                                       jint width,
                                                                                       jint height) {
    uint8 *y = (uint8 *) env->GetByteArrayElements(y_, NULL);
    uint8 *uv = (uint8 *) env->GetByteArrayElements(uv_, NULL);
    uint8 *argb = (uint8 *) env->GetByteArrayElements(argb_, NULL);

    libyuv::NV21ToARGB(y,stride_y,uv,stride_uv,argb,stride_argb,width,height);
    //libyuv::ARGBRotate(argb,width*4,argb,width*4,width,height,libyuv::kRotate90);

    env->ReleaseByteArrayElements(y_, (jbyte *) y, 0);
    env->ReleaseByteArrayElements(uv_, (jbyte *) uv, 0);
    env->ReleaseByteArrayElements(argb_, (jbyte *) argb, 0);

    LOGE("++++++   C++  +++++");
}