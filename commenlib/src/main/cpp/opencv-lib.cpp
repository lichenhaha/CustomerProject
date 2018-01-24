#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include "pthread.h"
#include <opencv2/core.hpp>



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

}

