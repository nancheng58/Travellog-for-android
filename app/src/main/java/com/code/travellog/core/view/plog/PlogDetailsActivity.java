package com.code.travellog.core.view.plog;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.adapter.ItemData;
import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.plog.PlogPojo;
import com.code.travellog.core.data.pojo.plog.PlogResultPojo;
import com.code.travellog.core.view.plog.holder.PlogPicHolder;
import com.code.travellog.core.viewmodel.PlogViewModel;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mvvm.base.AbsLifecycleActivity;
import com.mvvm.event.LiveBus;
import com.mvvm.http.HttpHelper;
import com.tencent.mmkv.MMKV;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @description
 * @time 2021/4/3 22:05
 */

public class PlogDetailsActivity extends AbsLifecycleActivity<PlogViewModel> {

    protected RecyclerView mRecyclerView;
    private DelegateAdapter adapter;
    protected ItemData items = new ItemData();
    private String plogId;
    private PlogPojo plogPojo = null;
    private WeakReference<PlogDetailsActivity> weakReference;
    private TextView title;
    private ImageView ivback;
    protected RelativeLayout mTitleBar;
    @Override
    protected void onStateRefresh() {
        super.onStateRefresh();
        mViewModel.getWorkListData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_plog_details;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);

        ImmersionBar.with(this).statusBarDarkFont(true).init();
        weakReference = new WeakReference<>(this);
        if (getIntent() != null) {
            plogId = getIntent().getStringExtra(Constants.PLOG_ID);
            assert plogId != null;
            if((PlogPojo) getIntent().getSerializableExtra(Constants.PLOG_POJO) !=null){
                plogPojo = (PlogPojo) getIntent().getSerializableExtra(Constants.PLOG_POJO);
            }
        }

        initAdapter();
        initRecyclerView();
        getData();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        title = findViewById(R.id.tv_title);
        mTitleBar = findViewById(R.id.rl_title_bar);
        title.setText("Plog 预览");
        mTitleBar.setVisibility(View.VISIBLE);
        ivback = findViewById(R.id.iv_back);
        ivback.setVisibility(View.VISIBLE);
    }

    private void initAdapter() {
        adapter =  new DelegateAdapter.Builder<>()
                .bind(PlogPojo.class, new PlogPicHolder(this))
//                .bind(PlogInfoVo.class, new PlogRemItemHolder(weakReference.get()))
//                .bind(TypeVo.class, new TypeItemView(this))
//                .setOnItemClickListener(this)
                .build();

    }

    @Override
    protected void dataObserver() {
        LiveBus.getDefault().subscribe(Constants.EVENT_KEY_PLOG_STATE).observe(this, observer);

//        registerSubscriber(PlogRepository.EVENT_KEY_PLOG,plogId, PlogPojo.class).observe(this,plogPojo ->  {
//            Log.w("111",plogPojo.toString());
//            if (plogPojo != null) {
//                items.add(plogPojo);
//            }
//
//            adapter.setDatas(items);
//            mRecyclerView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        });

    }


//    private void getNetWorkData() {
//        mViewModel.getPlogListData();
//    }
    @SuppressLint("CheckResult")
    private void getData() {
        if (TextUtils.isEmpty(plogId)) {
            ToastUtils.showToast("加载出错");
            return;
        }
        if (plogPojo != null) {
            Log.w("plogDetail","plog Detail get");

            items.add(plogPojo);
            adapter.setDatas(items);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            loadManager.showSuccess();
        }
        else{
            getRemoteData();
        }

    }
    @SuppressLint("CheckResult")
    void getRemoteData()
    {
        String url = URL.PLOG_URL + plogId + "/status";
        HttpHelper.getInstance().create(ApiService.class).getPlogResult(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<PlogResultPojo>() {

                    @Override
                    public void onSuccess(PlogResultPojo plogResultPojo) {
                        if (plogResultPojo == null)
                            ToastUtils.showToast(plogResultPojo.msg);
                        else if (plogResultPojo.data.status != 200)
                            ToastUtils.showToast("该Plog长图" + plogPojo.status_msg);
                        else {
                            Log.w("plogDetail","plog Detail get");
                            MMKV mmkv = MMKV.defaultMMKV();
                            plogResultPojo.data.avatar = mmkv.decodeString("avatar");
                            plogResultPojo.data.uname = mmkv.decodeString("userName");
                            items.add(plogResultPojo.data);
                            adapter.setDatas(items);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            loadManager.showSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String msg, int code) {

                    }


                });

    }
//        @Override
//    public void onItemClick(View view, int i, Object o) {
//        if (o != null) {
//            if (o instanceof PlogPojo) {
//                PlogPojo data = (PlogPojo) o;
//                Intent starter = new Intent(this, PlogDetailsActivity.class);
//                starter.putExtra("plog_id", data.work_id);
//                startActivity(starter);
//                finish();
//            }
//
//        }
//    }
}
