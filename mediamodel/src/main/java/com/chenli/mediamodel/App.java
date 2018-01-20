package com.chenli.mediamodel;


import android.app.Application;

import com.chenli.commenlib.util.mainutil.Utils;

/**
 * Created by Administrator on 2018/1/20 0020.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
