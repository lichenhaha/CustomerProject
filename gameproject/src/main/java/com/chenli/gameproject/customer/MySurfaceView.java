package com.chenli.gameproject.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.chenli.commenlib.util.mainutil.ConvertUtils;

import okhttp3.Interceptor;

/**
 * Created by Administrator on 2018/1/16.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback , Runnable{

    private Thread thread;
    private boolean flag;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;

    private float smallCenterX = 120;
    private float smallCenterY = 120;
    private float smallCenterR = 20;
    private float bigCenterX = 120;
    private float bigCenterY = 120;
    private float bigCenterR = 40;
    private int angle ;




    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(ConvertUtils.dp2px(16));
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
        flag = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }

    @Override
    public void run() {
        while (flag){
            long start = System.currentTimeMillis();
            drawSomeThing();
            logic();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 50){
                    Thread.sleep(50 - (end - start));
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void logic() {
    }

    private void drawSomeThing() {
        try {
            canvas = holder.lockCanvas();
            if (canvas != null){
                //清屏
                canvas.drawColor(Color.BLACK);

                paint.setAlpha(0x77);
                canvas.drawCircle(bigCenterX,bigCenterY,bigCenterR,paint);
                canvas.drawCircle(smallCenterX,smallCenterY,smallCenterR,paint);


            }
        }finally {
            if (canvas != null){
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void setSmallCircleXY(float centerX, float centerY, float R,double rad){
        smallCenterX = (float) (R*Math.cos(rad) + centerX);
        smallCenterY = (float) (R*Math.sin(rad) + centerY);
    }
}
