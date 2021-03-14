package com.code.travellog.core.data.source;

import com.code.travellog.config.URL;
import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

/**
 * @description: 影集数据仓库
 * @date: 2021/3/7
 */
public class AlbumRepository extends BaseRepository {
    public static String EVENT_KEY_ALBUMRESULT = null;
    public AlbumRepository()
    {
        if (EVENT_KEY_ALBUMRESULT == null) EVENT_KEY_ALBUMRESULT = StringUtil.getEventKey();
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
                        if(albumResultPojo.code != 200) onFailure(albumResultPojo.msg,albumResultPojo.code);
                        postData(EVENT_KEY_ALBUMRESULT,albumResultPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        ToastUtils.showToast(msg);
                    }
                }));
    }
}
