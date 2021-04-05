package com.code.travellog.core.data.repository;


import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.plog.PlogListPojo;
import com.code.travellog.core.data.pojo.plog.PlogMergePojo;
import com.code.travellog.core.data.pojo.plog.PlogResultPojo;
import com.code.travellog.core.data.pojo.plog.PlogStatusPojo;
import com.code.travellog.core.data.pojo.plog.PlogWorkPojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;

/**
 * @description Plog 数据仓库
 * @time 2021/4/2 16:26
 */

public class PlogRepository extends BaseRepository {


    public static String EVENT_KEY_PLOG_LIST = null;
    public static String EVENT_KEY_PLOGRESULT = null;
    public static String EVENT_KEY_PLOGID = null;
    public static String EVENT_KEY_PLOGPIC = null;
    public static String EVENT_KEY_PLOGSTART = null;
    public static String EVENT_KEY_PLOG_MORE = null;

    public static String EVENT_KEY_PLOG = null;

    private Flowable<PlogListPojo> mPlogData;

    private Flowable<BannerListVo> mBannerData;

//    private Flowable<PlogDetailVo> mPlogDetail;
//
//    private Flowable<PlogRecommentVo> mPlogRecommend;

    private PlogMergePojo plogMergePojo = new PlogMergePojo();


    public PlogRepository() {
        if (EVENT_KEY_PLOG_LIST==null) {
            EVENT_KEY_PLOG_LIST = StringUtil.getEventKey();
        }
        if (EVENT_KEY_PLOG_MORE==null) {
            EVENT_KEY_PLOG_MORE = StringUtil.getEventKey();
        }
        if(EVENT_KEY_PLOG == null){
            EVENT_KEY_PLOG = StringUtil.getEventKey();
        }
        if (EVENT_KEY_PLOGRESULT == null) EVENT_KEY_PLOGRESULT = StringUtil.getEventKey();
        if (EVENT_KEY_PLOGID == null) EVENT_KEY_PLOGID = StringUtil.getEventKey();
        if (EVENT_KEY_PLOGPIC == null) EVENT_KEY_PLOGPIC = StringUtil.getEventKey();
        if (EVENT_KEY_PLOGSTART == null) EVENT_KEY_PLOGSTART = StringUtil.getEventKey();

    }

    public void loadPlogListData()
    {
        mPlogData= apiService.getPlogList();
    }
    public void loadBannerData() {
        mBannerData = apiService.getBannerData();
    }


    public void getPlogId(){
        addDisposable(apiService.getPlogWorkid()
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<PlogWorkPojo>() {
                    @Override
                    public void onSuccess(PlogWorkPojo plogWorkPojo) {
                        postData(EVENT_KEY_PLOGID,plogWorkPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                })
        );
    }
    public void postPic(int workid,String index, MultipartBody multipartBody){
        String url = URL.PLOG_URL+workid+"/upload";
        addDisposable(apiService.postPic(url,multipartBody)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<BasePojo>() {
                    @Override
                    public void onSuccess(BasePojo basePojo) {
                        postData(EVENT_KEY_PLOGPIC,index,basePojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }

    public void getPlogStart(int workid){
        String url = URL.PLOG_URL+workid+ "/start";
        addDisposable(apiService.startPlog(url)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<BasePojo>() {
                    @Override
                    public void onSuccess(BasePojo basePojo) {
                        postData(EVENT_KEY_PLOGSTART,basePojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }
    public void loadPlogResult(int workid)
    {
        String url = URL.PLOG_URL+workid+"/status";
        addDisposable(apiService.getPlogStatus(url)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<PlogStatusPojo>() {
                    @Override
                    protected void onNoNetWork() {
                        super.onNoNetWork();
                    }

                    @Override
                    public void onSuccess(PlogStatusPojo plogStatusPojo) {
                        postData(EVENT_KEY_PLOGRESULT, plogStatusPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }

    public void loadPlogMergeData() {
        addDisposable(Flowable.concat(mBannerData, mPlogData)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<Object>() {

                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }

                    @Override
                    public void onSuccess(Object object) {
                        if (object instanceof BannerListVo) {
                            plogMergePojo.bannerListVo = (BannerListVo) object;
                        } else if (object instanceof PlogListPojo) {
                            plogMergePojo.plogListPojo = (PlogListPojo) object;
                            postData(EVENT_KEY_PLOG_LIST, plogMergePojo);
                            postState(StateConstants.SUCCESS_STATE);
                        }

                    }

                    @Override
                    public void onFailure(String msg,int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }

    public void loadPlogDetailData(String plogid) {
        String url = URL.PLOG_URL+plogid+"/status";
        addDisposable(apiService.getPlogResult(url)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<PlogResultPojo>() {

                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }
                    @Override
                    public void onSuccess(PlogResultPojo plogResultPojo) {
                        postData(EVENT_KEY_PLOG , plogResultPojo );
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {

                    }
                }));
    }
//
//    public void loadPlogRecommendData(String correctId) {
//        mPlogRecommend = apiService.getPlogRecommendData(correctId);
//    }

//    public void loadPlogMergeData() {
//        addDisposable(Flowable.concatArrayDelayError(mPlogDetail, mPlogRecommend)
//                .compose(RxSchedulers.<Object>io_main())
//                .subscribeWith(new RxSubscriber<Object>() {
//                    @Override
//                    protected void onNoNetWork() {
//                        postState(StateConstants.NET_WORK_STATE);
//                    }
//
//                    @Override
//                    public void onSuccess(Object object) {
//                        if (object instanceof PlogDetailVo) {
//                            plogMergeVo.plogDetailVo = (PlogDetailVo) object;
//                        } else if (object instanceof PlogRecommentVo) {
//                            plogMergeVo.plogRecommentVo = (PlogRecommentVo) object;
//                            postData(Constants.EVENT_KEY_PLOG, plogMergeVo);
//                            postState(StateConstants.SUCCESS_STATE);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String msg,int code) {
//                        postState(StateConstants.ERROR_STATE);
//
//                    }
//                }));
//    }
}
