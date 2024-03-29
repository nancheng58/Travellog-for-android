package com.code.travellog.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.code.travellog.config.Constants;

/**
 * @description
 * @time 2021/2/3 8:39
 */

public class GlideConfig implements GlideModule {
    public final static String TAG = "GlideConfig";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, Constants.MAX_CACHE_DISK_SIZE));
        builder.setMemoryCache(new LruResourceCache(Constants.MAX_CACHE_MEMORY_SIZE));
        builder.setBitmapPool(new LruBitmapPool(Constants.MAX_CACHE_MEMORY_SIZE));
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}