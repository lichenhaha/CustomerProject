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

JavaVM *gJavaVM = NULL;
jobject gJavaObj = NULL;
static volatile int gIsThreadExit = 0;

pthread_mutex_t mutex;
int number;

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
//    env->GetJavaVM(&gJavaVM);
//    gJavaObj = (jobject *) env->NewGlobalRef(instance);
}

void* native_thread_exec(void* arg){
//    JNIEnv *ent;
//    gJavaVM->AttachCurrentThread(&ent,NULL);
//    jclass javaClass = ent->GetObjectClass((jobject) gJavaObj);
//    if (javaClass == NULL){
//        //LOGE("Fail to find javaClass");
//        return 0;
//    }
//
//    jmethodID  methodID = ent->GetMethodID(javaClass,"onNativeCallback","(I)V");
    int count = 0;
    while (gIsThreadExit != 0){
        //ent->CallVoidMethod((jobject) gJavaObj, methodID,count++);
        LOGE("----------------%d--------------------", count);
        sleep(1);

        while (gIsThreadExit != 1){
            if (gIsThreadExit == 2){
                sleep(1);
            } else{
                break;
            }
        }

    }
//    gJavaVM->DetachCurrentThread();

    //必须调用否则出现anr异常，线程无法结束，android界面无法响应其他事件，通道被占用。
    pthread_exit(0);
    LOGE("native_thread_exec loop leave");
}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_Native_nativeThreadStart(JNIEnv *env, jobject instance) {
   if (gIsThreadExit == 0){
       gIsThreadExit = 1 ;
       pthread_t  threadId ;
       if (pthread_create(&threadId,NULL,native_thread_exec,NULL) != 0){
           return;
       }
       LOGI("native_thread_start success");
   } else if (gIsThreadExit == 2){
       gIsThreadExit = 1;
   }
}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_Native_nativeThreadPause(JNIEnv *env, jobject instance) {
    gIsThreadExit = 2;
    // TODO

}

JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_Native_nativeThreadStop(JNIEnv *env, jobject instance) {
    gIsThreadExit = 0 ;
    LOGE("native_thread_stop success");
}

void *thread_fun(void* args){
    JNIEnv *env;
    jclass cls ;
    jmethodID mid;

    if ((gJavaVM->AttachCurrentThread(&env,NULL)) != JNI_OK){
        LOGE("-----  有误  ------");
        return NULL;
    }

    cls = env->GetObjectClass(gJavaObj);
    if (cls == NULL){
        LOGE("findClass error");
        goto error;
    }
    mid = env->GetStaticMethodID(cls,"fromJNI","(I)V");
    if (mid==NULL){
        LOGE(" error");
        goto error;
    }
    env->CallStaticVoidMethod(cls,mid,(int)args);

    error:
        if (gJavaVM->DetachCurrentThread() != JNI_OK){
            LOGE("-------detachCurrentThread--------");
        }
        pthread_exit(0);

}

void* thread_callback(void* args){
    pthread_mutex_lock(&mutex);
    char* ch = (char *) args;
    for (number = 0; number < 5; ++number) {
        LOGE("%s thread, i: %d\n", ch, number);
        sleep(1);
    }
    number = 0 ;
    LOGE("---------------");
    pthread_mutex_unlock(&mutex);
    pthread_exit((void *) pthread_self());
}


JNIEXPORT void JNICALL
Java_com_chenli_commenlib_jni_JNICall_callPthread(JNIEnv *env, jobject instance) {
    env->GetJavaVM(&gJavaVM);
    gJavaObj = env->NewGlobalRef(instance);

    pthread_t tid1,tid2;
    pthread_mutex_init(&mutex,NULL);
    pthread_create(&tid1, NULL, thread_callback, (void *) "chenli1");
    pthread_create(&tid2,NULL,thread_callback,(void*)"chenli2");


//    int i ;
//    pthread_t pt[5];
//    for (i = 0; i < 5; ++i) {
//        pthread_create(&pt[i],NULL,thread_fun,(void*)i);
//    }
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM * vm, void *reserved){
    JNIEnv* env = NULL;
    jint result = -1 ;

    if ((vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK)){
        LOGE("getenv failed");
        return result;
    }

    LOGE("-------JNI_OnLoad-----------");
    return JNI_VERSION_1_4;
}


}


