package com.chenli.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ant.liao.GifView;
import com.chenli.commenlib.gif.GifLib;
import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2018/1/24.
 */

public class GifActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button1)
    Button button1;

    @Bind(R.id.imageview)
    GifImageView imageview;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int frame = gifLib.updateFrame(gifLib.ndkGifPoint, bitmap);
            handler.sendEmptyMessageDelayed(1,frame);
            imageview.setImageBitmap(bitmap);
        }
    };

    private GifLib gifLib;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_layout);
        ButterKnife.bind(this);

        button1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        if (f){
//            gifView.showCover();
//            f = false;
//        }else {
//            gifView.showAnimation();
//            f = true;
//        }

//        String srcPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gif1.gif";
//        gifLib = GifLib.getInstance(srcPath);
//        int width = gifLib.getWidth(GifLib.ndkGifPoint);
//        int height = gifLib.getHeight(GifLib.ndkGifPoint);
//
//        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        int nextFrame = gifLib.updateFrame(gifLib.ndkGifPoint, bitmap);
//        handler.sendEmptyMessageDelayed(1,nextFrame);

    }



}
