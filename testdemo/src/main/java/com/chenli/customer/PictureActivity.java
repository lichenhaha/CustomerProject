package com.chenli.customer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;
import com.github.chrisbanes.photoview.PhotoView;

import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/2/8.
 */

public class PictureActivity extends AppCompatActivity {

    private PhotoView photo;
    private ImageView imageView;

    private float pointX ;
    private float pointY;
    private DisplayMetrics displayMetrics;
    private float heightRadio1;
    private float widthRadio1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_layout);


        displayMetrics = getResources().getDisplayMetrics();
        imageView = (ImageView) findViewById(R.id.image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomething();


//                LogUtils.e("chenli","imageView.getLeft() = " + imageView.getLeft() + " , imageView.getWidth()*1.0f/2 = " + imageView.getWidth()*1.0f/2);
//                LogUtils.e("chenli","imageView.getTop() = " + imageView.getTop() + " , imageView.getWidth()*1.0f/2 = " + imageView.getHeight()*1.0f/2);

            }
        });

        photo = (PhotoView) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimation();
            }
        });
    }

    private void doAnimation() {
        Drawable drawable = photo.getDrawable().getCurrent();
        Rect bounds = drawable.getBounds();
        heightRadio1 = imageView.getHeight()*1.0f/bounds.height();
        widthRadio1 = imageView.getWidth()*1.0f/bounds.width();
        pointX =imageView.getLeft();// +   imageView.getWidth()*1.0f/2;
        pointY = imageView.getTop();//+ imageView.getHeight()*1.0f/2;

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,widthRadio1,
                1.0f,heightRadio1,
                Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF, 0f);
        LogUtils.e("chenli","pointX = " + pointX + " , pointY = " + pointY + " , bounds.height()=" + bounds.height() + " , bounds.width() = " + bounds.width());

        AnimationSet set = new AnimationSet(true);
        set.setDuration(300);
        set.addAnimation(scaleAnimation);
        set.setInterpolator(new AccelerateDecelerateInterpolator());

        photo.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                photo.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                photo.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


    }

    private float heightRadio;
    private float widthRadio;
    private float radio ;
    private float radio1 ;
    private float radio2 ;

    private void doSomething() {
        photo.setVisibility(View.VISIBLE);
        photo.setBackgroundColor(Color.BLACK);
        photo.setImageResource(R.mipmap.img4);

//        float v = width * 1.0f / displayMetrics.widthPixels;
//        float v1 = height * 1.0f / displayMetrics.heightPixels;
//        if (v > v1){
//            radio2 = v1;
//        }else {
//            radio2 = v;
//        }
//
//        float v2 = width * 1.0f / imageView.getWidth();
//        float v3 = height * 1.0f / imageView.getHeight();
//        if (v2 > v3){
//            radio1 = v2;
//        }else {
//            radio1 = v3;
//        }
//
//        radio = radio1*radio2;

//        LogUtils.e("chenli","width = " + width + " , height = " + height + " , radio = " + radio + " , radio2 = "+ radio2 + " , radio1 = " + radio1);
    }

    @Override
    public void onBackPressed() {
        if (photo.getVisibility() != View.GONE){
            photo.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }

    }
}
