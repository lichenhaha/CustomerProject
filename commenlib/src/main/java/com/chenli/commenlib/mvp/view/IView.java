package com.chenli.commenlib.mvp.view;

/**
 * Created by Administrator on 2018/1/13.
 */

public interface IView <T>{

    void showLoading();

    void hideLoading();

    void loadDataSuccess(T tdata);
    
    void loadDataError(Throwable throwable);

}
