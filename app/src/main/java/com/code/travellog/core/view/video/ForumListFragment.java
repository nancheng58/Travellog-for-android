package com.code.travellog.core.view.video;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.listener.OnItemClickListener;
import com.code.travellog.core.data.pojo.video.VideoListPojo;
import com.code.travellog.core.data.pojo.video.VideoPojo;
import com.code.travellog.core.data.repository.VideoRepository;
import com.code.travellog.core.viewmodel.VideoViewModel;
import com.code.travellog.core.view.base.BaseListFragment;
import com.code.travellog.config.Constants;

import com.code.travellog.core.view.AdapterPool;
import com.gyf.immersionbar.ImmersionBar;


/**
 * @description
 * @time 2021/3/26 21:10
 */

public class ForumListFragment extends BaseListFragment<VideoViewModel> implements OnItemClickListener {

    private String mVideoTagId;

    private boolean loadMore = false;

    public static ForumListFragment newInstance() {
        return new ForumListFragment();
    }

    @Override
    public void initView(Bundle state) {
        super.initView(state);

    }

    @Override
    protected void dataObserver() {
        if (getArguments() != null) {
            mVideoTagId = getArguments().getString(Constants.AlBUM_TAGID, null);
        }

        registerSubscriber(VideoRepository.EVENT_KEY_VIDEOTAG, mVideoTagId, VideoListPojo.class).observe(this, videoListPojo -> {
            if (videoListPojo != null && videoListPojo.data != null) {
//                lastId = videoListPojo.data.get(videoListPojo.data.size() - 1).id;
                setUiData(videoListPojo.data.movies);
            }

        });
    }


    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        return layoutManager;
    }

    @Override
    protected DelegateAdapter createAdapter() {
        DelegateAdapter adapter = AdapterPool.newInstance().getVideoRemAdapter(getActivity())
                .setOnItemClickListener(this).build();
        return adapter;
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getVideoByTag(mVideoTagId);
    }

    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        loadMore = isLoadMore;
        getRemoteData();
    }

    @Override
    public void onItemClick(View view, int i, Object object) {
        if (object != null) {
            if (object instanceof VideoPojo) {
                Intent intent = new Intent(activity, VideoDetailsActivity.class);
                intent.putExtra(Constants.AlBUM_ID, String.valueOf(((VideoPojo) object).work_id));
                activity.startActivity(intent);
            }

        }
    }
}
