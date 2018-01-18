package com.chenli.mvpapp.main;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public interface MainView {
    void showProgress();
    void hideProgress();
    void showMessage(String message);
    void setItems(List<String> items);
}
