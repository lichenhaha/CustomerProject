#include <jni.h>
#include "android/log.h"
#include <iostream>
#include <unistd.h>
#include <pthread.h>

using namespace std;

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "chenli", __VA_ARGS__)


extern "C" {

#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/avfilter.h>


char srcPath[256] = {0};
static volatile int globelFlag = -1 ;

static volatile  int num = 0 ;

//JavaVM *javaVM;
//jobject *javaObj;



void* native_thread_start(void* args){

    av_register_all();
    AVFormatContext *ic = avformat_alloc_context();
    //只读取文件头，并不会填充流信息
    if (avformat_open_input(&ic, srcPath, NULL, NULL) != 0){
        LOGE("could not open source %s", srcPath);
        return NULL;
    }
    if(avformat_find_stream_info(ic,NULL) < 0){
        LOGE("could not find stream information");
        return NULL;
    }

    int audioStream = -1;
    //int videoStream = -1;

    for (int i = 0; i < ic->nb_streams; ++i) {
//        if (ic->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO && videoStream<0){
//            videoStream = i;
//        }
        if (ic->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO & audioStream<0){
            audioStream = i;
            break;
        }
    }
//    if (videoStream == -1){
//        return;
//    }

    if (audioStream == -1){
        return NULL;
    }
    AVCodecContext* pCodecCtx = ic->streams[audioStream]->codec;
    AVCodec* pCodec = avcodec_find_decoder(pCodecCtx->codec_id);
    if(pCodec == NULL){
        LOGE("unsuported codec\n");
        return NULL;
    }
    if (avcodec_open2(pCodecCtx,pCodec,NULL) < 0){
        LOGE("不能打开编解码器");
        return NULL;
    }
    AVFrame *avFrame = av_frame_alloc();
    if (avFrame == NULL){
        return NULL;
    }
    int ret;
    int got_frame = 0 ;
    AVPacket packet;
    globelFlag = 1;//播放

    while (av_read_frame(ic,&packet) >= 0){
        if (packet.stream_index == audioStream){
            ret = avcodec_decode_audio4(pCodecCtx,avFrame,&got_frame,&packet);
            if (ret > 0 && got_frame){
                int size = av_samples_get_buffer_size(avFrame->linesize,avFrame->channels,
                                                      avFrame->nb_samples,pCodecCtx->sample_fmt,0);
                LOGE("audioDecodec : %d", size);
            }
        }

        usleep(500000);//睡100毫秒

        if (globelFlag == 0 || globelFlag == -1){
            break;
        }

        while (globelFlag != 1){
            if (globelFlag == 2){
                //暂停
                sleep(1);//睡觉2秒
            } else if (globelFlag == 0){
                //停止
                break;
            }
        }
        av_free_packet(&packet);
    }

    av_frame_free(&avFrame);
    avcodec_close(pCodecCtx);
    avformat_close_input(&ic);

    //退出线程
    pthread_exit(0);


//    while (globelFlag != -1){
//        usleep(200000);//睡100毫秒
//        LOGE("--------------- %d -----------",num++);
//        while (globelFlag != 1){
//            if (globelFlag == 2){
//                //暂停
//                sleep(1);//睡觉2秒
//            } else if (globelFlag == 0){
//                //停止
//                globelFlag = -1;
//                break;
//            }
//        }
//    }

//    while (globelFlag == 1){
//        usleep(500000);//睡100毫秒
//        LOGE("--------------- %d -----------",num++);
//    }
//
//    LOGE("----------end-------------");
//    //javaVM->DetachCurrentThread();
//    pthread_detach(pthread_self());

}


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
    env->ReleaseStringUTFChars(srcPath_,srcPath);
}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_setDataSource(JNIEnv *env, jobject instance,
                                                    jstring srcPath_) {
    const char* ch = env->GetStringUTFChars(srcPath_, 0);
    int len = strlen(ch);

    memcpy(srcPath, ch, len);//复制字符串
    LOGE("++++++ memcpy  len = %d , %s , %s +++++++" , len,ch,srcPath);


//    env->GetJavaVM(&javaVM);
//    javaObj = (jobject *) env->NewGlobalRef(instance);
    env->ReleaseStringUTFChars(srcPath_, ch);
}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_pauseAudioPlayer(JNIEnv *env, jobject instance) {
    globelFlag = 2;
    LOGE("pause");
}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_stopAudioPlayer(JNIEnv *env, jobject instance) {
    globelFlag = 0;
    LOGE("stop");

}


JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_startAudioPlayer(JNIEnv *env, jobject instance) {
    if (globelFlag == 2){
        globelFlag = 1;
        LOGE("+++++从pause到start++++");
    } else if (globelFlag == 0 || globelFlag == -1){
        globelFlag = 1;
        pthread_t  thread_Id;
        if (pthread_create(&thread_Id,NULL,native_thread_start,NULL) != 0){
            return;
        }
        LOGE("native_thread_start success");
    } else{
        globelFlag = 1;
    }

}

}
