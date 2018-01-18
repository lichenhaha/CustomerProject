package com.chenli.mvpapp.login;

/**
 * Created by Administrator on 2018/1/11.
 */

public interface LoginView {

    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();
}
