package com.code.travellog.core.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.code.travellog.core.data.source.ApiRepository;
import com.mvvm.base.AbsViewModel;

import okhttp3.MultipartBody;

/**
 * @description: API VM
 * @date: 2021/3/15
 */
public class ApiViewModel extends AbsViewModel<ApiRepository> {
    public ApiViewModel(@NonNull Application application) {
        super(application);
    }
    public void getWeatherResult(MultipartBody multipartBody){
        mRepository.loadWeatherResult(multipartBody);
    }
    public void getPoery(String keyword ,int length ,int experience ,int history){
        mRepository.loadPoetry(keyword,length,experience,history);
    }
    public void getColorResult(MultipartBody multipartBody){
        mRepository.loadColorResult(multipartBody);
    }
}
