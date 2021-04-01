package com.code.travellog.core.data.source;

import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.core.data.pojo.album.AlbumListPojo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.course.CourseDetailRemVideoVo;
import com.code.travellog.core.data.pojo.course.CourseDetailVo;
import com.code.travellog.core.data.pojo.course.CourseListVo;
import com.code.travellog.core.data.pojo.course.CourseRemVo;
import com.code.travellog.core.data.pojo.course.CourseTypeVo;

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

    public static String EVENT_KEY_VIDEO = null;
    public static String EVENT_KEY_VIDEOTAG = null;

    private Flowable<VideoListPojo> mVideoListObservable;

    private Flowable<BannerListVo> mBannerObservable;


    private final VideoMergePojo videoMergePojo = new VideoMergePojo();


    public VideoRepository() {
        if (EVENT_KEY_VIDEO == null) {
            EVENT_KEY_VIDEO = StringUtil.getEventKey();
        }
        if (EVENT_KEY_VIDEOTAG == null) EVENT_KEY_VIDEOTAG = StringUtil.getEventKey();
    }

//    public void loadVideoData(String id) {
//        mVideoListObservable = apiService.getVideoData(id);
//    }

    public void loadVideoData()
    {
        mVideoListObservable =apiService.getVideoList();
    }
    public void loadBannerData(String posType,
                               String fCatalogId,
                               String sCatalogId,
                               String tCatalogId,
                               String province) {
        mBannerObservable = apiService.getBannerData(posType, fCatalogId, sCatalogId, tCatalogId, province);
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
                                postData(EVENT_KEY_VIDEO, videoMergePojo);
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
