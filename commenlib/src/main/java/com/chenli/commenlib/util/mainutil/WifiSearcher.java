package com.chenli.commenlib.util.mainutil;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/1/19.
 */

public class WifiSearcher {

    private static final int WIFI_SEARCH_TIMEOUT = 20; //扫描WIFI的超时时间
    private final ReentrantLock mLock;
    private final Condition mCondition;
    public SearchWifiListener mSearchWifiListener;
    private WiFiScanReceiver mWifiReceiver;
    private final WifiManager mWifiManager;
    private volatile boolean mIsWifiScanCompleted = false;
    private final Context mContext;

    public static enum ErrorType {
        SEARCH_WIFI_TIMEOUT, //扫描WIFI超时（一直搜不到结果）
        NO_WIFI_FOUND,       //扫描WIFI结束，没有找到任何WIFI信号
    }

    //扫描结果通过该接口返回给Caller
    public interface SearchWifiListener {
        public void onSearchWifiFailed(ErrorType errorType);
        public void onSearchWifiSuccess(List<String> results);
    }

    public WifiSearcher(Context context , SearchWifiListener listener){
        mContext = context;
        mLock = new ReentrantLock();
        mSearchWifiListener = listener;
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mCondition = mLock.newCondition();
        mWifiReceiver = new WiFiScanReceiver();
    }

    public void search(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!mWifiManager.isWifiEnabled()){
                    mWifiManager.setWifiEnabled(true);
                }
                IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                mContext.registerReceiver(mWifiReceiver,intentFilter);
                //开始扫描
                mWifiManager.startScan();
                mLock.lock();
                //阻塞等待扫描结果
                mIsWifiScanCompleted = false;
                try {
                    mCondition.await(WIFI_SEARCH_TIMEOUT, TimeUnit.SECONDS);
                    if (!mIsWifiScanCompleted){
                        mSearchWifiListener.onSearchWifiFailed(ErrorType.SEARCH_WIFI_TIMEOUT);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mLock.unlock();
                mContext.unregisterReceiver(mWifiReceiver);
            }
        });
    }

    protected class WiFiScanReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //提取扫描结果
            List<String> ssidResults = new ArrayList<>();
            List<ScanResult> scanResults = mWifiManager.getScanResults();
            for (ScanResult result : scanResults){
                ssidResults.add(result.SSID);
            }
            if (ssidResults.isEmpty()){
                mSearchWifiListener.onSearchWifiFailed(ErrorType.NO_WIFI_FOUND);
            }else {
                mSearchWifiListener.onSearchWifiSuccess(ssidResults);
            }
            mLock.lock();
            mIsWifiScanCompleted = true;
            mCondition.signalAll();
            mLock.unlock();
        }
    }




}
