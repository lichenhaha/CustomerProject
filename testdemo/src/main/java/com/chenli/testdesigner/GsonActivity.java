package com.chenli.testdesigner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/4.
 */

public class GsonActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button1)
    Button button1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson_test);
        ButterKnife.bind(this);

        button1.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        BoxDismountBean boxDismountBean = new BoxDismountBean();
        boxDismountBean.cntrnum = "12233";
        boxDismountBean.cntrtype = 1 ;
        boxDismountBean.creattime = "2107-2-4";
        boxDismountBean.id = 2 ;
        boxDismountBean.ispass = true;
        boxDismountBean.userid = "user";
        boxDismountBean.passcntr = 0 ;

        LogUtils.e("chenli",boxDismountBean.toString());

        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equals("cntrtype")|f.getName().equals("ispass");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        String json = gson.toJson(boxDismountBean);

        LogUtils.e("chenli",json);

    }
}
