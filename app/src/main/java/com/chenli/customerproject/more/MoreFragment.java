package com.chenli.customerproject.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chenli.customerproject.photo.view.PhotoFragment;

/**
 * Created by Administrator on 2018/1/13.
 */

public class MoreFragment extends Fragment{

    private static MoreFragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(container.getContext());
        textView.setGravity(Gravity.CENTER);
        Bundle arguments = getArguments();
        textView.setText(arguments.getString("title"));
        return textView;
    }


    public static Fragment newInstance(String title){
        if (fragment == null){
            fragment = new MoreFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title",title);
            fragment.setArguments(bundle);
            return fragment;
        }else {
            return fragment;
        }
    }

}
