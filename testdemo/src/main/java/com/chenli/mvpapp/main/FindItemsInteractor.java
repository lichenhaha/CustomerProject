package com.chenli.mvpapp.main;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public interface FindItemsInteractor {

    interface OnFinishedListener{
        void onFinished(List<String> items);
    }
    void findItems(OnFinishedListener listener);
}
