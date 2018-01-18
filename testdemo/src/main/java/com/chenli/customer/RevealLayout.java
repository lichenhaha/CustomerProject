package com.chenli.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/15.
 */

public class RevealLayout extends LinearLayout {

    private static final long INVALIDATE_DURATION = 4;
    private View mTouchTarget;
    private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //圆心
    private float  mCenterX, mCenterY ;
    //半径
    private int mRevealRadius = 0;
    private int[] mLocation = new int[2];
    // 目标的 宽度 和 高度
    private int mTargetHeight, mTargetWidth;
    // 圆的最大半径
    public int mMaxRadius;
    // 半径增加幅度
    private int mRevealRadiusGap;
    // 控件宽度
    private int mMinBetweenWidthAndHeight;
    // 是否按下状态
    private boolean mIsPressed;
    // 是否需要继续绘制
    private boolean mShouldDoAnimation;
    // 当前需要绘制的 view
    private View mTargetView;
    //手指按下与抬起 是在同一个View 上面。
    private boolean onOneView=true;

    public RevealLayout(Context context) {
        this(context,null);
    }

    public RevealLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RevealLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.parseColor("#3b4169E1"));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getLocationOnScreen(mLocation);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int  x = (int) ev.getRawX();
        int  y = (int) ev.getRawY();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            View targetView = getTouchTarget(this,x,y);
            if ( targetView != null && targetView.isClickable() && targetView.isEnabled()){
                mTouchTarget = targetView;
                initParametersForchild(ev, targetView);
                postInvalidateDelayed(INVALIDATE_DURATION);
            }
        }else if (action == MotionEvent.ACTION_UP){
            //判断手按下和抬起是在同一个View身上
            viewOnScreen(ev, this,x,y);
            mIsPressed = false;
            postInvalidateDelayed(INVALIDATE_DURATION);
            postDelayed(mDispatchUpTouchEventRunnable,400);
            return true;
        }else if (action == MotionEvent.ACTION_CANCEL){
            mIsPressed = false;
            postInvalidateDelayed(INVALIDATE_DURATION);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initParametersForchild(MotionEvent event, View view) {
        // 手指的 x ,y 点在屏幕中的 坐标
        mCenterX = event.getX();
        mCenterY = event.getY();
        // 手机所在 view 自身的 宽度
        mTargetWidth = view.getMeasuredWidth();
        // 手机所在 view 自身的 高度
        mTargetHeight = view.getMeasuredHeight();
        // 判断 宽度 和高度 那个值比较大
        mMinBetweenWidthAndHeight = Math.min(mTargetWidth,mTargetHeight);
        mRevealRadius = 0;
        mRevealRadiusGap = mMinBetweenWidthAndHeight / 8;
        mIsPressed = true;
        mShouldDoAnimation = true;
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0] - mLocation[0];
        int top = location[1] - mLocation[1];
        // view 距离左边界的宽度
        int mTransformedCenterX = (int) mCenterX - left;
        // view 距离顶部的距离
        int transformedCenterY = (int) mCenterY - top;
        // 根据 子view 的 宽度 和高度 获取 圆的 半径
        int maxX = Math.max(mTransformedCenterX, mTargetWidth - mTransformedCenterX);
        int maxY = Math.max(transformedCenterY, mTargetHeight - transformedCenterY);
        mMaxRadius = Math.max(maxX, maxY);
    }

    private View getTouchTarget(View view, int x, int y) {
        View target = null;
        ArrayList<View> touchables = view.getTouchables();
        for (View child : touchables) {
            if (isTouchPointInView(child,x,y)){
                target = child;
                break;
            }
        }
        return target;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth() ;
        int bottom = top + view.getMeasuredHeight();
        if (view.isClickable() && y >= top && y <= bottom && x >= left && x <= right){
            return true;
        }
        return false;
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;
        @Override
        public void run() {
            if (mTouchTarget == null || !mTouchTarget.isEnabled()) {
                return;
            }
            if (isTouchPointInView(mTouchTarget, (int)event.getRawX(), (int)event.getRawY())) {
                mTouchTarget.dispatchTouchEvent(event);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!mShouldDoAnimation || mTargetWidth <= 0 || mTouchTarget == null) {
            return;
        }
        if (mRevealRadius > mMinBetweenWidthAndHeight / 4){
            mRevealRadius += mRevealRadiusGap * 4;
        }else {
            mRevealRadius += mRevealRadiusGap;
        }
        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);
        getLocationOnScreen(mLocation);

        int left = location[0]- mLocation[0];
        int top = location[1] - mLocation[1];
        int right = left + mTouchTarget.getMeasuredWidth();
        int bottom = top + mTouchTarget.getMeasuredHeight();

        canvas.save();
        canvas.clipRect(left,top,right,bottom);
        canvas.drawCircle(mCenterX,mCenterY,mRevealRadius, mPaint);
        canvas.restore();

        // 如果当前半径 还没有超过 最大半径 表示 还没有覆盖整个button 还需要继续 画自己
        if (mRevealRadius <= mMaxRadius){
            postInvalidateDelayed(INVALIDATE_DURATION);
        }else if (!mIsPressed){
            mShouldDoAnimation = false;
            postInvalidateDelayed(INVALIDATE_DURATION);
            if (onCompletionListener != null){
                onCompletionListener.onComplete(mTargetView.getId());
            }
        }

    }

    /**
     * 判断手抬起跟放下是不是同一个View
     * @param event  手指抬起动作
     * @param view   抬起的view
     */
    public void viewOnScreen(MotionEvent event, View view,int x,int y){
        View upView = getTouchTarget(view, x, (int)y);
        if(mTouchTarget.equals(upView)&&(null!=upView)&&(mTouchTarget.getId()==upView.getId())){
            onOneView=true;
        }else{
            onOneView=false;
        }
    }

    // 绘制完成后的 监听
    private OnRippleCompleteListener onCompletionListener;
    public void setOnRippleCompleteListener(OnRippleCompleteListener listener) {
        this.onCompletionListener = listener;
    }
    public interface OnRippleCompleteListener {
        void onComplete(int id);
    }

}
