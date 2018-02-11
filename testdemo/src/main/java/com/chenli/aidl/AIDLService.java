package com.chenli.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/10.
 */

public class AIDLService extends Service {

    //private List<Book> bookList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //bookList = new ArrayList<>();
        initData();
    }

    private void initData() {
//        Book book1 = new Book("活着");
//        Book book2 = new Book("或者");
//        Book book3 = new Book("叶应是叶");
//        Book book4 = new Book("https://github.com/leavesC");
//        Book book5 = new Book("http://www.jianshu.com/u/9df45b87cfdf");
//        Book book6 = new Book("http://blog.csdn.net/new_one_object");
//        bookList.add(book1);
//        bookList.add(book2);
//        bookList.add(book3);
//        bookList.add(book4);
//        bookList.add(book5);
//        bookList.add(book6);
    }



}
