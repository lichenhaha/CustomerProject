package com.chenli.media;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ant.liao.GifView;
import com.chenli.testmvp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/24.
 */

public class GifActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.gifview1)
    GifView gifView;

    boolean f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_layout);
        ButterKnife.bind(this);

        gifView.setGifImage(R.drawable.gif1);
        gifView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (f){
            gifView.showCover();
            f = false;
        }else {
            gifView.showAnimation();
            f = true;
        }
    }
}
