package com.chenli.testdesigner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.chenli.testmvp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/12.
 */

public class TabActivity extends AppCompatActivity {

    @Bind(R.id.tablayout)
    TabLayout tablayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        ButterKnife.bind(this);

        tablayout.addTab(tablayout.newTab().setCustomView(R.layout.tab_item),true);
        tablayout.addTab(tablayout.newTab().setCustomView(R.layout.tab_item));
        tablayout.addTab(tablayout.newTab().setCustomView(R.layout.tab_item));
        tablayout.addTab(tablayout.newTab().setCustomView(R.layout.tab_item));

    }
}
