package com.code.travellog.core.data.source;

import com.code.travellog.config.URL;
import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.core.data.pojo.album.AlbumWorkPojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

import okhttp3.MultipartBody;

/**
 * @description: 影集数据仓库
 * @date: 2021/3/7
 */
public class AlbumRepository extends BaseRepository {
    public static String EVENT_KEY_ALBUMRESULT = null;
    public static String EVENT_KEY_ALBUMID = null;
    public static String EVENT_KEY_ALBUMPIC = null;
    public static String EVENT_KEY_ALBUMSTART = null;

    public AlbumRepository()
    {
        if (EVENT_KEY_ALBUMRESULT == null) EVENT_KEY_ALBUMRESULT = StringUtil.getEventKey();
        if (EVENT_KEY_ALBUMID == null) EVENT_KEY_ALBUMID = StringUtil.getEventKey();
        if (EVENT_KEY_ALBUMPIC == null) EVENT_KEY_ALBUMPIC = StringUtil.getEventKey();
        if (EVENT_KEY_ALBUMSTART == null) EVENT_KEY_ALBUMSTART = StringUtil.getEventKey();

    }

    public void getAlbumId(){
        addDisposable(apiService.getAlbumWorkid()
                    .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<AlbumWorkPojo>() {
                    @Override
                    public void onSuccess(AlbumWorkPojo albumWorkPojo) {
                        postData(EVENT_KEY_ALBUMID,albumWorkPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                })
        );
    }
    public void postPic(int workid, MultipartBody multipartBody){
        String url = URL.ALBUM_URL+workid+"/upload";
        addDisposable(apiService.postPic(url,multipartBody)
        .compose(RxSchedulers.io_main())
        .subscribeWith(new RxSubscriber<BasePojo>() {
            @Override
            public void onSuccess(BasePojo basePojo) {
                postData(EVENT_KEY_ALBUMPIC,basePojo);
                postState(StateConstants.SUCCESS_STATE);
            }

            @Override
            public void onFailure(String msg, int code) {
                postState(StateConstants.ERROR_STATE);
            }
        }));
    }

    public void getAlbumStart(int workid){
        String url = URL.ALBUM_URL+workid+ "/start";
        addDisposable(apiService.startAlbum(url)
        .compose(RxSchedulers.io_main())
        .subscribeWith(new RxSubscriber<BasePojo>() {
            @Override
            public void onSuccess(BasePojo basePojo) {
                postData(EVENT_KEY_ALBUMSTART,basePojo);
                postState(StateConstants.SUCCESS_STATE);
            }

            @Override
            public void onFailure(String msg, int code) {
                postState(StateConstants.ERROR_STATE);
            }
        }));
    }
    public void loadAlbumResult(int workid)
    {
        String url = URL.ALBUM_URL+workid+"/status";
        addDisposable(apiService.getAlbumResult(url)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<AlbumResultPojo>() {
                    @Override
                    protected void onNoNetWork() {
                        super.onNoNetWork();
                    }

                    @Override
                    public void onSuccess(AlbumResultPojo albumResultPojo) {
                        postData(EVENT_KEY_ALBUMRESULT,albumResultPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }

}
