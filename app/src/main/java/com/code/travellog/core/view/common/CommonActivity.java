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
import com.code.travellog.core.view.EmptyFragment;
import com.code.travellog.core.view.color.ColorFragment;

import com.code.travellog.core.view.poetry.PoetryFragment;
import com.code.travellog.core.view.styletransfer.StyletransferFragment;
import com.code.travellog.core.view.superresolution.SuperResolutionFragment;
import com.code.travellog.core.view.weather.WeatherFragment;
import com.mvvm.base.BaseActivity;
import com.mvvm.base.BaseFragment;

/**
 * @description 界面选择
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
            case Constants.POETRY:
                commitFragment(PoetryFragment.newInstance());
                break;
            case Constants.STYLE:
                commitFragment(StyletransferFragment.newInstance());
                break;
            case Constants.SUPERVISION:
                commitFragment(EmptyFragment.newInstance());
                break;
            case Constants.SUPER:
                commitFragment(SuperResolutionFragment.newInstance());
                break;
            case Constants.OBJECT:
                commitFragment(EmptyFragment.newInstance());
            case Constants.ACTIVITY:
                commitFragment(EmptyFragment.newInstance());
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
