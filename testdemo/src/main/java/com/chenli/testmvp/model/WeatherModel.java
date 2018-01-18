package com.chenli.testmvp.model;

import com.chenli.testmvp.base.IBaseRequestCallBack;

/**
 * Created by Administrator on 2018/1/10.
 */

public interface WeatherModel<T> {
    void loadWeather(String city,String key ,IBaseRequestCallBack<T> iBaseRequestCallBack );
    void onUnsubscribe();
}
