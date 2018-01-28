#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include "pthread.h"
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>




#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "chenli", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "chenli", __VA_ARGS__)

using namespace cv;
using namespace std;

extern "C"{

JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_grayPicture(JNIEnv *env, jobject instance,
                                                       jintArray pixel_, jint w, jint h) {
    jint *pixel = env->GetIntArrayElements(pixel_, NULL);
    if (pixel == NULL){
        return 0;
    }
    Mat imgData(h, w, CV_8UC4,pixel);
    uchar *ptr = imgData.ptr(0);
    for (int i = 0; i < w * h; ++i) {
        uchar grayScale = ptr[4*i+2]*0.299+ptr[4*i+1]*0.587+ptr[4*i+0]*0.114;
        ptr[4*i+1] = grayScale;
        ptr[4*i+2] = grayScale;
        ptr[4*i+0] = grayScale;
    }
    int size = w*h;
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result,0,size,pixel);
    env->ReleaseIntArrayElements(pixel_, pixel, 0);
    return result;
}

JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_grayPicture1(JNIEnv *env, jobject instance,
                                                        jintArray pixel_, jint w, jint h) {
    jint *pixel = env->GetIntArrayElements(pixel_, NULL);
    if (pixel == NULL){
        return 0;
    }
    Mat imgData(h, w, CV_8UC4,pixel);
    Mat dstImg;
    cvtColor(imgData,dstImg,CV_BGR2GRAY);


    env->ReleaseIntArrayElements(pixel_, pixel, 0);
}


JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_grayPicture2(JNIEnv *env, jobject instance,
                                                        jobject bitmap_) {
    AndroidBitmapInfo infoColor;
    void *pixelsColor;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env,bitmap_,&infoColor)) < 0){
        LOGE("bitmap fail");
        return;
    }
    if (infoColor.format!=ANDROID_BITMAP_FORMAT_RGBA_8888){
        LOGE("bitmap format must RGBA_8888");
        return;
    }
    if ((ret = AndroidBitmap_lockPixels(env,bitmap_,&pixelsColor)) < 0){
        LOGE("lockPixels fail");
    }
    Mat test(infoColor.height,infoColor.width,CV_8UC4);
    Mat bgra;
    cvtColor(test,test,CV_BGRA2GRAY);
    cvtColor(test,test,CV_GRAY2RGBA);
    AndroidBitmap_unlockPixels(env,bitmap_);
}

JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_blurPicture(JNIEnv *env, jobject instance,
                                                       jintArray pixel_, jint w, jint h,
                                                       jint size) {
    jint *pixel = env->GetIntArrayElements(pixel_, NULL);

    env->ReleaseIntArrayElements(pixel_, pixel, 0);
}


}

