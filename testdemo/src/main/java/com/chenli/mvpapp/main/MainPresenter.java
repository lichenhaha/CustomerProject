package com.chenli.mvpapp.main;

/**
 * Created by Administrator on 2018/1/11.
 */

public interface MainPresenter {
    void onResume();
    void onItemClicked(int position);
    void onDestroy();
}
