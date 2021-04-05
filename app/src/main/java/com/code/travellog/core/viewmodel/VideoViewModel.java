package com.code.travellog.core.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.core.data.repository.VideoRepository;
import com.mvvm.base.AbsViewModel;


/**
 * @description
 * @time 2021/4/1 17:30
 */

public class VideoViewModel extends AbsViewModel<VideoRepository> {

    public VideoViewModel(@NonNull Application application) {
        super(application);
    }


    private void getVideoList() {
        mRepository.loadVideoData();
    }


    private void getBannerData() {
        mRepository.loadBannerData();

    }

    public void getVideoListData() {
        getBannerData();
        getVideoList();
        mRepository.loadData();
    }

}
