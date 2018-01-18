package com.chenli.ounews.base;

import android.app.Application;

import com.chenli.commenlib.util.mainutil.Utils;


/**
 * Created by Administrator on 2018/1/12.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
