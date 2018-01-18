package com.chenli.testmvp;

import android.os.Environment;

import com.chenli.commenlib.util.mainutil.CrashHandler;
import com.chenli.commenlib.util.mainutil.CrashUtils;
import com.chenli.commenlib.util.mainutil.Utils;

/**
 * Created by Administrator on 2018/1/10.
 */

public class App extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);

//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ryg_test/log/";
//        CrashUtils.init(path);

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }
}
