package com.code.travellog.core.view.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.code.travellog.R;
import com.code.travellog.ui.AboutActivity;
import com.code.travellog.ui.UserInfoActivity;
import com.code.travellog.util.ToastUtils;
import com.leon.lib.settingview.LSettingItem;
import com.mvvm.base.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @description "我的"页面
 * @time 2021/2/22 18:18
 */

public class MineFragment extends BaseFragment  {

    protected RelativeLayout mTitleBar;
    protected TextView mTitle;
    protected LSettingItem mabout;
    protected CircleImageView imageView;
    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    public int getContentResId(){
        return R.id.content_layout;
    }
    @Override
    public void initView(Bundle state) {
        loadManager.showSuccess();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mTitleBar = getViewById(R.id.rl_title_bar);
        mTitle = getViewById(R.id.tv_title);
        mabout = getActivity().findViewById(R.id.tv_about);
        imageView = getActivity().findViewById(R.id.avater);
        imageView.setOnClickListener(v -> {
//            ToastUtils.showToast("点击成功");
            Intent intent = new Intent(activity, UserInfoActivity.class);
            startActivity(intent);
        });
        mabout.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(activity, AboutActivity.class);
                startActivity(intent);
            }
        });
        setTitle(getResources().getString(R.string.mine_title_name));
    }
//    @Override
//    protected void dataObserver(){
////        registerSubscriber(MineRepository.EVENT_KEY_MINE_LIST,)
//    }

//    @Override
//    protected RecyclerView.LayoutManager createLayoutManager() {
//        return null;
//    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_about:
                ToastUtils.showToast("点击成功");
                Intent intent = new Intent(activity, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.avater:
                ToastUtils.showToast("点击成功");

        }
    }

    @Override
    protected void onStateRefresh() {

    }

//    @Override
//    protected DelegateAdapter createAdapter() {
//        return null;
//    }

    /**
     * set title
     * @param titleName
     */
    protected void setTitle(String titleName) {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitle.setText(titleName);
    }}