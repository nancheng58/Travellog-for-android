package com.code.travellog.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    /**
     * 截图鱼眼效果
     *
     * @param data yuv视频流数据
     * @param width 宽
     * @param height 高
     * @return
     */
    public static Bitmap byteArrayRGBABitmap(byte[] data, int width, int height) {

        int[] ints = yuvI420toARGB(data, width, height);
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(ints, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static int[] yuvI420toARGB(byte[] yuv, int width, int height) {

        int iterations = width * height;

        int[] rgb = new int[iterations];
        for (int i = 0; i < iterations; i++) {
            int nearest = (i / width) / 2 * (width / 2) + (i % width) / 2;
            int y = yuv[i] & 0x000000ff;
            int u = yuv[iterations + nearest] & 0x000000ff;

            int v = yuv[iterations + iterations / 4 + nearest] & 0x000000ff;

            int b = (int) (y + 1.8556 * (u - 128));

            int g = (int) (y - (0.4681 * (v - 128) + 0.1872 * (u - 128)));

            int r = (int) (y + 1.5748 * (v - 128));

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            rgb[i] = (0xff000000) | (0x00ff0000 & r << 16) | (0x0000ff00 & g << 8) | (0x000000ff & b);
        }
        return rgb;

    }

    //保存文件到指定路径
    public static boolean  saveMyBitmap(Bitmap bitmap) {

        File sd = Environment.getExternalStorageDirectory();

        File destDir = new File(sd.getPath() + "/DCIM/" + "Camera/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String tmpfile = sd.getPath() + "/DCIM/" + "Camera/" + System.currentTimeMillis() + ".jpg";

        BufferedOutputStream bos = null;
        try {
            try {
                bos = new BufferedOutputStream(new FileOutputStream(tmpfile));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bitmap.recycle();
        } catch (Exception e) {
//            Log.e(TAG, "截屏失败");
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bos != null)
                try {
                    bos.close();
                    return true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return false;
        // 最后通知图库更新
//        return new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + tmpfile));

    }
}
