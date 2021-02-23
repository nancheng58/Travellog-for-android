package com.code.travellog.core.view.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.core.data.pojo.activity.ActivityListVo;
import com.code.travellog.core.data.source.ActivityRepository;
import com.code.travellog.core.vm.ActivityViewModel;
import com.code.travellog.util.AdapterPool;

/**
 * @author：tqzhang on 18/7/4 14:10
 */
public class ActivityListFragment extends BaseListFragment<ActivityViewModel> {

    public static ActivityListFragment newInstance() {
        return new ActivityListFragment();
    }

    @Override
    protected void dataObserver() {
        registerSubscriber(ActivityRepository.EVENT_KEY_ACTIVITY, ActivityListVo.class).observe(this, activityListVo -> {
            if (activityListVo != null) {
                lastId = activityListVo.data.get(activityListVo.data.size() - 1).newsid;
                setUiData(activityListVo.data);
            }
        });
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getActivityAdapter(activity).build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getActivityList(lastId);
    }


    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        getRemoteData();
    }
}
