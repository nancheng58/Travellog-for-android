package com.code.travellog.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description:
 * @date: 2021/3/17
 */
public class AssetsUtil {
    public static byte[] getAssetsStyle(Context context,String filemname){
        byte[]  buffer1 = null;
        InputStream is1 = null;
        try {
            is1 = context.getResources().getAssets().open(filemname);
            int lenght1 = is1.available();
            buffer1 = new byte[lenght1];
            is1.read(buffer1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (is1!=null) {
                    is1.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer1;
    }

}
