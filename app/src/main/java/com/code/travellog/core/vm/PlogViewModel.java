package com.code.travellog.core.vm;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.code.travellog.core.data.repository.PlogRepository;
import com.mvvm.base.AbsViewModel;

import okhttp3.MultipartBody;


/**
 * @description Plog VM
 * @time 2021/4/2 23:36
 */

public class PlogViewModel extends AbsViewModel<PlogRepository> {

//    private MutableLiveData<PlogsListVo> mWorkMoreData;
//
//    private MutableLiveData<PlogDetailVo> mWorkDetailData;
//
//    private MutableLiveData<PlogRecommentVo> mWorkRecommentData;

//    private MutableLiveData<PlogMergeVo> mWorkMergeData;


    public PlogViewModel(@NonNull Application application) {
        super(application);
    }

    public void getPlogResult(int workid){
        mRepository.loadPlogResult(workid);
    }
    public void getPlogId() {
        mRepository.getPlogId();
    }
    public void postPic(int workid ,String index, MultipartBody multipartBody){
        mRepository.postPic(workid,index ,multipartBody);
    }
    public void PlogStart(int workid){
        mRepository.getPlogStart(workid);
    }
//    public LiveData<PlogsListVo> getWorkMoreData() {
//        if (mWorkMoreData == null) {
//            mWorkMoreData = new MutableLiveData<>();
//        }
//        return mWorkMoreData;
//    }
//
//    public LiveData<PlogDetailVo> getWorkDetailData() {
//        if (mWorkDetailData == null) {
//            mWorkDetailData = new MutableLiveData<>();
//        }
//        return mWorkDetailData;
//    }
//
//    public LiveData<PlogRecommentVo> getWorkRecommentData() {
//        if (mWorkRecommentData == null) {
//            mWorkRecommentData = new MutableLiveData<>();
//        }
//        return mWorkRecommentData;
//    }

//    public void getWorkMoreData(String corrected, String lastId, String utime) {
//        mRepository.loadWorkMoreData(corrected, lastId, utime, Constants.PAGE_RN);
//    }

    public void getPlogListData() {
        mRepository.loadPlogListData();

    }

    public void getBannerData() {
        mRepository.loadBannerData();
    }

    public void getPlogDetailData(String plogid)
    {
        mRepository.loadPlogDetailData(plogid);
    }
    public void getWorkListData() {
        getBannerData();
        getPlogListData();
        mRepository.loadPlogMergeData();
    }

//    private void getWorkDetailData(String correctId) {
//        mRepository.loadWorkDetailData(correctId);
//    }

//    public void getWorkRecommendData(String correctId) {
//        mRepository.loadWorkRecommendData(correctId);
//    }

//    public void getWorkDetaiMergeData(String correctId) {
//        getWorkDetailData(correctId);
//        getWorkRecommendData(correctId);
//        mRepository.loadWorkMergeData();
//
//    }

}
