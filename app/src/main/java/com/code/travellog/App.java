package com.code.travellog;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.code.travellog.config.URL;
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
import com.tencent.mmkv.MMKV;
import com.tqzhang.stateview.core.LoadState;
import com.zxy.tiny.Tiny;

import java.util.HashSet;
import java.util.Set;


/**
 * @author：tqzhang on 18/4/19 17:57
 */
public class App extends Application implements ComponentCallbacks2 {
    public static App mInstance;
    public static Context mContext;
    private Set<Activity> mActivities;
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_ffca00, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static App instance() {
        return mInstance;
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
