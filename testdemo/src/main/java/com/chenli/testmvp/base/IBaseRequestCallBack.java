package com.chenli.testmvp.base;

/**
 * Created by Administrator on 2018/1/10.
 */

public interface IBaseRequestCallBack<T> {
    void beforeRequest();
    void requestError(Throwable throwable);
    void requestComplete();
    void requestSuccess(T callback);
}
