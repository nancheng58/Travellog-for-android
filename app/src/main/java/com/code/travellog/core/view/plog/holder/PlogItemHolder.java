package com.code.travellog.core.view.plog.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.correct.WorksListVo;
import com.code.travellog.glide.GlideCircleTransform;
import com.code.travellog.glide.GlideRoundTransform;
import com.code.travellog.util.DisplayUtil;
import com.code.travellog.core.view.base.widget.CustomHeightImageView;

/**
 * @description
 * @time 2021/4/4 9:25
 */

public class PlogItemHolder extends AbsItemHolder<WorksListVo.Works, PlogItemHolder.ViewHolder> {

    private int commonWidth;

    public PlogItemHolder(Context context) {
        super(context);
        commonWidth = (int) (((float) DisplayUtil.getScreenWidth(mContext)  // 设备绝对宽度
                - 30 * DisplayUtil.getDisplayDensity(mContext)) / 2);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.correct_item;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlogItemHolder.ViewHolder holder, @NonNull final WorksListVo.Works data) {
        String mStatus = "0";
        if (mStatus.equals(data.correct.status)) {
            float dv = (float) data.correct.source_pic.img.l.h / (float) data.correct.source_pic.img.l.w;
            holder.mCHImageView.setHeight((int) (dv * commonWidth));
            Glide.with(mContext).load(data.correct.source_pic.img.l.url)
                    .placeholder(R.color.black_e8e8e8)
                    .transform(new GlideRoundTransform(mContext, 4))
                    .into(holder.mCHImageView);
        } else {
            if (data.correct.correct_pic.img != null) {
                float dv = (float) data.correct.correct_pic.img.l.h / (float) data.correct.correct_pic.img.l.w;
                holder.mCHImageView.setHeight((int) (dv * commonWidth));
                Glide.with(mContext).load(data.correct.correct_pic.img.l.url)
                        .placeholder(R.color.black_e8e8e8)
                        .transform(new GlideRoundTransform(mContext, 4)).into(holder.mCHImageView);
            } else {
                float dv = (float) data.correct.source_pic.img.l.h / (float) data.correct.source_pic.img.l.w;
                holder.mCHImageView.setHeight((int) (dv * commonWidth));
                Glide.with(mContext).load(data.correct.source_pic.img.l.url)
                        .placeholder(R.color.black_e8e8e8)
                        .transform(new GlideRoundTransform(mContext, 4)).into(holder.mCHImageView);
            }
        }

        Glide.with(mContext).load(data.correct.teacher_info.avatar).transform(new GlideCircleTransform(mContext)).into(holder.mUserIcon);
        holder.mTvDesc.setText(data.correct.content);
        holder.mUserName.setText(data.correct.teacher_info.sname);

    }

    public class ViewHolder extends AbsHolder {
        private CustomHeightImageView mCHImageView;
        private TextView mTvDesc, mUserName;
        private ImageView mUserIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCHImageView = getViewById(R.id.iv_custom_image);
            mTvDesc = getViewById(R.id.tv_desc);
            mUserName = getViewById(R.id.tv_user_name);
            mUserIcon = getViewById(R.id.iv_user_icon);
        }

    }
}