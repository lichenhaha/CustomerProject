package com.chenli.mvpapp.login;

/**
 * Created by Administrator on 2018/1/11.
 */

public interface LoginPresenter {
    void validateCredentials(String username, String password);
    void onDestroy();
}
