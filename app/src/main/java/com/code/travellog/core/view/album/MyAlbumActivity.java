package com.code.travellog.core.view.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.code.travellog.R;
import com.gyf.immersionbar.ImmersionBar;
import com.mvvm.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/7
 */
public class MyAlbumActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_title_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.tv_makealbum)
    SuperTextView tvText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_myalbum;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        ButterKnife.bind(this);
        rlTitleBar.setVisibility(View.VISIBLE);
        tvTitle.setText("我的影集");
        tvText.setLeftTopTextIsBold(true);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this);
        tvText.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                Intent intent = new Intent(MyAlbumActivity.this , MakeAlbumActivity.class);
                startActivity(intent);
            }
        });
//        tvText.setRightImageViewClickListener(new SuperTextView.OnRightImageViewClickListener() {
//            @Override
//            public void onClickListener(ImageView imageView) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
