package com.code.travellog.util;

import android.content.Context;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.adapter.OneToMany;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.activity.ActivityListVo;
import com.code.travellog.core.data.pojo.album.AlbumPojo;
import com.code.travellog.core.data.pojo.album.AlbumResultDescriptionPojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.core.data.pojo.article.ArticleInfoVo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.book.BookVo;
import com.code.travellog.core.data.pojo.common.TypeVo;
import com.code.travellog.core.data.pojo.correct.WorksListVo;
import com.code.travellog.core.data.pojo.course.CourseInfoVo;
import com.code.travellog.core.data.pojo.dynamic.DynamicInfoVo;
import com.code.travellog.core.data.pojo.followdraw.FollowDrawInfoVo;
import com.code.travellog.core.data.pojo.geo.CityPojo;
import com.code.travellog.core.data.pojo.home.ButtonPojo;
import com.code.travellog.core.data.pojo.home.CategoryVo;
import com.code.travellog.core.data.pojo.live.LiveRecommendVo;
import com.code.travellog.core.data.pojo.material.MaterialInfoVo;
import com.code.travellog.core.data.pojo.material.MatreialSubjectVo;
import com.code.travellog.core.data.pojo.qa.QaListVo;
import com.code.travellog.core.data.pojo.video.VideoPojo;
import com.code.travellog.core.view.activity.holder.ActivityItemHolder;
import com.code.travellog.core.view.album.holder.AlbumResultHeaderHolder;
import com.code.travellog.core.view.album.holder.AlbumResultStepHolder;
import com.code.travellog.core.view.book.holder.BookListHolder;
import com.code.travellog.core.view.common.TypeItemView;
import com.code.travellog.core.view.correct.holder.CorrectItemHolder;
import com.code.travellog.core.view.video.holder.VideoItemHolder;
import com.code.travellog.core.view.home.holder.AlbumItemHolder;
import com.code.travellog.core.view.home.holder.CategoryItemView;
import com.code.travellog.core.view.home.holder.HomeButtonItemView;
import com.code.travellog.core.view.live.holder.LiveItemHolder;
import com.code.travellog.core.view.live.holder.LiveListItemHolder;
import com.code.travellog.core.view.map.MapItemHolder;

import com.code.travellog.core.view.qa.holder.QaListItemHolder;
import com.code.travellog.widget.banner.BannerItemView;

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
//                .bind(TypeVo.class, new TypeItemView(context))
//                .bind(CategoryVo.class, new CategoryItemView(context))
//                .bind(BookList.class, new BookItemHolder(context))
//                .bind(CourseInfoVo.class, new ForumItemHolder(context))
//                .bind(LiveRecommendVo.class, new HomeLiveItemView(context))
//                .bind(MatreialSubjectVo.class, new HomeMaterialItemView(context));
    }
    public DelegateAdapter.Builder getFootprintAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(BannerListVo.class, new BannerItemView(context))
                .bind(WorksListVo.Works.class, new CorrectItemHolder(context));
    }

    public DelegateAdapter.Builder getAlbumResultAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(AlbumResultPojo.class, new AlbumResultHeaderHolder(context))
                .bind(AlbumResultDescriptionPojo.class,new AlbumResultStepHolder(context));
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
                .bind(WorksListVo.Works.class, new CorrectItemHolder(context));
    }

    public DelegateAdapter.Builder getBookAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(BookVo.class, new BookListHolder(context));
    }

    public DelegateAdapter.Builder getActivityAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(ActivityListVo.DataBean.class, new ActivityItemHolder(context));
    }




    public DelegateAdapter.Builder getCourseListAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(CourseInfoVo.class, new VideoItemHolder(context));

    }


    public DelegateAdapter.Builder getQaAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(QaListVo.DataBean.class, new QaListItemHolder(context));
    }
    public DelegateAdapter.Builder getLiveAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(LiveRecommendVo.class, new LiveListItemHolder(context));
    }

    public DelegateAdapter.Builder getLiveRemAdapter(Context context) {
        return new DelegateAdapter.Builder<>()
                .bind(LiveRecommendVo.class, new LiveItemHolder(context));
    }


}
