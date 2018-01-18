package com.chenli.testmvp.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/1/10.
 */

public interface WeatherServiceApi {

    //请求的接口地址：http://api.avatardata.cn/Weather/Query?key=75bfe88f27a34311a41591291b7191ce&cityname=%E9%95%BF%E6%B2%99
    @GET("Weather/Query?")
    Observable<String> loadWeatherInfo(@Query("key") String key, @Query("cityname") String cityname);

}
