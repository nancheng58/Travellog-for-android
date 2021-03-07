package com.mvvm.stateview;

import com.tqzhang.stateview.stateview.BaseStateControl;

import trecyclerview.com.mvvm.R;

/**
 * @description loading UI
 * @time 2021/3/7 10:40
 */

public class LoadingState extends BaseStateControl {
    @Override
    protected int onCreateView() {
        return R.layout.loading;
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

}
