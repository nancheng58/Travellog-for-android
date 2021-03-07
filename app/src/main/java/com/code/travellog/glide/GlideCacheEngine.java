package com.code.travellog.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.engine.CacheResourcesEngine;

import java.io.File;

/**
 * @description Glide 缓存
 * @param
 * @return
 * @time 2021/3/7 17:23
 */

public class GlideCacheEngine implements CacheResourcesEngine {

    @Override
    public String onCachePath(Context context, String url) {
        File cacheFile;
        cacheFile = getCacheFileTo4x(context, url);
        return cacheFile != null ? cacheFile.getAbsolutePath() : "";
    }


    private GlideCacheEngine() {
    }

    private static GlideCacheEngine instance;

    public static GlideCacheEngine createCacheEngine() {
        if (null == instance) {
            synchronized (GlideCacheEngine.class) {
                if (null == instance) {
                    instance = new GlideCacheEngine();
                }
            }
        }
        return instance;
    }
    /**
     * 根据url获取图片缓存
     * Glide 4.x请调用此方法
     * 注意：此方法必须在子线程中进行
     *
     * @param context
     * @param url
     * @return
     */
    public static File getCacheFileTo4x(Context context, String url) {
        try {
            return Glide.with(context).downloadOnly().load(url).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
