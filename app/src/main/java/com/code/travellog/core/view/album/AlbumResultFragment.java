package com.code.travellog.core.view.album;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.vm.AlbumViewModel;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.ui.MakeAlbumActivity;
import com.mvvm.base.AbsLifecycleFragment;
import com.mvvm.http.HttpHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description:
 * @date: 2021/3/7
 */
public class AlbumResultFragment extends AbsLifecycleFragment<AlbumViewModel> {
    public static AlbumResultFragment newInstance(){
        return new AlbumResultFragment();
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_ablumresult;
    }
    private int workid ;
    @Override
    public void initView(Bundle state) {
        super.initView(state);
        startAlbum();
    }
    @SuppressLint("CheckResult")
    public void startAlbum()
    {
        workid = (((MakeAlbumActivity)getActivity())).getWorkid();
        HttpHelper.getInstance().create(ApiService.class).startAlbum(URL.ALBUM_URL+workid+"/start")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<BasePojo>() {
                    @Override
                    public void onSuccess(BasePojo basePojo) {
                        if (basePojo.code != 200) {
                            onFailure(basePojo.msg, basePojo.code);
                            return;
                        }
                    }
                    @Override
                    public void onFailure(String msg, int code) {
                        com.code.travellog.util.ToastUtils.showToast(msg);
                    }
                });
    }
}
