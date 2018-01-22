#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include "pthread.h"


#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "chenli", __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "chenli", __VA_ARGS__)


using namespace std;

extern "C"{



JavaVM *gJavaVM;
jobject *gJavaObj;
static volatile int gIsThreadExit = 0;

#define TEST_BUFFER_SIZE 128




JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_nativeSetBuffer1(JNIEnv *env, jobject instance,
                                                       jbyteArray buffer_, jint len) {

    unsigned char array[128];
    env->GetByteArrayRegion(buffer_, 0, len, (jbyte *) array);
    int i = 0 ;
    for (i = 0; i < 128; ++i) {
        array[i] = i;
    }

}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_nativeSetBuffer2(JNIEnv *env, jobject instance,
                                                       jbyteArray buffer_, jint len) {
    jbyte *buffer = env->GetByteArrayElements(buffer_, NULL);
    int i = 0;
    for ( i = 0; i < TEST_BUFFER_SIZE; ++i) {
        buffer[i] = i;
    }
    env->ReleaseByteArrayElements(buffer_, buffer, 0);
}

JNIEXPORT jbyteArray JNICALL
Java_com_chenli_commenlib_jni_JNICall_nativeGetByteArray(JNIEnv *env, jobject instance) {
    jbyte buffer[128];
    int i = 0;
    for (i = 0; i < 128; ++i) {
        buffer[i] = i;
    }
    jbyteArray  array = env->NewByteArray(128);
    //将传递数据拷贝到java端
    env->SetByteArrayRegion(array,0,128,buffer);
    return array;
}


JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_Native_nativeInitilize(JNIEnv *env, jobject instance) {
    env->GetJavaVM(&gJavaVM);
    gJavaObj = (jobject *) env->NewGlobalRef(instance);
}

void* native_thread_exec(void* arg){
    JNIEnv *ent;
    gJavaVM->AttachCurrentThread(&ent,NULL);
    jclass javaClass = ent->GetObjectClass((jobject) gJavaObj);
    if (javaClass == NULL){
        //LOGE("Fail to find javaClass");
        return 0;
    }

    jmethodID  methodID = ent->GetMethodID(javaClass,"onNativeCallback","(I)V");
    int count = 0;
    while (!gIsThreadExit){
        ent->CallVoidMethod((jobject) gJavaObj, methodID,count++);
        sleep(1);
    }
    gJavaVM->DetachCurrentThread();
    LOGE("native_thread_exec loop leave");

}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_Native_nativeThreadStart(JNIEnv *env, jobject instance) {
    gIsThreadExit = 0 ;
    pthread_t  threadId ;
    if (pthread_create(&threadId,NULL,native_thread_exec,NULL) != 0){
        //LOGE("native_thread_start pthread_create fail");
        return;
    }
    //pthread_join(threadId,NULL);
    LOGI("native_thread_start success");

}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_Native_nativeThreadStop(JNIEnv *env, jobject instance) {
    gIsThreadExit = 1 ;
    LOGE("native_thread_stop success");
}

}


