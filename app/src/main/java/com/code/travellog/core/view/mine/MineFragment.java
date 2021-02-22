package com.code.travellog.core.view.mine;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.adapter.listener.OnItemClickListener;
import com.code.travellog.R;
import com.code.travellog.adapter.BaseRecyclerAdapter;
import com.code.travellog.base.BaseListFragment;
import com.code.travellog.core.data.source.MineRepository;
import com.code.travellog.core.vm.MineViewModel;
import com.mvvm.stateview.LoadingState;

/**
 * @description "我的"页面
 * @time 2021/2/22 18:18
 */

public class MineFragment extends BaseListFragment<MineViewModel> implements OnItemClickListener {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    public int getContentResId(){
        return R.id.content_layout;
    }
    @Override
    public void initView(Bundle state) {
        super.initView(state);
        setTitle(R.string.mine_title_name);
        refreshHelper.setEnableLoadMore(false);
    }

    @Override
    protected void dataObserver(){
        registerSubscriber(MineRepository.EVENT_KEY_MINE_LIST,)
    }

    @Override
    protected void onStateRefresh() {

    }



}
