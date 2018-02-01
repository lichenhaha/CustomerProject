//
// Created by Administrator on 2018/2/1.
//

#include <jni.h>
#include <android/log.h>


#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "chenli", __VA_ARGS__)

#ifndef CUSTOMERPROJECT_LIBYUV_H
#define CUSTOMERPROJECT_LIBYUV_H

extern "C" {

JNIEXPORT jstring JNICALL
Java_com_chenli_yuvlib_YUVlib_getStringFromJNI(JNIEnv *env, jclass instance);


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
                                                                                       jint height);


}


#endif //CUSTOMERPROJECT_LIBYUV_H
