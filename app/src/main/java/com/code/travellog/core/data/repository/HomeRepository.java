package com.code.travellog.core.data.repository;


import com.code.travellog.core.data.pojo.album.AlbumListPojo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.home.HomeMergePojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

import io.reactivex.Flowable;

/**
 * @description
 * @time 2021/3/5 15:58
 */

public class HomeRepository extends BaseRepository {

    public static String EVENT_KEY_HOME = null;



    private Flowable<BannerListVo> mBannerObservable;

    private Flowable<AlbumListPojo> mAlbumListObservable;

    private final HomeMergePojo homeMergePojo = new HomeMergePojo();


    public HomeRepository() {
        if (EVENT_KEY_HOME == null) {
            EVENT_KEY_HOME = StringUtil.getEventKey();
        }
    }


    public void loadAlbumData()
    {
        mAlbumListObservable =apiService.getAlbumList();
    }
    public void loadBannerData(String posType,
                               String fCatalogId,
                               String sCatalogId,
                               String tCatalogId,
                               String province) {
        mBannerObservable = apiService.getBannerData(posType, fCatalogId, sCatalogId, tCatalogId, province);
    }


    public void loadHomeData() {
        addDisposable(Flowable.concat(mBannerObservable, mAlbumListObservable)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }

                    @Override
                    public void onSuccess(Object object) {
                        if (object instanceof BannerListVo) {
                            homeMergePojo.bannerListVo = (BannerListVo) object;
                        } else if (object instanceof AlbumListPojo) {
                            homeMergePojo.albumListPojo = (AlbumListPojo) object;
                            if (homeMergePojo != null) {
                                postData(EVENT_KEY_HOME, homeMergePojo);
                                postState(StateConstants.SUCCESS_STATE);
                            } else {
                                postState(StateConstants.NOT_DATA_STATE);
                            }
                        }

                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }

}
