package com.chenli.gameproject;

import android.app.Application;

import com.chenli.commenlib.util.mainutil.Utils;

/**
 * Created by Administrator on 2018/1/16.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
