package com.code.travellog.ui;

import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.ToastUtils;
import com.mvvm.base.BaseActivity;
import com.mvvm.http.HttpHelper;
import com.tencent.mmkv.MMKV;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description: 起始页界面
 * @date: 2021/2/24
 */
public class LaunchActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    private MMKV kv ;
    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        kv = MMKV.defaultMMKV();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                kv.removeValueForKey("isLogin");
                boolean isLogin = kv.decodeBool("isLogin");
                Intent intent = new Intent();
                if (! isLogin ) {
                    //如果用户是第一次安装应用并进入
                    intent.setClass(LaunchActivity.this, LoginActivity.class);
                } else {
                    //更新用户信息
//                    String userName = kv.decodeString("userName");
                    intent.setClass(LaunchActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();

            }
        }, 1000);
        updateUserInfo();
    }
    @SuppressLint("CheckResult")
    public void updateUserInfo()
    {
        HttpHelper.getInstance().create(ApiService.class).getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<UserPojo>() {

                    @Override
                    public void onSuccess(UserPojo user) {
                        if(user.code!=200) {
                            onFailure(user.msg,user.code);
                            return;
                        }
                        kv.encode("uid",user.data.uid);
                        kv.encode("userName",user.data.uname);
                        kv.encode("phone",user.data.phone);
                        kv.encode("email",user.data.email);
                        kv.encode("gender",user.data.gender);
                        kv.encode("avatar", URL.IMAGE_URL+user.data.avatar);
                        kv.encode("intro",user.data.intro);
                    }
                    @Override
                    public void onFailure(String msg, int code) {
                        ToastUtils.showToast(msg);
                    }
                });
    }
}
