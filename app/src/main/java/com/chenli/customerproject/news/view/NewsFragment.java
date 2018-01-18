package com.chenli.customerproject.news.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenli.commenlib.util.mainutil.LogUtils;
import com.chenli.customerproject.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/13.
 */

public class NewsFragment extends Fragment {
    private static NewsFragment fragment;
    private View view;
    private List<Fragment> lists = new ArrayList<>();

    @Bind(R.id.toolbar)
    Toolbar toolbar ;

    @Bind(R.id.tablayout)
    TabLayout tablayout;

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private String[] stringArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_layout, container, false);
        ButterKnife.bind(this,view);

        initToolbar();
        initTab();
        return view;
    }

    private void initTab() {
        stringArray = getResources().getStringArray(R.array.news_channel);
        String[] stringArrayIds = getResources().getStringArray(R.array.news_channel_id);

        lists.clear();
        for (int i = 0; i < stringArray.length; i++) {
            lists.add(NewsListFragment.newInstance(this.stringArray[i],stringArrayIds[i]));
        }

        LogUtils.e("hangce",lists.size() + " , stringArray = " + stringArray.length);
        MyFragmentAdapter adapter = new MyFragmentAdapter(getChildFragmentManager(),lists);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
    }

    private void initToolbar() {
        Bundle bundle = new Bundle();
        String title = (String) bundle.get("title");
        LogUtils.e("hangce",title);
        toolbar.setTitle(title);
    }

    public static Fragment newInstance(String title){
        if (fragment == null){
            fragment = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title",title);
            fragment.setArguments(bundle);
            return fragment;
        }else {
            return fragment;
        }
    }

    class MyFragmentAdapter extends FragmentPagerAdapter{
        List<Fragment> fragmentLists ;
        public MyFragmentAdapter(FragmentManager fm, List<Fragment> listFragments) {
            super(fm);
            fragmentLists = listFragments;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentLists.get(position);
        }
        @Override
        public int getCount() {
            return fragmentLists.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArray[position];
        }
    }

}
