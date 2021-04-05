package com.code.travellog.core.view.video;

import android.os.Bundle;

import com.code.travellog.R;
import com.code.travellog.core.viewmodel.VideoViewModel;
import com.code.travellog.core.view.base.BaseViewPagerFragment;

import com.mvvm.base.BaseFragment;

import java.util.List;

/**
 * @description
 * @time 2021/4/1 14:26
 */

public class VideoFragment extends BaseViewPagerFragment<VideoViewModel> {

//    private List<CourseTypeVo.DataBean> titleName = new ArrayList<>();

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }
    private String[]tags = new String[6];
    @Override
    public void initView(Bundle state) {
        super.initView(state);
        setTitle(getResources().getString(R.string.video_title_name));
        getTabData();
        setData();
        loadManager.showSuccess();
    }

    @Override
    protected void dataObserver() {
        // Tabs
//        registerSubscriber(VideoRepository.EVENT_KEY_VIDEOTAG, VideoMergePojo.class).observe(this, videoMergePojo -> {
//            if (videoMergePojo == null) {
//                return;
//            }
//            setData(videoMergePojo);
//        });

    }

    @Override
    protected void onStateRefresh() {
        super.onStateRefresh();
        getTabData();
    }

    @Override
    protected String[] createPageTitle() {
        return mArrTitles;
    }

    @Override
    protected List<BaseFragment> createFragments() {
        return mFragments;
    }

    private void getTabData() {
//        mViewModel.getCourseTypeData();
//        tags= new String[]{"大好河山", "美丽海景", "秀丽山川", "精致园林","交友区", "驴友日常"};
        tags= new String[]{"山水田园", "假日海滨", "历史建筑", "精致园林", "交友区", "驴友日常"};

    }


    private void setData() {
        mArrTitles = new String[tags.length + 1];
        mArrTitles[0] = getResources().getString(R.string.recommend_tab_name);

//        titleName.clear();
//        CourseTypeVo.DataBean dataBean = new CourseTypeVo.DataBean();
//        dataBean.name = getResources().getString(R.string.recommend_tab_name);
//        titleName.add(dataBean);
        for (int j = 0; j < 6; j++) {
//            titleName.add(courseTypeVo.data.get(j));
            mArrTitles[j + 1] = tags[j];
        }
        initFragment();
        setAdapter();
    }

    private void initFragment() {
        for (int i = 0; i < mArrTitles.length ; i++) {
            if (i == 0) {
                VideoRecommendFragment videoRecommendFragment = VideoRecommendFragment.newInstance();
                mFragments.add(videoRecommendFragment);
            }
            else {
                ForumListFragment forumListFragment = ForumListFragment.newInstance();
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.F_CATALOG_ID, titleName.get(i).id);
//                bundle.putSerializable(Constants.S_CATALOG, titleName.get(i).s_catalog);
//                forumListFragment.setArguments(bundle);
                mFragments.add(forumListFragment);
            }

        }
    }
}
