package com.code.travellog.core.view.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.code.travellog.core.view.activity.ActivityListFragment;
import com.code.travellog.core.view.article.ArticleFragment;
import com.code.travellog.core.view.book.BookFragment;
import com.code.travellog.core.view.color.ColorFragment;
import com.code.travellog.core.view.dynamic.DynamicFragment;
import com.code.travellog.core.view.followdraw.FollowDrawFragment;
import com.code.travellog.core.view.live.LiveFragment;
import com.code.travellog.core.view.material.MaterialFragment;
import com.code.travellog.core.view.qa.QaListFragment;
import com.code.travellog.core.view.weather.WeatherFragment;
import com.mvvm.base.BaseActivity;
import com.mvvm.base.BaseFragment;

/**
 * @description 界面选择类
 * @time 2021/3/23 19:54
 */

public class CommonActivity extends BaseActivity implements View.OnClickListener {

    private String typeFragment;

    private FragmentTransaction ft;

    private TextView barTitle;

    @Override
    protected void onStateRefresh() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_common;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        ImageView barBack = findViewById(R.id.iv_back);
        barBack.setVisibility(View.VISIBLE);
        RelativeLayout mTitleLayout = findViewById(R.id.rl_title_bar);
        mTitleLayout.setVisibility(View.VISIBLE);
        barTitle = findViewById(R.id.tv_title);
        getIntentData();
        FragmentManager fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        showFragment();
        barBack.setOnClickListener(this);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            typeFragment = intent.getStringExtra("type_fragment");
            String titleName = intent.getStringExtra("title_name");
            barTitle.setText(titleName);
        }

    }

    private void showFragment() {
        switch (typeFragment) {
            case Constants.WEATHER:
                commitFragment(WeatherFragment.newInstance());
                break;
            case Constants.COLOR:
                commitFragment(ColorFragment.newInstance());
                break;
            case Constants.FOLLOW_DRAW:
                commitFragment(FollowDrawFragment.newInstance());
                break;
            case Constants.DYNAMIC:
                commitFragment(DynamicFragment.newInstance());
                break;
            case Constants.BOOK:
                commitFragment(BookFragment.newInstance());
                break;
            case Constants.LIVE:
                commitFragment(LiveFragment.newInstance());
                break;
            case Constants.ACTIVITY:
                commitFragment(ActivityListFragment.newInstance());
                break;
            case Constants.QA:
                commitFragment(QaListFragment.newInstance());
                break;
            default:
                break;
        }

    }

    public void commitFragment(BaseFragment baseFragment) {
        ft.replace(R.id.fragment_content, baseFragment).commit();
    }

    public static void start(Context context, String typeFragment, String titleName) {
        Intent starter = new Intent(context, CommonActivity.class);
        starter.putExtra("type_fragment", typeFragment);
        starter.putExtra("title_name", titleName);
        context.startActivity(starter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(Fragment fragment : fragmentManager.getFragments())
        {
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }
    @Override
    public void onClick(View v) {
        finish();
    }
}
