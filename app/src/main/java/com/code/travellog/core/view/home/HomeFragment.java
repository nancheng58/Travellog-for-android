package com.code.travellog.core.view.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.listener.OnItemClickListener;
import com.code.travellog.R;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.course.CourseInfoVo;
import com.code.travellog.core.data.pojo.home.CategoryVo;
import com.code.travellog.core.data.pojo.home.HomeMergeVo;
import com.code.travellog.core.data.source.HomeRepository;
import com.code.travellog.core.view.forum.VideoDetailsActivity;
import com.code.travellog.core.vm.HomeViewModel;
import com.code.travellog.util.AdapterPool;


/**
 * @author：tqzhang on 18/5/2 18:46
 */
public class HomeFragment extends BaseListFragment<HomeViewModel> implements OnItemClickListener {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void initView(final Bundle state) {
        super.initView(state);
        setTitle(getResources().getString(R.string.home_title_name));
        refreshHelper.setEnableLoadMore(true);
        loadManager.showSuccess();
    }

    /**
     * @description 注册数据观察者，将Repository获取的数据homeMergeVo返回到View
     * @param
     * @return
     * @time 2021/2/22 11:10
     */
    @Override
    protected void dataObserver() {
        registerSubscriber(HomeRepository.EVENT_KEY_HOME, HomeMergeVo.class)
                .observe(this, homeMergeVo -> {
                    if (homeMergeVo != null) {
                        HomeFragment.this.addItems(homeMergeVo);
                    }
                });

    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getHomeAdapter(getActivity())
                .setOnItemClickListener(this)
                .build();
    }

    @Override
    protected void getRemoteData() {
//        mViewModel.getHomeListData();
    }

    private void addItems(HomeMergeVo homeMergeVo) {
        if (isRefresh) {
            mItems.clear();
        }
        mItems.add(homeMergeVo.bannerListVo);
        mItems.add(new CategoryVo("title"));
//        mItems.add(new TypeVo(getResources().getString(R.string.recommend_live_type)));
//        if (homeMergeVo.homeListVo.data.live_recommend.size() > 0) {
//            mItems.addAll(homeMergeVo.homeListVo.data.live_recommend);
//        }
//        mItems.add(new TypeVo(getResources().getString(R.string.recommend_video_type)));
//        if (homeMergeVo.homeListVo.data.course.size() > 0) {
//            mItems.addAll(homeMergeVo.homeListVo.data.course);
//        }
//        mItems.add(new TypeVo(getResources().getString(R.string.recommend_book_type)));
//        if (homeMergeVo.homeListVo.data.publishingbook.size() > 0) {
//            mItems.add(new BookList(homeMergeVo.homeListVo.data.publishingbook));
//        }
//        mItems.add(new TypeVo(getResources().getString(R.string.special_tab_name)));
//        if (homeMergeVo.homeListVo.data.matreialsubject.size() > 0) {
//            mItems.addAll(homeMergeVo.homeListVo.data.matreialsubject);
//        }
        setData();
    }

    @Override
    public void onItemClick(View view, int i, Object object) {
        if (object != null) {
            if (object instanceof CourseInfoVo) {
                Intent intent = new Intent(activity, VideoDetailsActivity.class);
                intent.putExtra(Constants.COURSE_ID, ((CourseInfoVo) object).courseid);
                activity.startActivity(intent);
            }
        }
    }
}
