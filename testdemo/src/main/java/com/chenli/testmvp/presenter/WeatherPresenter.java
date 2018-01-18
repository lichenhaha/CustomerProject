package com.chenli.testmvp.presenter;

/**
 * Created by Administrator on 2018/1/10.
 */

public interface WeatherPresenter {

    void loadWeather(String key , String city);
    void unSubscribe();
}
