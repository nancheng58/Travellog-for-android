package com.code.travellog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.code.travellog.R;
import com.mvvm.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/7
 */
public class MyAlbumActivity extends BaseActivity {
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
        ButterKnife.bind(this);

        tvText.setRightImageViewClickListener(new SuperTextView.OnRightImageViewClickListener() {
            @Override
            public void onClickListener(ImageView imageView) {
                Intent intent = new Intent(MyAlbumActivity.this , MakeAlbumActivity.class);
                startActivity(intent);
            }
        });
    }

}