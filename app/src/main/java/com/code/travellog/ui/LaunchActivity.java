package com.code.travellog.ui;

import com.code.travellog.R;
import com.mvvm.base.BaseActivity;
import com.tencent.mmkv.MMKV;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * @deprecated: 起始页界面
 * @date: 2021/2/24
 */
public class LaunchActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MMKV kv = MMKV.defaultMMKV();

//                kv.removeValueForKey("isLogin");
                boolean isLogin = kv.decodeBool("isLogin");
                Intent intent = new Intent();
                if (! isLogin ) {
//                    sp.edit().putBoolean("isLogin", false).commit();
                    //如果用户是第一次安装应用并进入
                    intent.setClass(LaunchActivity.this, LoginActivity.class);
                } else {
                    intent.setClass(LaunchActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();

            }
        }, 1000);
    }

}