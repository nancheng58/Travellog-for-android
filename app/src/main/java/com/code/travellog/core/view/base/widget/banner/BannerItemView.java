package com.code.travellog.core.view.base.widget.banner;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.banner.BannerVo;
import com.code.travellog.core.view.home.holder.CategoryItemView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.transformer.DepthPageTransformer;
import com.youth.banner.transformer.RotateDownPageTransformer;
import com.youth.banner.transformer.RotateYTransformer;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.transformer.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @time 2021/3/4 0:36
 */

public class BannerItemView extends AbsItemHolder<BannerListVo, BannerItemView.ViewHolder> {

    public BannerItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.common_banner_view;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final BannerListVo bannerAdListVo) {
        //StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.banner.getLayoutParams();
        //layoutParams.setFullSpan(true);
        List<BannerVo> bannerVoList = new ArrayList<>();
        for(int i = 0 ;i < bannerAdListVo.images.size();i++){
            BannerVo bannerVo = new BannerVo();
            bannerVo.imgUrl = bannerAdListVo.images.get(i);
            bannerVoList.add(bannerVo);
        }

        holder.banner.setAdapter(new BannerImageAdapter<BannerVo>(bannerVoList) {
            @Override
            public void onBindView(BannerImageHolder holder, BannerVo data, int position, int size) {
                //holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                Glide.with(holder.itemView)
                        .load(URL.IMAGE_URL+data.imgUrl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                        .into(holder.imageView);
            }
        }).setIndicator(new CircleIndicator(mContext))
                .setPageTransformer(new ScaleInTransformer()).setBannerRound((float) 0.3)
        .setBannerGalleryEffect(5,3, (float) 1);
//        holder.mBannerView.delayTime(5).setBannerView(() -> {
//            List<ImageView> imageViewList = new ArrayList<>();
//            for (int i = 0; i < bannerAdListVo.images.size(); i++) {
//                ImageView mImageView = new ImageView(mContext);
//                mImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                Glide.with(mContext).load(URL.IMAGE_URL+bannerAdListVo.images.get(i)).centerCrop().into(mImageView);
//                imageViewList.add(mImageView);
//            }
//            return imageViewList;
//        }).build(bannerAdListVo.images);

    }
//
//    @Override
//    protected void onViewAttachedToWindow(@NonNull ViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        RecyclerView.LayoutParams clp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
//        if (clp instanceof StaggeredGridLayoutManager.LayoutParams) {
//            ((StaggeredGridLayoutManager.LayoutParams) clp).setFullSpan(true);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
//        ImageView imageView = new ImageView(parent.getContext());
//        //注意，必须设置为match_parent，这个是viewpager2强制要求的
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        return new ViewHolder(imageView);
//    }
//
//    @Override
//    public void onBindView(ViewHolder holder, BannerListVo data, int position, int size) {
//
//    }
    @Override
    protected void onViewAttachedToWindow(@NonNull BannerItemView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        RecyclerView.LayoutParams clp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (clp instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) clp).setFullSpan(true);
        }
    }
    static class ViewHolder extends AbsHolder {

        private BannerView mBannerView;
        //private BannerViewPager<BannerVo> mViewPager;
        public Banner banner;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = getViewById(R.id.banner);
//            mViewPager = getViewById(R.id.banner_view);
//            mViewPager.setLifecycleRegistry(getLifecycle())
//                    .setAdapter(new SimpleAdapter())
//                    .create();
        }

    }

}
