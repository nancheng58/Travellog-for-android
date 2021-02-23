package com.code.travellog.core.view.followdraw;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.core.data.pojo.followdraw.FollowDrawRecommendVo;
import com.code.travellog.core.data.source.FollowDrawRepository;
import com.code.travellog.core.vm.FollowDrawViewModel;
import com.code.travellog.util.AdapterPool;

/**
 * @author：tqzhang  on 18/7/2 14:39
 */
public class FollowDrawRecommendFragment extends BaseListFragment<FollowDrawViewModel> {
    public static FollowDrawRecommendFragment newInstance() {
        return new FollowDrawRecommendFragment();
    }


    @Override
    protected void dataObserver() {
        registerSubscriber(FollowDrawRepository.EVENT_KEY_FD_RED, FollowDrawRecommendVo.class).observe(this, followDrawRecommendObject -> {
            if (followDrawRecommendObject == null) {
                return;
            }
            lastId = followDrawRecommendObject.data.get(followDrawRecommendObject.data.size() - 1).lessonid;
            setUiData(followDrawRecommendObject.data);
        });
    }


    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getFollowAdapter(activity).build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getFollowDrawRemList(lastId);
    }

    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        getRemoteData();
    }
}
