#include <jni.h>
#include "android/log.h"
#include <iostream>
#include <unistd.h>

using namespace std;

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "chenli", __VA_ARGS__)


extern "C" {

#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/avfilter.h>

void callJavaShowProgress(JNIEnv *env, jobject obj , char* message){
    jclass clazz = env->GetObjectClass(obj);
    jmethodID  methodId = env->GetMethodID(clazz,"showProgresstDialog","(Ljava/lang/String;)V");
    jstring msg = env->NewStringUTF(message);
    env->CallVoidMethod(obj,methodId,msg);
}

JNIEXPORT jint JNICALL
Java_com_chenli_jni_JNIActivity_getInt(JNIEnv *env, jobject instance, jstring name_, jstring pwd_,
                                       jint payInt) {
    const char *name = env->GetStringUTFChars(name_, 0);
    const char *pwd = env->GetStringUTFChars(pwd_, 0);

    int result1 =  strcmp(name,"abc");
    int result2 = strcmp(pwd,"123");

    env->ReleaseStringUTFChars(name_, name);
    env->ReleaseStringUTFChars(pwd_, pwd);

    callJavaShowProgress(env,instance,"正在验证用户名和密码");

    sleep(2);
    callJavaShowProgress(env,instance,"正在联网验证支付");

    sleep(2);
    callJavaShowProgress(env,instance,"正在验证支付结果");

    sleep(2);

    if (result1 == 0 && result2 == 0){
        if (payInt < 500){
            return 1;
        } else{
            return 0;
        }
    } else{
        return -1;
    }
}


JNIEXPORT jstring JNICALL
Java_com_chenli_commenlib_jni_JNICall_getStringFromJNI(JNIEnv *env, jclass type) {

    string str = "hello from c++ ";
    LOGE("++++++++++++");
    return env->NewStringUTF(str.c_str());
}

JNIEXPORT jintArray JNICALL
Java_com_chenli_commenlib_jni_JNICall_getIntArrayFromJNI(JNIEnv *env, jclass type) {
    jintArray intArray = env->NewIntArray(2);
    int size[] = {600, 900};
    env->SetIntArrayRegion(intArray, 0, 2, size);
    return intArray;
}


JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_setPersonToJNI(JNIEnv *env, jclass type, jobject person) {
    jclass jperson = env->GetObjectClass(person);
    if (jperson != NULL){
        jfieldID  ageFieldId = env->GetFieldID(jperson, "age", "I");
        jfieldID  nameFieldId = env->GetFieldID(jperson,"name", "Ljava/lang/String;");
        jint age = env->GetIntField(person,ageFieldId);
        jstring name = (jstring) env->GetObjectField(person, nameFieldId);
        const char* c_name =  env->GetStringUTFChars(name, NULL);
        LOGE("age ===== %d, name ===== %s", age, c_name);
    }
}

JNIEXPORT jobject JNICALL
Java_com_chenli_commenlib_jni_JNICall_getPersonFromJNI(JNIEnv *env, jclass type) {
    jclass jstudent = env->FindClass("com/chenli/commenlib/jni/Person");
    jmethodID  methodId = env->GetMethodID(jstudent, "<init>","(ILjava/lang/String;)V");
    jstring  name = env->NewStringUTF("john");
    jobject jobj = env->NewObject(jstudent, methodId, 20, name);
    return jobj;
}

JNIEXPORT jstring JNICALL
Java_com_chenli_commenlib_jni_JNICall_getStringFromProtocol(JNIEnv *env, jclass type) {
    av_register_all();
    char info[40000] = {0};
    struct URLProtocol *pup = NULL;
    struct URLProtocol **p_temp = &pup;
    avio_enum_protocols((void **)p_temp,0);
    while ((*p_temp) != NULL){
        sprintf(info, "%s[In ][%10s]\n", info, avio_enum_protocols((void **)p_temp, 0));
    }
    pup = NULL;
    avio_enum_protocols((void **)p_temp, 1);
    while ((*p_temp) != NULL){
        sprintf(info, "%s[Out][%10s]\n", info, avio_enum_protocols((void **)p_temp, 1));
    }
    return env->NewStringUTF(info);
}

JNIEXPORT jstring JNICALL
Java_com_chenli_commenlib_jni_JNICall_getStringFormatInfo(JNIEnv *env, jclass type) {
    char info[40000] = { 0 };
    av_register_all();
    AVInputFormat *if_temp = av_iformat_next(NULL);
    AVOutputFormat *of_temp = av_oformat_next(NULL);
    while(if_temp!=NULL){
        sprintf(info, "%s[In ][%10s]\n", info, if_temp->name);
        if_temp=if_temp->next;
    }
    while (of_temp != NULL){
        sprintf(info, "%s[Out][%10s]\n", info, of_temp->name);
        of_temp = of_temp->next;
    }
    return env->NewStringUTF(info);
}

JNIEXPORT jstring JNICALL
Java_com_chenli_commenlib_jni_JNICall_getstringAvcodeInfo(JNIEnv *env, jclass type) {
    char info[40000] = { 0 };
    av_register_all();
    AVCodec *c_temp = av_codec_next(NULL);
    while(c_temp!=NULL){
        if (c_temp->decode!=NULL){
            sprintf(info, "%s[Dec]", info);
        }
        else{
            sprintf(info, "%s[Enc]", info);
        }
        switch (c_temp->type){
            case AVMEDIA_TYPE_VIDEO:
                sprintf(info, "%s[Video]", info);
                break;
            case AVMEDIA_TYPE_AUDIO:
                sprintf(info, "%s[Audio]", info);
                break;
            default:
                sprintf(info, "%s[Other]", info);
                break;
        }
        sprintf(info, "%s[%10s]\n", info, c_temp->name);
        c_temp=c_temp->next;
    }
    return env->NewStringUTF(info);
}

JNIEXPORT jstring JNICALL
Java_com_chenli_commenlib_jni_JNICall_getstringFilterInfo(JNIEnv *env, jclass type) {
    char info[40000] = { 0 };
    avfilter_register_all();
    AVFilter *f_temp = (AVFilter *)avfilter_next(NULL);
    while (f_temp != NULL){
        sprintf(info, "%s[%10s]\n", info, f_temp->name);
    }
    return env->NewStringUTF(info);
}


JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_getMediaInfo(JNIEnv *env, jobject instance,
                                                   jstring srcPath_) {
    const char *srcPath = env->GetStringUTFChars(srcPath_, 0);
    av_register_all();
    AVFormatContext *ic = avformat_alloc_context();
    if (avformat_open_input(&ic, srcPath, NULL, NULL) != 0){
        LOGE("could not open source %s", srcPath);
        return;
    }
    if(avformat_find_stream_info(ic,NULL) < 0){
        LOGE("could not find stream information");
        return;
    }

    int videoStream = -1;
    int audioStream = -1;
    for (int i = 0; i < ic->nb_streams; ++i) {
        if (ic->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO){
            videoStream = i;
            break;
        }
    }
    if (videoStream == -1){
        return;
    }
    AVCodecContext* pCodecCtx = ic->streams[videoStream]->codec;
    AVStream *stream = ic->streams[videoStream];

    LOGE("----------------------dumping stream info-------------------");
    LOGE("input frmat :%s", ic->iformat->name);
    LOGE("nb_stream :%d", ic->nb_streams);
    LOGE("start_time: %lld", ic->start_time/AV_TIME_BASE);
    LOGE("duration :%lld s",ic->duration/AV_TIME_BASE);

    LOGE("video nb_frames : %lld", stream->nb_frames);
    LOGE("video codec_id: %d", pCodecCtx->codec_id);
    LOGE("video codec_name %s",avcodec_get_name(pCodecCtx->codec_id));
    LOGE("video width x height: %d x %d", pCodecCtx->width, pCodecCtx->height);
    LOGE("video pix_fmt:%d",pCodecCtx->pix_fmt);
    LOGE("video bitrate %lld kb/s", pCodecCtx->bit_rate/1000);
    LOGE("video avg_frame_rate : %d fps",
         stream->avg_frame_rate.num/stream->avg_frame_rate.den);


    for (int i = 0; i < ic->nb_streams; ++i) {
        if (ic->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO){
            audioStream = i;
            break;
        }
    }

    if (audioStream == -1){
        return;
    }
    AVStream *audio_stream = ic->streams[audioStream];
    AVCodecContext *audioCtx = ic->streams[audioStream]->codec;
    LOGE("audio codec_id %d",audio_stream->codec->codec_id);
    LOGE("audio codec_name %s", avcodec_get_name(audioCtx->codec_id));
    LOGE("audio sample_rate :%d ",audioCtx->sample_rate);
    LOGE("audio channels %d", audioCtx->channels);
    LOGE("audio channels_layout &d" , audioCtx->channel_layout);
    LOGE("audio sample_fmt %d", audioCtx->sample_fmt);
    LOGE("audio frame_size %d", audioCtx->frame_size);
    LOGE("audio nb_frames &lld", audio_stream->nb_frames);
    LOGE("audio bitrate %lld kb/s", audioCtx->bit_rate/1000);

    LOGE("--------------------end------------------------");
    avformat_close_input(&ic);

    env->ReleaseStringUTFChars(srcPath_, srcPath);
}


}
