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


typedef struct {
    uint8_t red;
    uint8_t green;
    uint8_t blue;
    uint8_t alpha;
} argb;

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
    cvtColor(dstImg,dstImg,CV_GRAY2BGRA);
    jint  *ptr = dstImg.ptr<jint>(0);
    int size = w*h;
    jintArray  result = env->NewIntArray(size);
    env->SetIntArrayRegion(result,0,size,ptr);
    env->ReleaseIntArrayElements(pixel_, pixel, 0);
    return result;
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
    uint8_t gray;
    for (int i = 0; i < infoColor.height; ++i) {
        argb *line = (argb *) pixelsColor;
        for (int j = 0; j < infoColor.width; ++j) {
            gray = 0.3 * line[j].red + 0.59 * line[j].green
                   + 0.11 * line[j].blue;
            line[j].red = line[j].green = line[j].blue = gray;
        }
        pixelsColor = (char*)pixelsColor + infoColor.stride;
    }
    AndroidBitmap_unlockPixels(env,bitmap_);
}

JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_blurPicture(JNIEnv *env, jobject instance,
                                                       jintArray pixel_, jint w, jint h,
                                                       jint size) {
    jint *pixel = env->GetIntArrayElements(pixel_, NULL);
    Mat srcImg(h,w,CV_8UC4,pixel);
    Mat dstImg;
    blur(srcImg,dstImg,cv::Size(size,size));
    jint  *ptr = dstImg.ptr<jint>(0);
    int count = w*h;
    jintArray  result = env->NewIntArray(count);
    env->SetIntArrayRegion(result,0,count,ptr);
    env->ReleaseIntArrayElements(pixel_, pixel, 0);
    return result;
}

JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_cannyPicture(JNIEnv *env, jobject instance,
                                                        jintArray pixels_, jint w, jint h,
                                                        jint size) {
    jint *pixels = env->GetIntArrayElements(pixels_, NULL);
    Mat srcImg(h,w,CV_8UC4,(uchar *)pixels);
    Mat dstImage, edge, grayImage;
    dstImage.create(srcImg.size(),srcImg.type());
    cvtColor(srcImg,grayImage,CV_BGRA2GRAY,4);
    blur(grayImage,edge,cv::Size(size,size));
    Canny(edge,edge,3,9);
    cvtColor(edge,edge,CV_GRAY2BGRA);
    jint  *ptr = edge.ptr<jint>(0);
    int count = w*h;
    jintArray  result = env->NewIntArray(count);
    env->SetIntArrayRegion(result,0,count,ptr);
    env->ReleaseIntArrayElements(pixels_, pixels, 0);
    pixels = NULL;
    ptr = NULL;
    return result;
}


JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_dataTransition(JNIEnv *env, jobject instance,
                                                          jintArray pixels_, jint w, jint h) {
    jint *pixels = env->GetIntArrayElements(pixels_, NULL);


//    LOGE("pixles0 = %x ", (jbyte*)pixels[0]);
//    LOGE("pixles1 = %x" , (jbyte*)pixels[1]);
//    LOGE("pixles2 = %x" , (jbyte*)pixels[2]);
//    LOGE("pixles3 = %x" , (jbyte*)pixels[3]);


    Mat mat(h,w,CV_8UC4,pixels);

    LOGE("mat0 = %d " ,mat.at<Vec4b>(0,0)[0]);
    LOGE("mat1 = %d " ,mat.at<Vec4b>(0,0)[1]);
    LOGE("mat2 = %d " ,mat.at<Vec4b>(0,0)[2]);
    LOGE("mat3 = %d " ,mat.at<Vec4b>(0,0)[3]);

    jint *ptr = mat.ptr<jint>(0);
    jintArray result = env->NewIntArray(w*h);
    env->SetIntArrayRegion(result,0,w*h,ptr);
    env->ReleaseIntArrayElements(pixels_, pixels, 0);
    return result;
}


JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_filterPicture(JNIEnv *env, jobject instance,
                                                         jintArray pixels_, jint w, jint h) {
    jint *pixels = env->GetIntArrayElements(pixels_, NULL);
    Mat mat(h,w,CV_8UC4,pixels);

    Mat kern = Mat::zeros(3, 3, CV_8SC1);
    kern.at<char>(0, 0) = 1;
    kern.at<char>(0, 1) = -1;
    kern.at<char>(0, 2) = 0;
    kern.at<char>(1, 0) = -1;
    kern.at<char>(1, 1) = 5;
    kern.at<char>(1, 2) = -1;
    kern.at<char>(2, 0) = 0;
    kern.at<char>(2, 1) = -1;
    kern.at<char>(2, 2) = 0;

    Mat dst;
    filter2D(mat, dst, mat.depth(),kern);

    jint *ptr = dst.ptr<jint>(0);
    int size = w*h;
    jintArray  result = env->NewIntArray(size);
    env->SetIntArrayRegion(result,0,size,ptr);
    env->ReleaseIntArrayElements(pixels_, pixels, 0);
    return result;
}




//JNIEXPORT jintArray JNICALL
//Java_com_chenli_commenlib_jni_OpencvNative_filterPicture(JNIEnv *env, jobject instance,
//                                                         jintArray pixels_, jint w, jint h) {
//    jint *pixels = env->GetIntArrayElements(pixels_, NULL);
//
//    // TODO
//
//    env->ReleaseIntArrayElements(pixels_, pixels, 0);
//}


JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_OpencvNative_blurHalfPicture(JNIEnv *env, jobject instance,
                                                           jintArray pixels_, jint w, jint h) {
    jint *pixels = env->GetIntArrayElements(pixels_, NULL);
    Mat srcDada(h,w,CV_8UC4,(uchar*)pixels);
    Mat dstData = srcDada.rowRange(0,h/3);
    cv::GaussianBlur(dstData,dstData,cv::Size(7,7),0,0);
    int size = w*h;
    jintArray  result = env->NewIntArray(size);
    env->SetIntArrayRegion(result,0, size,pixels);
    env->ReleaseIntArrayElements(pixels_, pixels, 0);
    return result;
}


}

