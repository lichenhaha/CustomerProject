package com.chenli.customer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by Administrator on 2018/1/15.
 */

public class CustomerDrag extends LinearLayout {
    private ViewDragHelper viewDragHelper;
    public CustomerDrag(Context context) {
        this(context,null);
    }
    public CustomerDrag(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomerDrag(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * @params ViewGroup forParent 必须是一个ViewGroup
         * @params float sensitivity 灵敏度
         * @params Callback cb 回调
         */
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new Callback());
    }



    class Callback extends ViewDragHelper.Callback {
        /**
         * 尝试捕获子view，一定要返回true
         * @param  child 尝试捕获的view
         * @param  pointerId 指示器id
         * 这里可以决定哪个子view可以拖动
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }
        /**
         * 处理水平方向上的拖动
         * @param child 被拖动到view
         * @param left 移动到达的x轴的距离
         * @param dx 建议的移动的x距离
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (getPaddingLeft() > left){
                return getPaddingLeft();
            }
            if (getWidth() - child.getWidth() -getPaddingLeft() < left){
                return getWidth() - child.getWidth() - getPaddingLeft();
            }
            return left;
        }

        /**
         *  处理竖直方向上的拖动
         * @param  child 被拖动到view
         * @param  top 移动到达的y轴的距离
         * @param  dy 建议的移动的y距离
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (getPaddingTop() > top){
                return getPaddingTop();
            }
            if (top > getHeight() - child.getHeight() - getPaddingBottom()){
                return getHeight() - child.getHeight() - getPaddingBottom();
            }
            return top;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_CANCEL:
                viewDragHelper.cancel();
                break;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}
