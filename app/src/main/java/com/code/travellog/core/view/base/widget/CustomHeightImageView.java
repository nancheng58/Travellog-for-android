package com.code.travellog.core.view.base.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @description
 * @time 2021/1/16 0:34
 */

public class CustomHeightImageView extends AppCompatImageView {

    private double mHeightRatio;
    private int mHeight;

    public CustomHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHeightImageView(Context context) {
        super(context);
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public void setHeight(int height) {
        if (height != mHeight) {
            mHeight = height;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0 || mHeight > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, mHeight);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}