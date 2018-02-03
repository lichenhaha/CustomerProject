package com.chenli.jni;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenli.commenlib.jni.OpencvNative;
import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/24.
 */

public class OpenCvActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.button1)
    Button button1;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.button3)
    Button button3;
    @Bind(R.id.button4)
    Button button4;
    @Bind(R.id.button5)
    Button button5;
    @Bind(R.id.button6)
    Button button6;
    @Bind(R.id.button7)
    Button button7;
    @Bind(R.id.button8)
    Button button8;
    @Bind(R.id.button9)
    Button button9;
    @Bind(R.id.button10)
    Button button10;
    @Bind(R.id.button11)
    Button button11;
    @Bind(R.id.button12)
    Button button12;
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
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        imageView.setImageResource(R.mipmap.img4);
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
        }else if (v.getId() == R.id.button2){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
            Bitmap resultImg = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            Canvas canvas = new Canvas(resultImg);
            Paint paint = new Paint();
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
            paint.setColorFilter(colorFilter);
            canvas.drawBitmap(bitmap,0,0,paint);
            imageView.setImageBitmap(resultImg);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }else if (v.getId() == R.id.button3){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
            OpencvNative.getInstance().grayPicture2(bitmap);
            imageView.setImageBitmap(bitmap);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }else if (v.getId() == R.id.button4){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pixels = new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            int[] ints = OpencvNative.getInstance().grayPicture1(pixels, w, h);
            Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            resultImg.setPixels(ints,0,w,0,0,w,h);
            imageView.setImageBitmap(resultImg);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }else if (v.getId() == R.id.button5){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img4);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pixels = new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            int[] result = OpencvNative.getInstance().blurPicture(pixels, w, h, 7);
            Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            resultImg.setPixels(result,0,w,0,0,w,h);
            imageView.setImageBitmap(resultImg);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }else if (v.getId() == R.id.button6){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img4);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pixels = new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            int[] result = OpencvNative.getInstance().cannyPicture(pixels, w, h, 7);
            Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            resultImg.setPixels(result,0,w,0,0,w,h);
            imageView.setImageBitmap(resultImg);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }else if (v.getId() == R.id.button7){
            Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    bitmap.setPixel(i,j, Color.parseColor("#8899AABB"));
                }
            }
            int[] pixels = new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            int[] resultData = OpencvNative.getInstance().dataTransition(pixels,w,h);
            Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            resultImg.setPixels(resultData,0,w,0,0,w,h);
            imageView.setImageBitmap(resultImg);
        }else if (v.getId() == R.id.button8){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img4);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pixels = new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            OpencvNative opencvNative = OpencvNative.getInstance();
            int[] grayPicture = opencvNative.filterPicture(pixels, w, h);
            Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            resultImg.setPixels(grayPicture,0,w,0,0,w,h);
            imageView.setImageBitmap(resultImg);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }else if (v.getId() == R.id.button9){
            long current = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img4);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int[] pixels = new int[w*h];
            bitmap.getPixels(pixels,0,w,0,0,w,h);
            OpencvNative opencvNative = OpencvNative.getInstance();
            int[] resultData = opencvNative.blurHalfPicture(pixels,w,h);
            Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            resultImg.setPixels(resultData,0,w,0,0,w,h);
            imageView.setImageBitmap(resultImg);
            long end = System.currentTimeMillis();
            textView.setText("消耗时间:" + (end-current));
        }else if (v.getId() == R.id.button9){

        }else if (v.getId() == R.id.button9){

        }
    }
}
