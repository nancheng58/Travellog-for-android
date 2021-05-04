package com.code.travellog.core.view;

import com.airbnb.lottie.LottieAnimationView;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.core.view.user.LoginActivity;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mvvm.base.BaseActivity;
import com.mvvm.http.HttpHelper;
import com.tencent.mmkv.MMKV;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description: 起始页界面
 * @date: 2021/2/15
 */
public class LaunchActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    private MMKV kv ;
    @BindView(R.id.lottie_imageview)
    LottieAnimationView lottieImageview;
    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        loadManager.showSuccess();
        lottieImageview.playAnimation();
        setListener();


    }
    private void loop ()
    {
        lottieImageview.playAnimation();
        setListener();
    }
    private void setListener() {
        ValueAnimator animator = new ValueAnimator().ofFloat(0f, 1f).setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                lottieImageview.setProgress(Float.parseFloat
//                        (animation.getAnimatedValue().toString()));
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //Logg.d("onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //Logger.d("onAnimationEnd");
//                goToMainActivity();
                //loop();

                kv = MMKV.defaultMMKV();
                boolean isLogin = kv.decodeBool("isLogin");
                Intent intent = new Intent();
                if (! isLogin ) {
                    //如果用户是第一次安装应用并进入
                    intent.setClass(LaunchActivity.this, LoginActivity.class);
                } else {
                    //更新用户信息
//                    String userName = kv.decodeString("userName");
                    intent.setClass(LaunchActivity.this, MainActivity.class);
                    updateUserInfo();
                }

                finish();
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //Logger.d("onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //lottieImageview.playAnimation();
                //Logger.d("onAnimationRepeat");
            }
        });
        animator.start();
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
                        Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                });

    }


}
