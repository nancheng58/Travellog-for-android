package com.code.travellog.core.view.video;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.listener.OnItemClickListener;
import com.code.travellog.core.data.pojo.video.VideoMergePojo;
import com.code.travellog.core.data.pojo.video.VideoPojo;
import com.code.travellog.core.viewmodel.VideoViewModel;
import com.code.travellog.core.view.base.BaseListFragment;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.common.TypeVo;
import com.code.travellog.core.data.repository.VideoRepository;
import com.code.travellog.core.view.AdapterPool;

import java.util.Random;

/**
 * @description 视频推荐
 * @time 2021/4/1 15:38
 */

public class VideoRecommendFragment extends BaseListFragment<VideoViewModel> implements OnItemClickListener {

    public static VideoRecommendFragment newInstance() {
        return new VideoRecommendFragment();
    }
    private String[]tags = new String[6];
    @Override
    public void initView(Bundle state) {
        super.initView(state);
        refreshHelper.setEnableLoadMore(false);
    }

    @Override
    protected void dataObserver() {
        registerSubscriber(VideoRepository.EVENT_KEY_VIDEO, VideoMergePojo.class)
                .observe(this, videoMergePojo -> {
                    if (videoMergePojo != null) {
                        VideoRecommendFragment.this.addItems(videoMergePojo);
                    }

                });
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mItems.get(position) instanceof TypeVo
                        || mItems.get(position) instanceof BannerListVo?
                        1 : 1;
            }
        });
        return layoutManager;
    }

    @Override
    protected DelegateAdapter createAdapter() {
        DelegateAdapter adapter = AdapterPool.newInstance().getVideoRemAdapter(activity)
                .setOnItemClickListener(this).build();
        return adapter;
    }

    @Override
    protected void getRemoteData() {

        tags= new String[]{"山水田园", "假日海滨", "历史建筑", "精致园林", "交友区", "驴友日常"};

        mViewModel.getVideoListData();
    }

    private void addItems(VideoMergePojo videoMergePojo) {
        if(isRefresh) mItems.clear();
        mItems.add(videoMergePojo.bannerListVo);
//        mItems.add(new TypeVo("影集列表"));
        if (videoMergePojo.videoListPojo.data ==null) return;
//        if (videoMergePojo.videoListPojo.data.post_num > 0) {
//            mItems.addAll(videoMergePojo.videoListPojo.data.new_posts);
//        }
        int num = videoMergePojo.videoListPojo.data.post_num;
        for (int i = 0; i < 5; i++) {
            mItems.add(new TypeVo(tags[i]));
            for(int j = 0 ; j<num/5 ; j++ ){
                Random random = new Random();
                mItems.add(videoMergePojo.videoListPojo.data.new_posts.get(random.nextInt(num-1)));
            }

        }
        setData();
    }


    @Override
    public void onItemClick(View view, int i, Object object) {
        if (object != null) {
            if (object instanceof VideoPojo) {
                Intent intent = new Intent(activity, VideoDetailsActivity.class);
                intent.putExtra(Constants.AlBUM_ID, String.valueOf(((VideoPojo) object).reference.work_id));
                activity.startActivity(intent);
            }

        }
    }
}
