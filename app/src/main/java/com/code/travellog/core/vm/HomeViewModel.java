package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.core.data.repository.HomeRepository;
import com.mvvm.base.AbsViewModel;


/**
 * @description
 * @time 2021/3/6 17:30
 */

public class HomeViewModel extends AbsViewModel<HomeRepository> {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    private void getAlbumList() {
        mRepository.loadAlbumData();
    }


    private void getBannerData() {
        mRepository.loadBannerData();

    }

    public void getHomeListData() {
        getBannerData();
//        getHomeListData("0");
        getAlbumList();
        mRepository.loadHomeData();
    }

}
