package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.core.data.source.HomeRepository;
import com.code.travellog.core.data.source.VideoRepository;
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


    private void getBannerData(String posType,
                               String fCatalogId,
                               String sCatalogId,
                               String tCatalogId,
                               String province) {
        mRepository.loadBannerData(posType, fCatalogId, sCatalogId, tCatalogId, province);

    }

    public void getVideoListData() {
        getBannerData("1", "4", "109", "", null);
        getVideoList();
        mRepository.loadData();
    }

}
