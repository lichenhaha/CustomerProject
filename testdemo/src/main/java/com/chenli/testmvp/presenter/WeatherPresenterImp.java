package com.chenli.testmvp.presenter;


import android.content.Context;

import com.chenli.testmvp.base.BasePresenterImp;
import com.chenli.testmvp.model.WeatherModelImp;
import com.chenli.testmvp.view.WeatherView;

/**
 * Created by Administrator on 2018/1/10.
 */

public class WeatherPresenterImp extends BasePresenterImp<WeatherView,String> implements WeatherPresenter {
    private Context context ;
    private final WeatherModelImp weatherModelImp;

    public WeatherPresenterImp(WeatherView view, Context context) {
        super(view);
        this.context = context;
        weatherModelImp = new WeatherModelImp(context);
    }

    @Override
    public void loadWeather(String key, String city) {
        weatherModelImp.loadWeather(city,key,this);
    }

    @Override
    public void unSubscribe() {
        weatherModelImp.onUnsubscribe();
    }
}
