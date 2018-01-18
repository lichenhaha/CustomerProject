package com.chenli.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.testmvp.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/1/16.
 */

public class BannerActivity extends AppCompatActivity {
    private ViewPager mBanner;
    private BannerAdapter mBannerAdapter;
    private ImageView[] mIndicators;
    private Timer mTimer = new Timer();

    private int mBannerPosition = 0;
    private final int FAKE_BANNER_SIZE = 100;
    private final int DEFAULT_BANNER_SIZE = 5;
    private boolean mIsUserTouched = false;

    private int[] mImagesSrc = {
            R.mipmap.img1,
            R.mipmap.img2,
            R.mipmap.img3,
            R.mipmap.img4,
            R.mipmap.img5
    };

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched) {
                mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_layout);
        initView();
        //mTimer.schedule(mTimerTask,5000,5000);
    }

    private void initView() {
        mIndicators = new ImageView[]{
                (ImageView) findViewById(R.id.indicator1),
                (ImageView) findViewById(R.id.indicator2),
                (ImageView) findViewById(R.id.indicator3),
                (ImageView) findViewById(R.id.indicator4),
                (ImageView) findViewById(R.id.indicator5)
        };
        mBanner = (ViewPager) findViewById(R.id.banner);
        mBannerAdapter = new BannerAdapter(this);
        mBanner.setAdapter(mBannerAdapter);
        mBanner.addOnPageChangeListener(mBannerAdapter);
        mBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {
        private LayoutInflater mInflater;

        public BannerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= DEFAULT_BANNER_SIZE;
            View view = mInflater.inflate(R.layout.item, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setImageResource(mImagesSrc[position]);
            final int pos = position;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(BannerActivity.this, "click banner item :" + pos, Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);

            int position = mBanner.getCurrentItem();
            LogUtils.e("hangce", "position = " + position);
            if (position == 0) {
                position = DEFAULT_BANNER_SIZE;
                mBanner.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = DEFAULT_BANNER_SIZE - 1;
                mBanner.setCurrentItem(position, false);
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mBannerPosition = position;

            setInxdicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void setInxdicator(int position) {
        position %= DEFAULT_BANNER_SIZE;
        for (ImageView indicator : mIndicators) {
            indicator.setImageResource(R.mipmap.indicator_unchecked);
        }
        mIndicators[position].setImageResource(R.mipmap.indicator_checked);
    }
}