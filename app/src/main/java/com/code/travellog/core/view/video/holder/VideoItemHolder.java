package com.code.travellog.core.view.video.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.album.AlbumPojo;
import com.code.travellog.core.data.pojo.video.VideoPojo;
import com.code.travellog.glide.GlideCircleTransform;
import com.code.travellog.util.DisplayUtil;
import com.tencent.mmkv.MMKV;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description 主页个人影集列表
 * @time 2021/3/30 21:16
 */

public class VideoItemHolder extends AbsItemHolder<VideoPojo, VideoItemHolder.ViewHolder> {
    private int commonWidth;

    public VideoItemHolder(Context context) {
        super(context);
        commonWidth = (int) ((float) DisplayUtil.getScreenWidth(mContext)
                / 2);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_home_album;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull VideoPojo videoPojo) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                commonWidth, (int) (0.56 * commonWidth));
        holder.ivVideoImage.setLayoutParams(params);
        holder.ivVideoImage.setScaleType(ImageView.ScaleType.FIT_XY);
        // TODO
        Glide.with(mContext).load(URL.IMAGE_URL+videoPojo.reference.movie_cover).placeholder(R.color.black_e8e8e8).into(holder.ivVideoImage);
        Glide.with(mContext).load(URL.IMAGE_URL+videoPojo.avatar).transform(new GlideCircleTransform(mContext)).into(holder.ivIcon);
        holder.tvCreateTime.setText(videoPojo.reference.create_time);
        holder.tvVideoTitle.setText(videoPojo.reference.movie_title);
        Random random = new Random();
        holder.tvLookNum.setText(new StringBuilder(String.valueOf(random.nextInt(101))).append("人看过"));
    }


    static class ViewHolder extends AbsHolder {

        @BindView(R.id.iv_video_image)
        ImageView ivVideoImage;
        @BindView(R.id.tv_look_num)
        TextView tvLookNum;
        @BindView(R.id.tv_video_title)
        TextView tvVideoTitle;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
