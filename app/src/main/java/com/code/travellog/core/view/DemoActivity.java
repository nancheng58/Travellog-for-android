package com.code.travellog.core.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.code.travellog.R;
import com.czp.library.ArcProgress;
import com.mvvm.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/5/2
 */
public class DemoActivity extends BaseActivity {

    @BindView(R.id.lottie_imageview)
    LottieAnimationView lottieImageview;


    @Override
    public int getLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        loadManager.showSuccess();
        setListener();
//        myProgress01.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
//            @Override
//            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
//                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//                textPaint.setStrokeWidth(35);
//                textPaint.setColor(getResources().getColor(R.color.textColor));
//                String progressStr = String.valueOf(progress + "%");
//                float textX = x - (textPaint.measureText(progressStr) / 2);
//                float textY = y - ((textPaint.descent() + textPaint.ascent()) / 2);
//                canvas.drawText(progressStr, textX, textY, textPaint);
//            }
//        });
//
//        myProgress01.setProgress(50);
    }

    private void setListener() {
//        Log.w("tewww", "rwrwrw");
        ValueAnimator animator = new ValueAnimator().ofFloat(0f, 1f).setDuration(50000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lottieImageview.setProgress(0.5f);
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
                Intent intent = new Intent(DemoActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //Logger.d("onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //Logger.d("onAnimationRepeat");
            }
        });
        animator.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
