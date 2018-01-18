package com.chenli.customerproject.news.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/1/15.
 */

public class NewsListFragment extends Fragment {

    private static NewsListFragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        String title = arguments.getString("title");
        String ids = arguments.getString("ids");
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText("title = " + title + " , ids = " + ids);
        return textView;
    }

    public static Fragment newInstance(String title,String ids){
        fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("ids",ids);
        fragment.setArguments(bundle);
        return fragment;
    }

}

