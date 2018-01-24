package com.chenli.jni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenli.commenlib.jni.OpencvNative;
import com.chenli.testmvp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/24.
 */

public class OpenCvActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.imageview)
    ImageView imageView;
    @Bind(R.id.textview)
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv_layout);
        ButterKnife.bind(this);

        button1.setOnClickListener(this);
        imageView.setImageResource(R.drawable.pic);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pixels = new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            OpencvNative opencvNative = OpencvNative.getInstance();
            int[] grayPicture = opencvNative.grayPicture(pixels, w, h);
            Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            resultImg.setPixels(grayPicture,0,w,0,0,w,h);
            imageView.setImageBitmap(resultImg);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }
    }
}
