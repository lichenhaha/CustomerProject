package com.chenli.commenlib.rxbus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.subjects.Subject;

/**
 * Created by Administrator on 2018/1/11.
 */

public class RxBus {

    public static final String TAG = RxBus.class.getSimpleName();
    private static volatile RxBus instance;
    public static boolean DEBUG = false;

    private RxBus(){}

    public static RxBus get(){
        if (null == instance){
            synchronized (RxBus.class){
                if (null == instance){
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    private ConcurrentHashMap<Object,List<Subject>> subjectMapper = new ConcurrentHashMap<>();





}
