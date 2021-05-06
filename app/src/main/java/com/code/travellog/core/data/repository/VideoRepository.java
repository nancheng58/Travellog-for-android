package com.code.travellog.core.data.repository;

import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.banner.BannerListVo;

import com.code.travellog.core.data.pojo.video.VideoListPojo;
import com.code.travellog.core.data.pojo.video.VideoMergePojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

import io.reactivex.Flowable;


/**
 * @description
 * @time 2021/4/1 16:58
 */

public class VideoRepository extends BaseRepository {

    public static String EVENT_KEY_VIDEOLIST = null;
    public static String EVENT_KEY_VIDEOTAG = null;

    private Flowable<VideoListPojo> mVideoListObservable;

    private Flowable<BannerListVo> mBannerObservable;


    private final VideoMergePojo videoMergePojo = new VideoMergePojo();


    public VideoRepository() {
        if (EVENT_KEY_VIDEOLIST == null) EVENT_KEY_VIDEOLIST = StringUtil.getEventKey();
        if (EVENT_KEY_VIDEOTAG == null) EVENT_KEY_VIDEOTAG = StringUtil.getEventKey();
    }

    public void loadVideoDataById(String id) {
        String url = URL.ALBUM_URL+"tag/"+id;
        addDisposable(apiService.getVideoListById(url)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<VideoListPojo>(){
                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }
                    @Override
                    public void onSuccess(VideoListPojo videoListPojo) {
                        postData(EVENT_KEY_VIDEOTAG,id,videoListPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }

    public void loadVideoData()
    {
        mVideoListObservable =apiService.getVideoList();
    }
    public void loadBannerData() {
        mBannerObservable = apiService.getBannerData();
    }


    public void loadData() {
        addDisposable(Flowable.concat(mBannerObservable, mVideoListObservable)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<Object>() {
                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }
                    @Override
                    public void onSuccess(Object object) {
                        if (object instanceof BannerListVo) {
                            videoMergePojo.bannerListVo = (BannerListVo) object;
                        } else if (object instanceof VideoListPojo) {
                            videoMergePojo.videoListPojo = (VideoListPojo) object;
                            if (videoMergePojo != null) {
                                postData(EVENT_KEY_VIDEOLIST, videoMergePojo);
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
