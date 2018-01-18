package com.chenli.customerproject;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.chenli.commenlib.bean.TabEntity;
import com.chenli.commenlib.util.mainutil.ConvertUtils;
import com.chenli.commenlib.util.mainutil.FragmentUtils;
import com.chenli.customerproject.more.MoreFragment;
import com.chenli.customerproject.news.view.NewsFragment;
import com.chenli.customerproject.photo.view.PhotoFragment;
import com.chenli.customerproject.video.view.VideoFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private ArrayList<CustomTabEntity> lists = new ArrayList<>(4);
    private String[] mTitles = {"新闻", "视频", "图片", "更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};

    @Bind(R.id.navigation_bar)
    CommonTabLayout navigation_bar;
    private ActionBar actionBar;

    private int lastPosition = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initNavigation();

    }

    private void initView() {
        setFragment(NewsFragment.newInstance(mTitles[0]));
    }

    private void initNavigation() {

        for (int i = 0; i < mTitles.length; i++) {
            TabEntity entity = new TabEntity(mTitles[i],mIconSelectIds[i],mIconUnselectIds[i]);
            lists.add(entity);
        }
        navigation_bar.setTabData(lists);
        navigation_bar.setTextsize(ConvertUtils.dp2px(12));
        navigation_bar.setTabPadding(ConvertUtils.dp2px(4));
        navigation_bar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (lastPosition != position){
                    if (position == 0){
                        setFragment(NewsFragment.newInstance(mTitles[0]));
                    }else if(position == 1){
                        setFragment(VideoFragment.newInstance(mTitles[1]));
                    }else if (position == 2){
                        setFragment(PhotoFragment.newInstance(mTitles[2]));
                    }else if (position == 3){
                        setFragment(MoreFragment.newInstance(mTitles[3]));
                    }
                    lastPosition = position;
                }
            }
            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentUtils.hideFragments(getSupportFragmentManager());
        if (fragment.isAdded()){
            FragmentUtils.showFragment(fragment);
        }else {
            FragmentUtils.addFragment(getSupportFragmentManager(),fragment,R.id.fragmlayout);
        }
    }
}
