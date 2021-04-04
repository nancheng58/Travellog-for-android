package com.code.travellog.core.view.plog;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.listener.OnItemClickListener;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.plog.PlogMergePojo;
import com.code.travellog.core.data.pojo.plog.PlogPojo;
import com.code.travellog.core.view.base.BaseListFragment;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.repository.PlogRepository;
import com.code.travellog.core.vm.PlogViewModel;
import com.code.travellog.core.view.AdapterPool;
import com.mvvm.event.LiveBus;


/**
 * @description
 * @time 2021/4/2 16:20
 */

public class PlogFragment extends BaseListFragment<PlogViewModel> implements OnItemClickListener {

    private String uTime;

    public static PlogFragment newInstance() {
        return new PlogFragment();
    }

    @Override
    public void initView(Bundle state) {
        super.initView(state);
        setTitle(getResources().getString(R.string.work_title_name));
//        refreshHelper.setEnableLoadMore(false);
//        refreshHelper.setEnableAutoLoadMore(t);
    }


    @Override
    protected void dataObserver() {
        registerSubscriber(PlogRepository.EVENT_KEY_PLOG_LIST, PlogMergePojo.class).observe(this, plogMergePojo -> {
            if (plogMergePojo != null) {
                mItems.clear();
                mItems.add(plogMergePojo.bannerListVo);
//                lastId = plogMergePojo.plogsListVo.data.content.get(plogMergePojo.plogsListVo.data.content.size() - 1).tid;
//                uTime = plogMergePojo.plogsListVo.data.content.get(plogMergePojo.plogsListVo.data.content.size() - 1).utime;
                mItems.addAll(plogMergePojo.plogListPojo.data.photos);
                setData();
            }
        });

//        registerSubscriber(PlogRepository.EVENT_KEY_WORK_MORE, PlogsListVo.class).observe(this, worksListVo -> {
//            if (worksListVo!=null && worksListVo.data.content != null) {
//                lastId = worksListVo.data.content.get(worksListVo.data.content.size() - 1).tid;
//                uTime = worksListVo.data.content.get(worksListVo.data.content.size() - 1).utime;
//                mItems.addAll(worksListVo.data.content);
//                setMoreData();
//            }
//
//        });

    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getWorkAdapter(getActivity())
                .setOnItemClickListener(this)
                .build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getWorkListData();
    }


    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
//        mViewModel.getWorkMoreData("", lastId, uTime);
    }

    @Override
    public void onItemClick(View view, int i, Object obj) {
        if (obj != null) {
            if (obj instanceof PlogPojo) {
                PlogPojo data = (PlogPojo) obj;
                Intent starter = new Intent(getActivity(), PlogDetailsActivity.class);
                starter.putExtra(Constants.PLOG_ID, String.valueOf(data.work_id));
                starter.putExtra(Constants.PLOG_POJO,data);
//                LiveBus.getDefault().postEvent(PlogRepository.EVENT_KEY_PLOG,String.valueOf(data.work_id),data);
                startActivity(starter);
            }

        }
    }
}
