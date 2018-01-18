package com.chenli.commenlib.mvp.model;

/**
 * Created by Administrator on 2018/1/13.
 */

public interface IBaseRequestCallBack<T> {
    void beforeRequest();
    void requestError(Throwable throwable);
    void requestComplete();
    void requestSuccess(T callback);
}
