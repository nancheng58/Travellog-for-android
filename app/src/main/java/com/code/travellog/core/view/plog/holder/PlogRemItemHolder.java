package com.code.travellog.core.view.plog.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.plog.PlogPojo;
import com.code.travellog.util.DisplayUtil;
import com.code.travellog.core.view.base.widget.CustomHeightImageView;
import com.code.travellog.core.view.base.widget.CustomHeightRelativeLayout;

/**
 * @description
 * @time 2021/4/5 0:20
 */

public class PlogRemItemHolder extends AbsItemHolder<PlogPojo, PlogRemItemHolder.ViewHolder> {
    private int commonwidth;

    public PlogRemItemHolder(Context context) {
        super(context);
        commonwidth = (int) (((float) DisplayUtil.getScreenWidth(mContext)
                - 30 * DisplayUtil.getDisplayDensity(mContext)) / 2);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_plog;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull PlogRemItemHolder.ViewHolder holder, @NonNull final PlogPojo data) {
//        String mStatus = "0";
//        if (mStatus.equals(data.status)) {
//            float dv = (float) data.source_pic.img.l.h / (float) data.source_pic.img.l.w;
//            holder.mCHImageView.setHeight((int) (dv * commonwidth));
//            Glide.with(mContext).load(data.source_pic.img.l.url)
//                    .placeholder(R.color.black_e8e8e8)
//                    .transform(new GlideRoundTransform(mContext, 4))
//                    .into(holder.mCHImageView);
//        } else {
//            if (data.correct_pic.img != null) {
//                float dv = (float) data.correct_pic.img.l.h / (float) data.correct_pic.img.l.w;
//                holder.mCHImageView.setHeight((int) (dv * commonwidth));
//                Glide.with(mContext).load(data.correct_pic.img.l.url)
//                        .placeholder(R.color.black_e8e8e8)
//                        .transform(new GlideRoundTransform(mContext, 4))
//                        .into(holder.mCHImageView);
//
//            } else {
//                float dv = (float) data.source_pic.img.l.h / (float) data.source_pic.img.l.w;
//                holder.mCHImageView.setHeight((int) (dv * commonwidth));
//                Glide.with(mContext).load(data.source_pic.img.l.url)
//                        .placeholder(R.color.black_e8e8e8)
//                        .transform(new GlideRoundTransform(mContext, 4))
//                        .into(holder.mCHImageView);
//            }
//        }
//
//        holder.mTvDesc.setText(data.content);
//        holder.mUserName.setText(data.teacher_info.sname);
//        Glide.with(mContext).load(data.teacher_info.avatar).transform(new GlideCircleTransform(mContext)).into(holder.mUserIcon);

    }


    static class ViewHolder extends AbsHolder {
        private CustomHeightImageView mCHImageView;
        private TextView mTvDesc, mUserName;
        private ImageView mUserIcon;
        private CustomHeightRelativeLayout mCHRootLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCHImageView = getViewById(R.id.iv_custom_image);
            mTvDesc = getViewById(R.id.tv_desc);
            mCHRootLayout = getViewById(R.id.custom_root_layout);
            mUserName = getViewById(R.id.tv_user_name);
            mUserIcon = getViewById(R.id.iv_user_icon);
        }

    }


}
