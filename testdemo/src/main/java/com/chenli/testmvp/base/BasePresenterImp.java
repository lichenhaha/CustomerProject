package com.chenli.testmvp.base;

/**
 * Created by Administrator on 2018/1/10.
 */

public class BasePresenterImp <V extends IBaseView,T> implements IBaseRequestCallBack<T> {

    private IBaseView iBaseView = null;  //基类视图

    public BasePresenterImp(V view){
        this.iBaseView = view;
    }

    @Override
    public void beforeRequest() {
        iBaseView.showProgress();
    }

    @Override
    public void requestError(Throwable throwable) {
        iBaseView.loadDataError(throwable);
        iBaseView.disimissProgress();
    }

    @Override
    public void requestComplete() {
        iBaseView.disimissProgress();
    }

    @Override
    public void requestSuccess(T callback) {
        iBaseView.loadDataSuccess(callback);
    }
}
