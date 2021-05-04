package com.code.travellog.core.view.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.View;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.album.AlbumPojo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.banner.BannerVo;
import com.code.travellog.core.data.pojo.common.TypeVo;
import com.code.travellog.core.data.pojo.home.ButtonPojo;
import com.code.travellog.core.data.pojo.plog.PlogPojo;
import com.code.travellog.core.view.base.BaseListFragment;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.home.CategoryVo;
import com.code.travellog.core.data.pojo.home.HomeMergePojo;
import com.code.travellog.core.data.repository.HomeRepository;
import com.code.travellog.core.view.base.widget.banner.BannerItemView;
import com.code.travellog.core.view.plog.PlogDetailsActivity;
import com.code.travellog.core.view.video.VideoDetailsActivity;
import com.code.travellog.core.viewmodel.HomeViewModel;
import com.code.travellog.core.view.AdapterPool;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @description
 * @time 2021/3/1 18:55
 */

public class HomeFragment extends BaseListFragment<HomeViewModel> implements OnItemClickListener {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    public Banner banner;
    @Override
    public void initView(final Bundle state) {
        super.initView(state);
        setTitle(getResources().getString(R.string.home_title_name));
        refreshHelper.setEnableLoadMore(false);
        loadManager.showSuccess();
        banner = new Banner(activity);
//        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
//                .setAdapter(new BannerItemView(DataBean.getTestData()))
//                .setIndicator(new CircleIndicator(this));
    }

    /**
     * @description 注册数据观察者，将Repository获取的数据homeMergeVo返回到View
     * @param
     * @return
     * @time 2021/2/22 11:10
     */
    @Override
    protected void dataObserver() {
        registerSubscriber(HomeRepository.EVENT_KEY_HOME, HomeMergePojo.class)
                .observe(this, homeMergePojo -> {
                    if (homeMergePojo != null) {
                        HomeFragment.this.addItems(homeMergePojo);
                    }
                });

    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        return layoutManager;
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getHomeAdapter(getActivity())
                .setOnItemClickListener(this)
                .build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getHomeListData();
    }

    private void addItems(HomeMergePojo homeMergePojo) {
        if (isRefresh) {
            mItems.clear();
        }

        mItems.add(homeMergePojo.bannerListVo);
        mItems.add(new CategoryVo("title"));
        mItems.add(new ButtonPojo(1));
        mItems.add(new ButtonPojo(2));
        mItems.add(new ButtonPojo(3));

        if (homeMergePojo.albumListPojo.data ==null) return;
        if (homeMergePojo.albumListPojo.data.movie_num > 0) {
            mItems.add(new TypeVo("  "+getResources().getString(R.string.home_album_list)));
            mItems.addAll(homeMergePojo.albumListPojo.data.movies);
        }
        if (homeMergePojo.plogListPojo.data.photo_num > 0) {
            mItems.add(new TypeVo("  "+getResources().getString(R.string.home_plog_list)));
            mItems.addAll(homeMergePojo.plogListPojo.data.photos);
        }
        setData();
    }

    @Override
    public void onItemClick(View view, int i, Object object) {
        if (object != null) {
            if (object instanceof AlbumPojo) {
                Intent intent = new Intent(activity, VideoDetailsActivity.class);
                intent.putExtra(Constants.AlBUM_ID, String.valueOf(((AlbumPojo) object).work_id));
                activity.startActivity(intent);
            }
            if (object instanceof PlogPojo){
                Intent intent = new Intent(activity, PlogDetailsActivity.class);
                intent.putExtra(Constants.PLOG_ID, String.valueOf(((PlogPojo) object).work_id));
                activity.startActivity(intent);
            }
        }
    }
}
