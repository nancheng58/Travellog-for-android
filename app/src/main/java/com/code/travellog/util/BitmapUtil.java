package com.code.travellog.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
    public static Bitmap ChangeSize(Bitmap bitmap, int newHeight, int newWidth) {

    //计算压缩的比率

    float scaleWidth = ((float) newWidth) / bitmap.getWidth();

    float scaleHeight = ((float) newHeight) / bitmap.getHeight();

    //获取想要缩放的matrix

    Matrix matrix =new Matrix();

    matrix.postScale(scaleWidth, scaleHeight);

    //获取新的bitmap
    return Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(), matrix,true);

    }

}
