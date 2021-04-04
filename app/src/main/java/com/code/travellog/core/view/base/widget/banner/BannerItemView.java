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
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.banner.BannerListVo;

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
    protected void onBindViewHolder(@NonNull BannerItemView.ViewHolder holder, @NonNull final BannerListVo bannerAdListVo) {
        holder.mBannerView.delayTime(5).setBannerView(() -> {
            List<ImageView> imageViewList = new ArrayList<>();
            for (int i = 0; i < bannerAdListVo.images.size(); i++) {
                ImageView mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                Glide.with(mContext).load(URL.IMAGE_URL+bannerAdListVo.images.get(i)).centerCrop().into(mImageView);
                imageViewList.add(mImageView);
            }
            return imageViewList;
        }).build(bannerAdListVo.images);

    }

    @Override
    protected void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        RecyclerView.LayoutParams clp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (clp instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) clp).setFullSpan(true);
        }
    }

    static class ViewHolder extends AbsHolder {

        private BannerView mBannerView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBannerView = getViewById(R.id.banner);
        }

    }

}
