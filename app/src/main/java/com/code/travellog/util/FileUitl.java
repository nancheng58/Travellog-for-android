package com.code.travellog.util;

import android.graphics.BitmapFactory;

import java.io.File;

/**
 * @description: 文件工具类
 * @date: 2021/3/28
 */
public class FileUitl {
    public static String getImgMimeType(String imgFilename) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFilename, options);
        return options.outMimeType;
    }
    public static String getImgType(String imgFilename) {
        String fileType = getImgMimeType(imgFilename);
        fileType = fileType.substring(6);
        return "."+fileType;
    }
}
