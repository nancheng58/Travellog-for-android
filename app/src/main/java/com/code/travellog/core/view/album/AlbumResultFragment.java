package com.code.travellog.core.view.album;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.album.AlbumResultDescriptionPojo;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
    ImageView ivBack;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_list_common;
    }
    private List<String> list;
    private int workid ;
    private int lastStaus = 0;
    private String lastDes = null ;
    private AlbumResultDescriptionPojo albumResultDescriptionPojo = null;
    @Override
    public void initView(Bundle state) {
        super.initView(state);
        startAlbum();
        dataObserver();
        ivBack = getActivity().findViewById(R.id.iv_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(v -> {
            onDestroy();
        });
//        mSmartRefreshLayout.s
        isLoadMore = false;
        isRefresh = true;
        albumResultDescriptionPojo = new AlbumResultDescriptionPojo();
        albumResultDescriptionPojo.descriptions = new ArrayList<>();
        setTitle("影集生成");
        mSmartRefreshLayout.setEnableLoadMore(false);
        onLoadMore(false,0);
    }

    @Override
    protected void dataObserver() {
//        if(getArguments() != null) workid =getArguments().getInt("work_id");
        registerSubscriber(AlbumRepository.EVENT_KEY_ALBUMSTART,BasePojo.class).observe(this,basePojo -> {
            if(basePojo.code != 200) ToastUtils.showToast(basePojo.msg);
            else {
                getRemoteData();
            }
        });
        registerSubscriber(AlbumRepository.EVENT_KEY_ALBUMRESULT, AlbumResultPojo.class).observe(this, albumResultPojo -> {
            if(albumResultPojo == null ) return ;
            else if (albumResultPojo.code != 200){
                ToastUtils.showToast(albumResultPojo.msg);
            }
//            else if(albumResultPojo.data.status != 3 ){
//                Log.w("AlbumResultInfo",albumResultPojo.data.info);
//
//                getRemoteData();
//            }
//            else if (albumResultPojo.data.status != lastStaus)
            {
                //TODO
                Log.w("AlbumResultInfo",albumResultPojo.data.description);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getRemoteData();
//                    }
//                }
//                , 1000);
                AlbumResultFragment.this.addItems(albumResultPojo);
            }

        });
    }

    @Override
    protected void onStateRefresh() {
        super.onStateRefresh();
        getRemoteData();
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getAlbumResultAdapter(activity).build();
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(activity,RecyclerView.VERTICAL,false);
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
        workid=((MakeAlbumActivity)activity).getWorkid();

        mViewModel.AlbumStart(workid);
    }
    private void addItems(AlbumResultPojo albumResultPojo){

        if(!lastDes.equals(albumResultPojo.data.description))
            albumResultDescriptionPojo.descriptions.add(albumResultPojo.data.description);
        if(isRefresh) mItems.clear();
        mItems.add(albumResultPojo);
        mItems.add(albumResultDescriptionPojo);
        setData();
        lastStaus = albumResultPojo.data.status;
        lastDes = albumResultPojo.data.description;
    }
}
