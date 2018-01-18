package com.chenli.mvpapp.main;

import com.chenli.commenlib.util.mainutil.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public class MainPresenterImpl implements MainPresenter, FindItemsInteractor.OnFinishedListener {
    private MainView mainView;
    private FindItemsInteractor findItemsInteractor;

    public MainPresenterImpl(MainView mainView){
        this.mainView = mainView;
        findItemsInteractor = new FindItemsInteractorImpl();
    }

    @Override
    public void onResume() {
        if (mainView != null){
            mainView.showProgress();
        }
        findItemsInteractor.findItems(this);
    }

    @Override
    public void onItemClicked(int position) {
        if (mainView != null){
            mainView.showMessage(String.format("Position %d click ",position+1));
        }
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onFinished(List<String> items) {
        LogUtils.e("hangce","--------------");
        if (mainView != null){
            mainView.setItems(items);
            mainView.hideProgress();
        }
    }
}
