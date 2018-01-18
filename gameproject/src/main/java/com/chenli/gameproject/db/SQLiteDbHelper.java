package com.chenli.gameproject.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chenli.commenlib.util.mainutil.LogUtils;

import java.util.logging.Logger;

/**
 * Created by Administrator on 2018/1/16.
 */

public class SQLiteDbHelper extends SQLiteOpenHelper {


    public SQLiteDbHelper(Context context) {
        super(context, "testdb", null, 1, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table sqlitetest (uid long, uname varchar(255))");
        LogUtils.e("hangce","already create a database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
