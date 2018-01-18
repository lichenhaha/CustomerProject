package com.chenli.mvpapp.login;

/**
 * Created by Administrator on 2018/1/11.
 */

public class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener {

    private LoginView loginView;
    private LoginInteractor loginInteractor;
    public LoginPresenterImpl(LoginView loginView){
        this.loginView = loginView;
        loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentials(String username, String password) {
        if (loginView != null){
            loginView.showProgress();
        }
        loginInteractor.login(username,password,this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onUsernameError() {
        if (loginView != null){
            loginView.hideProgress();
            loginView.setUsernameError();
        }
    }

    @Override
    public void onPasswordError() {
        if (loginView != null){
            loginView.hideProgress();
            loginView.setPasswordError();
        }
    }

    @Override
    public void onSuccess() {
        if (loginView != null){
            loginView.navigateToHome();
        }
    }
}
