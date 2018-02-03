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
                                         jbyteArray src,
                                         jbyteArray dst,
                                         jint width,
                                         jint height) {
    jbyte *src_data = env->GetByteArrayElements(src, NULL);
    jbyte *dst_data = env->GetByteArrayElements(dst, NULL);
    uint8 *src_y = (uint8 *) src_data;
    int src_stride_y = width;
    uint8 *src_vu = (uint8 *) (src_data + width * height);
    int src_stride_vu = width/2;
    uint8 *dst_argb = (uint8 *) dst_data;
    int dst_stride_argb = width*4;
    //临时数组
    jbyte temp[width*height*4];
    libyuv::NV21ToARGB(src_y,src_stride_y,src_vu,src_stride_vu,(uint8*)temp,dst_stride_argb,width,height);
    //旋转
    libyuv::ARGBRotate((uint8*)temp,dst_stride_argb,dst_argb,dst_stride_argb,width,height,libyuv::kRotate90);
    env->ReleaseByteArrayElements(src,  src_data, 0);
    env->ReleaseByteArrayElements(dst, dst_data, 0);
}


JNIEXPORT jint JNICALL
Java_com_chenli_yuvlib_YUVlib_RGBAToI420(JNIEnv *env, jclass type_, jbyteArray rgba_,
                                         jbyteArray yuv_, jint width, jint height) {
    jbyte *rgba = env->GetByteArrayElements(rgba_, NULL);
    jbyte *yuv = env->GetByteArrayElements(yuv_, NULL);
    int rgba_stride = width*4;
    int y_stride = width;
    int u_stride = width/2;
    int v_stride = u_stride;
    uint8* dst_u = (uint8*)yuv + width*height;
    uint8* dst_v = (uint8*)yuv + width*height + width*height/4;
    libyuv::ABGRToI420((uint8*)rgba,rgba_stride,(uint8*)yuv,y_stride,dst_u,u_stride,dst_v,v_stride,width,height);
    //libyuv::ARGBToI420((uint8*)rgba,rgba_stride,(uint8*)yuv,y_stride,dst_u,u_stride,dst_v,v_stride,width,height);
    env->ReleaseByteArrayElements(rgba_, rgba, 0);
    env->ReleaseByteArrayElements(yuv_, yuv, 0);

}



JNIEXPORT void JNICALL
Java_com_chenli_yuvlib_YUVlib_ARGBToI420(JNIEnv *env, jclass type, jbyteArray array_,
                                         jbyteArray yuv_, jint width, jint height) {
    jbyte *array = env->GetByteArrayElements(array_, NULL);
    jbyte *yuv = env->GetByteArrayElements(yuv_, NULL);
    uint8 *src_argb = (uint8 *) array;
    int src_stride_argb = width*4;
    uint8 *dst_y = (uint8 *) yuv;
    int dst_stride_y = width;
    uint8 *dst_u = (uint8*) (yuv + width * height);
    int dst_stride_u = width/2;
    uint8  *dst_v = (uint8 *) (yuv + width * height + width * height / 4);
    int dst_stride_v = dst_stride_u;

    libyuv::ARGBToI420(src_argb,src_stride_argb,dst_y,dst_stride_y,dst_u,dst_stride_u,dst_v,dst_stride_v,width,height);

    env->ReleaseByteArrayElements(array_, array, 0);
    env->ReleaseByteArrayElements(yuv_, yuv, 0);
    LOGE("+++++++++++++");
}


