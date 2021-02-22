package com.code.travellog.core.view.live;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.base.BaseListFragment;
import com.code.travellog.core.data.pojo.live.LiveListVo;
import com.code.travellog.core.data.source.LiveRepository;
import com.code.travellog.core.vm.LiveViewModel;
import com.code.travellog.util.AdapterPool;

/**
 * @author：tqzhang on 18/6/30 18:36
 */
public class LiveRecommendFragment extends BaseListFragment<LiveViewModel> {

    public static LiveRecommendFragment newInstance() {
        return new LiveRecommendFragment();
    }

    @Override
    public void initView(Bundle state) {
        super.initView(state);
    }


    @Override
    protected void dataObserver() {
        registerSubscriber(LiveRepository.EVENT_KEY_LIVE_RED, LiveListVo.class)
                .observe(this, liveListVo -> {
                    if (liveListVo != null) {
                        lastId = liveListVo.
                                data.get(liveListVo.data.size() - 1).liveid;
                        setUiData(liveListVo.data);
                    }
                });
    }


    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getLiveRemAdapter(activity).build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getLiveRemList(lastId);
    }

    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        getRemoteData();
    }
}
