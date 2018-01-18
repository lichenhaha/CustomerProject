#include <jni.h>
#include "android/log.h"
#include <iostream>
#include <unistd.h>

using namespace std;

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "hangce", __VA_ARGS__)



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

}
