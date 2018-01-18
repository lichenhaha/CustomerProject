package com.chenli.testmvp.base;

/**
 * Created by Administrator on 2018/1/10.
 */

public interface IBaseView<T> {

    void showProgress();

    void disimissProgress();

    void loadDataSuccess(T tdata);

    void loadDataError(Throwable throwable);

}
