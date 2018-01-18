package com.chenli.testmvp.model;

import android.content.Context;

import com.chenli.testmvp.api.WeatherServiceApi;
import com.chenli.testmvp.base.BaseModel;
import com.chenli.testmvp.base.IBaseRequestCallBack;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2018/1/10.
 */

public class WeatherModelImp extends BaseModel implements WeatherModel<String> {

    private final WeatherServiceApi weatherServiceApi;
    private Observable<String> observable;
    private Context context ;

    public WeatherModelImp(Context context){
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.avatardata.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        weatherServiceApi = retrofit.create(WeatherServiceApi.class);
    }

    @Override
    public void loadWeather(String city, String key, final IBaseRequestCallBack<String> iBaseRequestCallBack) {
        observable = weatherServiceApi.loadWeatherInfo(key, city);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onNext(@NonNull String str) {
                        iBaseRequestCallBack.requestSuccess(str);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iBaseRequestCallBack.requestError(e);
                    }

                    @Override
                    public void onComplete() {
                        iBaseRequestCallBack.requestComplete();
                    }
                });

    }

    @Override
    public void onUnsubscribe() {
        
    }
}
