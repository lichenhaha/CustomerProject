package com.chenli.gameproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.gameproject.db.SQLiteDbHelper;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.ButtonCreate)
    Button buttonCreate;

    @Bind(R.id.ButtonInsert)
    Button buttonInsert;

    @Bind(R.id.ButtonQuery)
    Button buttonQuery;

    @Bind(R.id.ButtonUpdate)
    Button buttonUpdate;
    private SQLiteDbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        buttonCreate.setOnClickListener(this);
        buttonInsert.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonQuery.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ButtonCreate){
            helper = new SQLiteDbHelper(this);
            LogUtils.e("hangce","create");
        }else if (v.getId() == R.id.ButtonInsert){
            SQLiteDatabase database = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("uid",1);
            values.put("uname","zhangsan");
            database.insert("sqlitetest", null, values);
            LogUtils.e("hangce", "insert");
            database.close();
        }else if (v.getId() == R.id.ButtonUpdate){
            SQLiteDatabase database = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("uname","zhangsan_updata");
            database.update("sqlitetest",values, " uid = ? ", new String[]{"1"});
            LogUtils.e("hangce", "updata");
            database.close();
        }else if (v.getId() == R.id.ButtonQuery){
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.query("sqlitetest", new String[]{"uid", "uname"}, " uid=? ", new String[]{"1"}, null, null, null);
            while ( cursor != null && cursor.moveToNext()){
                String uname = cursor.getString(cursor.getColumnIndex("uname"));
                LogUtils.e("hangce","uname = " + uname);
            }
            cursor.close();
            database.close();
        }
    }
}
