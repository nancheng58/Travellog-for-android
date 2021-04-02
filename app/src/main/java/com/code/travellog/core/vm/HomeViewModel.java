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


    private void getBannerData(String posType,
                               String fCatalogId,
                               String sCatalogId,
                               String tCatalogId,
                               String province) {
        mRepository.loadBannerData(posType, fCatalogId, sCatalogId, tCatalogId, province);

    }

    public void getHomeListData() {
        getBannerData("1", "4", "109", "", null);
//        getHomeListData("0");
        getAlbumList();
        mRepository.loadHomeData();
    }

}
