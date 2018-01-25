#include <jni.h>
#include <android/log.h>
#include "gif_lib.h"
#include <stdlib.h>
#include <malloc.h>
#include <string>
#include <android/bitmap.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "chenli", __VA_ARGS__)
#define  argb(a,r,g,b) ( ((a) & 0xff) << 24 ) | ( ((b) & 0xff) << 16 ) | ( ((g) & 0xff) << 8 ) | ((r) & 0xff)

//class GifBean{
//public:
//    int current_frame;
//    int total_frame;
//    int  *dealys;
//};

typedef struct GifBean{
    int current_frame;
    int total_frame;
    int  *dealys;
} GifBean;


extern "C"{

/**
 * 初始化
 */
JNIEXPORT jlong JNICALL
Java_com_chenli_commenlib_gif_GifLib_setGifSource(JNIEnv *env, jobject instance, jstring srcPath_) {
    const char *srcPath = env->GetStringUTFChars(srcPath_, 0);
    GifFileType *gifFileType;
    int error = -1;
    if((gifFileType = DGifOpenFileName(srcPath,&error)) == NULL){
        LOGE("打开失败");
        return  0;
    }
    DGifSlurp(gifFileType);
    GifBean *gifBean = (GifBean *) malloc(sizeof(GifBean));
    //清空内存地址
    memset(gifBean,0, sizeof(GifBean));
    gifFileType->UserData = gifBean;
    gifBean->dealys = (int *) malloc(sizeof(int) * gifFileType->ImageCount);
    memset(gifBean->dealys, 0, sizeof(int) * gifFileType->ImageCount);
    gifBean->total_frame = gifFileType->ImageCount;
    ExtensionBlock* ext;
    for (int i = 0; i < gifFileType->ImageCount; ++i) {
        SavedImage frame = gifFileType->SavedImages[i];
        for (int j = 0; j < frame.ExtensionBlockCount; ++j) {
            if (frame.ExtensionBlocks[j].Function == GRAPHICS_EXT_FUNC_CODE) {
                ext = &frame.ExtensionBlocks[j];
                break;
            }
        }
        if (ext) {
            int frame_delay = 10 * (ext->Bytes[2] << 8 | ext->Bytes[1]);
            LOGE("时间  %d   ",frame_delay);
            gifBean->dealys[i] = frame_delay;
        }
    }
    LOGE("gif  长度大小    %d  ",gifFileType->ImageCount);
    env->ReleaseStringUTFChars(srcPath_, srcPath);
    //因为这个结构体指针就是一个数值，指针也是值，所以强制转换成long类型的数据返回。
    return (jlong) gifFileType;
}

JNIEXPORT jint JNICALL
Java_com_chenli_commenlib_gif_GifLib_getWidth(JNIEnv *env, jobject instance, jlong ndkGif) {
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    return gifFileType->SWidth;
}

JNIEXPORT jint JNICALL
Java_com_chenli_commenlib_gif_GifLib_getHeight(JNIEnv *env, jobject instance, jlong ndkGif) {
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    return gifFileType->SHeight;
}


/**
 * 绘制图片
 * @param pType
 * @param pBean
 * @param info
 * @param pVoid
 */
void drawFrame(GifFileType *gifFileType, GifBean *gifBean, AndroidBitmapInfo info, void *pixels) {
    SavedImage savedImage = gifFileType->SavedImages[gifBean->current_frame];
    GifImageDesc frameInfo = savedImage.ImageDesc;
    int* px = (int *) pixels;
    int *line ;
    int pointPixel ;
    GifByteType  gifByteType;
    GifColorType gifColorType;
    ColorMapObject* colorMapObject=frameInfo.ColorMap;
    px = (int *) ((char*)px + info.stride * frameInfo.Top);
    for (int y =frameInfo.Top; y < frameInfo.Top+frameInfo.Height; ++y) {
        line=px;
        for (int x = frameInfo.Left; x< frameInfo.Left + frameInfo.Width; ++x) {
            pointPixel = (y - frameInfo.Top) * frameInfo.Width + (x - frameInfo.Left);
            gifByteType = savedImage.RasterBits[pointPixel];
            gifColorType = colorMapObject->Colors[gifByteType];
            line[x] = argb(255,gifColorType.Red, gifColorType.Green, gifColorType.Blue);
        }
        px = (int *) ((char*)px + info.stride);
    }
}


JNIEXPORT jint JNICALL
Java_com_chenli_commenlib_gif_GifLib_updateFrame(JNIEnv *env, jobject instance,jlong ndkGif, jobject bitmap) {
    GifFileType *gifFileType = (GifFileType *) ndkGif;
    GifBean *gifBean = (GifBean *) gifFileType->UserData;
    AndroidBitmapInfo info;
    void *pixels ;
    AndroidBitmap_getInfo(env, bitmap , &info);
    //锁定bitmap
    AndroidBitmap_lockPixels(env,bitmap,&pixels);
    drawFrame(gifFileType,gifBean,info,pixels);
    gifBean->current_frame+=1;
    LOGE("当前帧  %d  ",gifBean->current_frame);
    if (gifBean->current_frame>=gifBean->total_frame-1){
        gifBean->current_frame = 0;
        LOGE("重新过来  %d  ",gifBean->current_frame);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
    return gifBean->dealys[gifBean->current_frame];
}


}




