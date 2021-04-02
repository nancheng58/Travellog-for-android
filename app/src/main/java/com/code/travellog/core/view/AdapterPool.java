package com.code.travellog.core.view;
import android.content.Context;
import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.core.data.pojo.album.AlbumPojo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.common.TypeVo;
import com.code.travellog.core.data.pojo.correct.WorksListVo;
import com.code.travellog.core.data.pojo.geo.CityPojo;
import com.code.travellog.core.data.pojo.home.ButtonPojo;
import com.code.travellog.core.data.pojo.home.CategoryVo;
import com.code.travellog.core.data.pojo.video.VideoPojo;
import com.code.travellog.core.view.common.TypeItemView;
import com.code.travellog.core.view.plog.holder.PlogItemHolder;
import com.code.travellog.core.view.video.holder.VideoItemHolder;
import com.code.travellog.core.view.home.holder.AlbumItemHolder;
import com.code.travellog.core.view.home.holder.CategoryItemView;
import com.code.travellog.core.view.home.holder.HomeButtonItemView;
import com.code.travellog.core.view.map.MapItemHolder;
import com.code.travellog.core.view.base.widget.banner.BannerItemView;

/**
 * @description
 * @time 2021/2/30 19:56
 */

public class AdapterPool {

    private static AdapterPool adapterPool;

    public static AdapterPool newInstance() {
        if (adapterPool == null) {
            synchronized (AdapterPool.class) {
                if (adapterPool == null) {
                    adapterPool = new AdapterPool();
                }
            }
        }

        return adapterPool;
    }

    // Home 布局适配器
    public DelegateAdapter.Builder getHomeAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(BannerListVo.class, new BannerItemView(context))
                .bind(CategoryVo.class, new CategoryItemView(context))
                .bind(ButtonPojo.class,new HomeButtonItemView(context))
                .bind(TypeVo.class, new TypeItemView(context))
                .bind(AlbumPojo.class, new AlbumItemHolder(context));
    }


    public DelegateAdapter.Builder getMapAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(CityPojo.class, new MapItemHolder(context));
    }
    public DelegateAdapter.Builder getVideoRemAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(TypeVo.class, new TypeItemView(context))
                .bind(BannerListVo.class, new BannerItemView(context))
                .bind(VideoPojo.class, new VideoItemHolder(context));
    }

    public DelegateAdapter.Builder getWorkAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(BannerListVo.class, new BannerItemView(context))
                .bind(WorksListVo.Works.class, new PlogItemHolder(context));
    }

}
