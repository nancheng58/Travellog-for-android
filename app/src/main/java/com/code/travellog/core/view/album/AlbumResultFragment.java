package com.code.travellog.core.view.album;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.core.data.source.AlbumRepository;
import com.code.travellog.core.vm.AlbumViewModel;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.ui.MakeAlbumActivity;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.util.AdapterPool;
import com.mvvm.base.AbsLifecycleFragment;
import com.mvvm.http.HttpHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description:
 * @date: 2021/3/7
 */
public class AlbumResultFragment extends BaseListFragment<AlbumViewModel> {

    public static AlbumResultFragment newInstance(){
        return new AlbumResultFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_ablumresult;
    }

    private int workid ;
    private int lastStaus;
    @Override
    public void initView(Bundle state) {
        super.initView(state);
        startAlbum();

    }

    @Override
    protected void dataObserver() {
        if(getArguments() != null) workid =getArguments().getInt("work_id");
        registerSubscriber(AlbumRepository.EVENT_KEY_ALBUMRESULT, AlbumResultPojo.class).observe(this, albumResultPojo -> {
            if(albumResultPojo == null ) return ;
            if (albumResultPojo.data.status != lastStaus){

            }
            lastStaus = albumResultPojo.data.status;
        });
    }

    @Override
    protected void onStateRefresh() {
        super.onStateRefresh();
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getAlbumResultAdapter(activity).build();
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false);
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getAlbumResult(workid);
    }

    @Override
    protected void showLoading() {
        super.showLoading();
    }

    @SuppressLint("CheckResult")
    public void startAlbum()
    {
//        workid = (((MakeAlbumActivity)getActivity())).getWorkid();
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
