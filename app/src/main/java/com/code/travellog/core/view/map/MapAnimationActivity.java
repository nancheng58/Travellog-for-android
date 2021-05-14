package com.code.travellog.core.view.map;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.code.travellog.R;
import com.code.travellog.core.data.repository.PictureRepository;

import com.code.travellog.core.viewmodel.PictureViewModel;
import com.code.travellog.util.ToastUtils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.gyf.immersionbar.ImmersionBar;
import com.mvvm.base.AbsLifecycleActivity;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/5/4
 */
public class MapAnimationActivity extends AbsLifecycleActivity<PictureViewModel> {

    public boolean flag = false;
    @BindView(R.id.lottie_imageview)
    LottieAnimationView lottieImageview;
    @BindView(R.id.number_progress_bar)
    NumberProgressBar numberProgressBar;
    ValueAnimator animator;
    @Override
    public int getLayoutId() {
        return R.layout.lottie_map;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        loadManager.showSuccess();
        requestPermission();
        setListener();
        dataObserver();
        lottieImageview.playAnimation();
    }
    private void checkPermission(String[] permissions){
        Log.w("msg ","鉴权"+"");
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                finish();return ;
            }

        }
        getData();
    }
    private void getData(){
        try {
            if(mViewModel.getIscache()) flag=true;
            ContentResolver resolver = getContentResolver();
            mViewModel.getGalleryExif(resolver,0);
            int total = mViewModel.getPictureCount(getContentResolver());
            numberProgressBar.setMax(total);
            Log.w("total: ",numberProgressBar.getMax()+"");
            numberProgressBar.setProgress(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void requestPermission() {
        //申请读写SD卡的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            checkPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        } else {
            getData();
        }
    }
    @Override
    protected void dataObserver(){
        registerSubscriber(PictureRepository.EVENT_KEY_PICPROGRESS,int.class).observe(this,index -> {
            if (numberProgressBar.getProgress()< index){
                Log.w("地图图片当前处理到 ",index+"");
                numberProgressBar.setProgress(index);
            }

            Log.w("地图图片当前Progress ",numberProgressBar.getProgress()+"");
        });
    }
    private void loop ()
    {
        if (numberProgressBar.getProgress()!=numberProgressBar.getMax()){
            Log.w("numberProgressBar now : ",numberProgressBar.getProgress()+"");
            lottieImageview.playAnimation();
            setListener();
        }
        else{
            lottieImageview.playAnimation();
            if(mViewModel.getIscache()){
                ToastUtils.showToast("图片地理信息数据更新成功");
                Intent intent = new Intent(MapAnimationActivity.this,MapActivity.class);
                finish();
                startActivity(intent);
            }else {
                lottieImageview.playAnimation();
                setListener();
            }
        }
    }

    private void setListener() {
        animator = new ValueAnimator().ofFloat(0f, 1f).setDuration(2000);
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
                if(flag ){ // 有Cache
                    numberProgressBar.setProgress(numberProgressBar.getMax());
                    ToastUtils.showToast("加载缓存成功");
                    Timer mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.w("timer",1+"");
                            Intent intent = new Intent(MapAnimationActivity.this,MapActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }, 100);

                }
                else loop();
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

}
