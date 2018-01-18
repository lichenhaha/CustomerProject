package com.chenli.animation;

import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/16.
 */

public class AnimationActivity extends AppCompatActivity {

    @Bind(R.id.btn_animation1)
    Button btn_animation1;
    @Bind(R.id.btn_animation2)
    Button btn_animation2;
    @Bind(R.id.btn_animation3)
    Button btn_animation3;
    @Bind(R.id.btn_animation4)
    Button btn_animation4;

    @Bind(R.id.tv)
    TextView tv;

    @Bind(R.id.mypointview)
    MyPointView mypointview;

    @Bind(R.id.mytv)
    MyTextView mytv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);

        //final Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        btn_animation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimation2();
            }
        });

        //final Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.rotate);
        btn_animation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimation3();
            }
        });

//        final TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
//                Animation.RELATIVE_TO_SELF,100,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,100);
//        translateAnimation.setDuration(700);
//        translateAnimation.setFillAfter(true);

        btn_animation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimator1();
            }
        });
        btn_animation4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimator();
            }
        });
    }

    private void doAnimation3() {
        //mypointview.doPointAnim();
        PropertyValuesHolder holder = PropertyValuesHolder.ofObject("charText",new CharEvaluator(),new Character('A'),new Character('Z'));
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mytv,holder);
        animator.setDuration(5000);
        //animator.addUpdateListener(listener);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();

    }

    private void doAnimation2() {

        Keyframe keyframe1 = Keyframe.ofFloat(0f,0f);
        Keyframe keyframe2 = Keyframe.ofFloat(0.1f,-20f);
        Keyframe keyframe3 = Keyframe.ofFloat(1f,0f);

        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("rotation",keyframe1,keyframe2,keyframe3);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(tv,holder);
        animator.setDuration(2000);
        animator.start();





//        PropertyValuesHolder rotationHolder  = PropertyValuesHolder.ofFloat("rotation",60f, -60f, 40f, -40f, -20f, 20f, 10f, -10f, 0f);
//        PropertyValuesHolder colorHolder = PropertyValuesHolder.ofInt("BackgroundColor",0xffffffff, 0xffff00ff, 0xffffff00, 0xffffffff);
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(tv,rotationHolder,colorHolder);
//        animator.setDuration(3000);
//        animator.setInterpolator(new AccelerateInterpolator());
//        animator.start();


//        ObjectAnimator animator = ObjectAnimator.ofObject(tv,"text",new CharEvaluator(),new Character('A'),new Character('Z'));
//        animator.setDuration(10000);
//        animator.addUpdateListener(listener);
//        animator.start();
    }

    private void doAnimator1() {

        ObjectAnimator animator = ObjectAnimator.ofInt(mypointview,"pointRadius",0, 300, 100);
        animator.setDuration(1000);
        animator.start();

    }

    private class CharEvaluator implements TypeEvaluator<Character>{

        @Override
        public Character evaluate(float fraction, Character startValue, Character endValue) {
            int startInt = startValue;
            int endInt = endValue;
            int curInt = (int) (startInt + fraction*(endInt-startInt));
            char result = (char) curInt;
            return result;
        }
    }

    private void doAnimator() {
        ValueAnimator animator = ValueAnimator.ofObject(new CharEvaluator(),new Character('A'),new Character('Z'));
        animator.setDuration(10000);
        animator.addUpdateListener(listener);
//        animator.setEvaluator(new TypeEvaluator<Integer>() {
//
//            @Override
//            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
//                int startInt = startValue;
//                return (int)(200 + startInt + fraction * (endValue-startInt));
//            }
//        });

        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    private ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            char curValue = (char) animation.getAnimatedValue();
            //tv.layout(tv.getLeft(), curValue,tv.getRight(),curValue+tv.getHeight());
            //tv.setBackgroundColor(curValue);
            tv.setText(String.valueOf(curValue));
        }
    };

}
