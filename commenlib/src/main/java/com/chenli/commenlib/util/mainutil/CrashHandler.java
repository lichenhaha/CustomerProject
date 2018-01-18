package com.chenli.commenlib.util.mainutil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/18.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ryg_test/log/";
    public static final String FILE_NAME = "crash";

    public static final String FILE_NAME_SUFFIX = ".txt";

    public static CrashHandler instance = new CrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    private CrashHandler(){
    }

    public static CrashHandler getInstance(){
        return instance;
    }

    public void init(Context context){
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            dumpExceptionToSDCard(e);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        uploadExceptionToServer();
        e.printStackTrace();
        if (mDefaultCrashHandler != null){
            mDefaultCrashHandler.uncaughtException(t,e);
        }else {
            Process.killProcess(Process.myPid());
        }
    }

    private void uploadExceptionToServer() {
    }

    private void dumpExceptionToSDCard(Throwable e) throws IOException {
        File dir = new File(PATH);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        if (!file.exists()){
            file.createNewFile();
        }
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            e.printStackTrace(pw);
            pw.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        pw.print("MOdel: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);

    }
}
