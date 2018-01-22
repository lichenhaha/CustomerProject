package com.chenli.testdesigner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.commenlib.util.mainutil.WifiSearcher;
import com.chenli.testmvp.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/22.
 */

public class WifiActivity extends AppCompatActivity{

    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.textview1)
    TextView textview1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_layout);
        ButterKnife.bind(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomething1();
            }
        });
    }

    private void doSomething1() {
        WifiSearcher searcher = new WifiSearcher(this, new WifiSearcher.SearchWifiListener() {
            @Override
            public void onSearchWifiFailed(WifiSearcher.ErrorType errorType) {
                LogUtils.e("chenli",errorType.name());
            }

            @Override
            public void onSearchWifiSuccess(List<String> results) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < results.size(); i++) {
                    builder.append(results.get(i));
                    builder.append("\n");
                }
                textview1.setText(builder.toString());
            }
        });
        searcher.search();
    }
}
