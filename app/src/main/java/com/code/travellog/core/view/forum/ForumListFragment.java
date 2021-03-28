package com.code.travellog.core.view.forum;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.listener.OnItemClickListener;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.course.CourseInfoVo;
import com.code.travellog.core.data.pojo.course.CourseListVo;
import com.code.travellog.core.data.source.CourseRepository;
import com.code.travellog.core.vm.CourseViewModel;
import com.code.travellog.util.AdapterPool;


/**
 * @description
 * @time 2021/3/26 21:10
 */

public class ForumListFragment extends BaseListFragment<CourseViewModel> implements OnItemClickListener {

    private String mCatalogId;

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
            mCatalogId = getArguments().getString(Constants.F_CATALOG_ID, null);
        }

        registerSubscriber(CourseRepository.EVENT_KEY_COURSE_LIDT, mCatalogId, CourseListVo.class).observe(this, courseListVo -> {
            if (courseListVo != null && courseListVo.data != null) {
                lastId = courseListVo.data.get(courseListVo.data.size() - 1).courseid;
                setUiData(courseListVo.data);
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
        DelegateAdapter adapter = AdapterPool.newInstance().getCourseListAdapter(getActivity())
                .setOnItemClickListener(this).build();
        return adapter;
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getCourseList(mCatalogId, lastId);
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
            if (object instanceof CourseInfoVo) {
                Intent intent = new Intent(activity, VideoDetailsActivity.class);
                intent.putExtra(Constants.COURSE_ID, ((CourseInfoVo) object).courseid);
                activity.startActivity(intent);
            }

        }
    }
}