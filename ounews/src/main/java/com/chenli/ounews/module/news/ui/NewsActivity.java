package com.chenli.ounews.module.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chenli.ounews.R;
import com.chenli.ounews.base.BaseActivity;
import com.chenli.ounews.module.photo.ui.PhotoActivity;
import com.chenli.ounews.module.settings.SettingsActivity;
import com.chenli.ounews.module.video.ui.VideoActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/11.
 */

public class NewsActivity extends BaseActivity{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawerlayout)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigation_view;
    private Class mClass;
    private ActionBarDrawerToggle drawerToggle;

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Bind(R.id.tabs)
    TabLayout tabs;

    private int lastPosition = 0 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        initToolbar();
        initNavigation();
        initView();
    }

    private void initView() {

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("新闻");
    }

    private void initNavigation() {
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open_toggle,R.string.close_toggle);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == lastPosition){
                    return true;
                }
                switch (item.getItemId()){
                    case R.id.action_news:
                        mClass = NewsActivity.class;
                        break;
                    case R.id.action_video:
                        mClass = VideoActivity.class;
                        break;
                    case R.id.action_photo:
                        mClass = PhotoActivity.class;
                        break;
                    case R.id.action_settings:
                        mClass = SettingsActivity.class;
                        break;
                }
                toolbar.setTitle(item.getTitle());
                drawerLayout.closeDrawers();
                lastPosition = item.getItemId();
                return true;
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                if (mClass != null){
                    startNextActivity(mClass);
                    mClass = null;
                }
            }
        });
    }

    private void startNextActivity(Class mClass) {
        Intent intent = new Intent(this,mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }else if (item.getItemId() == R.id.action_channel_manage){
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
