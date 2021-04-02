package com.code.travellog.core.view.base.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mvvm.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @time 2021/2/2 19:30
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;

    private List<String> mTitles;


    public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> lists, List<String> titles) {
        super(fm);
        fragments = new ArrayList<>();
        mTitles = new ArrayList<>();
        fragments.addAll(lists);
        mTitles.addAll(titles);
    }

    @NotNull
    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }
}
