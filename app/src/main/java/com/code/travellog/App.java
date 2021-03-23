package com.code.travellog;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.code.travellog.config.URL;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.mvvm.http.HttpHelper;
import com.mvvm.stateview.ErrorState;
import com.mvvm.stateview.LoadingState;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mmkv.MMKV;
import com.tqzhang.stateview.core.LoadState;
import com.zxy.tiny.Tiny;

import java.util.HashSet;
import java.util.Set;

import tech.spiro.addrparser.io.RegionDataInput;
import tech.spiro.addrparser.io.file.JSONFileRegionDataInput;
import tech.spiro.addrparser.parser.LocationParserEngine;
import tech.spiro.addrparser.parser.ParserEngineException;


/**
 * @author：tqzhang on 18/4/19 17:57
 */
public class App extends Application implements ComponentCallbacks2 {
    public static App mInstance;
    public static Context mContext;
    public static LocationParserEngine regionDataengine;
    public static PictureParameterStyle mPictureParameterStyle;
    private Set<Activity> mActivities;
    static { // 防止内存泄漏
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setEnableHeaderTranslationContent(false);
                layout.setPrimaryColorsId(R.color.white, android.R.color.background_dark);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
        RegionDataInput regionDataInput = new JSONFileRegionDataInput("path/china-region.json");
        regionDataengine = new LocationParserEngine(regionDataInput);
        try {
            regionDataengine.init();
        } catch (ParserEngineException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static App instance() {
        return mInstance;
    }

    public PictureParameterStyle getWhiteStyle() {
        // 相册主题

        if(mPictureParameterStyle == null){
            PictureParameterStyle mPictureParameterStyle = new PictureParameterStyle();
            // 是否改变状态栏字体颜色(黑白切换)
            mPictureParameterStyle.isChangeStatusBarFontColor = true;
            // 是否开启右下角已完成(0/9)风格
            mPictureParameterStyle.isOpenCompletedNumStyle = false;
            // 是否开启类似QQ相册带数字选择风格
            mPictureParameterStyle.isOpenCheckNumStyle = true;
            // 相册状态栏背景色
            mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#FFFFFF");
            // 相册列表标题栏背景色
            mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#FFFFFF");
            // 相册列表标题栏右侧上拉箭头
            mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_orange_arrow_up;
            // 相册列表标题栏右侧下拉箭头
            mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_orange_arrow_down;
            // 相册文件夹列表选中圆点
            mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
            // 相册返回箭头
            mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back_arrow;
            // 标题栏字体颜色
            mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getApplicationContext(), R.color.app_color_black);
            // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
            mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getApplicationContext(), R.color.app_color_black);
            // 相册列表勾选图片样式
            mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_num_selector;
            // 相册列表底部背景色
            mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getApplicationContext(), R.color.picture_color_fa);
            // 已选数量圆点背景样式
            mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
            // 相册列表底下预览文字色值(预览按钮可点击时的色值)
            mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getApplicationContext(), R.color.picture_color_fa632d);
            // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
            mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getApplicationContext(), R.color.picture_color_9b);
            // 相册列表已完成色值(已完成 可点击色值)
            mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getApplicationContext(), R.color.picture_color_fa632d);
            // 相册列表未完成色值(请选择 不可点击色值)
            mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getApplicationContext(), R.color.picture_color_9b);
            // 预览界面底部背景色
            mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getApplicationContext(), R.color.picture_color_white);
            // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
            mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_checkbox;
            // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
            mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(getApplicationContext(), R.color.app_color_53575e);
            // 外部预览界面删除按钮样式
            mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_black_delete;
            // 外部预览界面是否显示删除按钮
            mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
//        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
            mPictureParameterStyle.pictureCompleteText = "确定";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";

//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;

        }
        return mPictureParameterStyle;

    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        new HttpHelper.Builder(this)
                .initOkHttp(mContext)
                .createRetrofit(URL.BASE_URL)
                .build();
        new LoadState.Builder()
                .register(new ErrorState())
                .register(new LoadingState())
                .setDefaultCallback(LoadingState.class)
                .build();
        String mmkv_root_dir = MMKV.initialize(this);
        Log.w("mmkv root dir :",mmkv_root_dir);
        Tiny.getInstance().init(this);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
    public void addActivity(Activity activity) {
        if (mActivities == null) {
            mActivities = new HashSet<>();
        }
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (mActivities != null) {
            mActivities.remove(activity);
        }
    }
    public void removeAllActivity() {
        if (mActivities != null) {
            synchronized (mActivities) {
                for (Activity activity :
                        mActivities) {
                    activity.finish();
                }
            }
        }
    }
    public void exitApp() {
        if (mActivities != null) {
            synchronized (mActivities) {
                for (Activity activity :
                        mActivities) {
                    activity.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
