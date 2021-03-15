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
import com.code.travellog.util.ToastUtils;
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
        dataObserver();
    }

    @Override
    protected void dataObserver() {
        if(getArguments() != null) workid =getArguments().getInt("work_id");
        registerSubscriber(AlbumRepository.EVENT_KEY_ALBUMSTART,BasePojo.class).observe(this,basePojo -> {
            if(basePojo.code != 200) ToastUtils.showToast(basePojo.msg);
            else {
                //TODO
            }
        });
        registerSubscriber(AlbumRepository.EVENT_KEY_ALBUMRESULT, AlbumResultPojo.class).observe(this, albumResultPojo -> {
            if(albumResultPojo == null ) return ;
            else if (albumResultPojo.code != 200){
                ToastUtils.showToast(albumResultPojo.msg);
            }
            else if (albumResultPojo.data.status != lastStaus){
                //TODO
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

    public void startAlbum()
    {
//        workid = (((MakeAlbumActivity)getActivity())).getWorkid();
        mViewModel.AlbumStart(workid);
    }
}
