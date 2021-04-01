package com.code.travellog.ui;

import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.ToastUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.mvvm.base.BaseActivity;
import com.mvvm.http.HttpHelper;
import com.pharid.splash.lib.activity.AnimatedSplash;
import com.pharid.splash.lib.cnst.Flags;
import com.pharid.splash.lib.model.ConfigSplash;
import com.tencent.mmkv.MMKV;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import butterknife.internal.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description: 起始页界面
 * @date: 2021/2/24
 */
public class LaunchActivity extends AnimatedSplash {
    private Handler mHandler = new Handler();
    private MMKV kv ;

    @Override
    public void initSplash(ConfigSplash configSplash) {
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.app_color_blue); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher_project); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
//        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
//        configSplash.setOriginalHeight(600); //in relation to your svg (path) resource
//        configSplash.setOriginalWidth(600); //in relation to your svg (path) resource
//        configSplash.setAnimPathStrokeDrawingDuration(1000);
//        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
//        configSplash.setPathSplashStrokeColor(R.color.app_color_red); //any color you want form colors.xml
//        configSplash.setAnimPathFillingDuration(1000);
//        configSplash.setPathSplashFillColor(R.color.app_color_red); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("Travel Log");
        configSplash.setTitleTextColor(R.color.black_e8e8e8);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.RotateInDownLeft);
        configSplash.setTitleFont("fonts/streatwear.otf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
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
