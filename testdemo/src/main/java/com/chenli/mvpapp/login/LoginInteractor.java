package com.chenli.mvpapp.login;

/**
 * Created by Administrator on 2018/1/11.
 */

public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
    }

    void login(String username, String password, OnLoginFinishedListener listener);
}
