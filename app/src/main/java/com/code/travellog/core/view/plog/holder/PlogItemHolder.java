package com.code.travellog.core.view.plog.holder;

import android.content.Context;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.plog.PlogPojo;
import com.code.travellog.glide.GlideCircleTransform;
import com.code.travellog.glide.GlideRoundTransform;
import com.code.travellog.util.DisplayUtil;
import com.code.travellog.core.view.base.widget.CustomHeightImageView;
import com.code.travellog.util.ViewUtils;
import com.tencent.mmkv.MMKV;

/**
 * @description
 * @time 2021/4/4 9:25
 */

public class PlogItemHolder extends AbsItemHolder<PlogPojo, PlogItemHolder.ViewHolder> {

    private int commonWidth;

    public PlogItemHolder(Context context) {
        super(context);
        commonWidth = (int) (((float) DisplayUtil.getScreenWidth(mContext)  // 设备绝对宽度
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
    protected void onBindViewHolder(@NonNull PlogItemHolder.ViewHolder holder, @NonNull final PlogPojo plogPojo) {
        int mStatus = 200;
        if (mStatus == plogPojo.status) {
            float dv = (float) plogPojo.cover_height / (float) plogPojo.cover_width;
            holder.mCHImageView.setHeight((int) (dv * commonWidth));
            Glide.with(mContext).load(URL.IMAGE_URL+plogPojo.photo_cover)
                    .placeholder(R.color.black_e8e8e8)
                    .transform(new GlideRoundTransform(mContext, 4))
                    .into(holder.mCHImageView);
        }
//        else {
//            if (data.correct.correct_pic.img != null) {
//                float dv = (float) data.correct.correct_pic.img.l.h / (float) data.correct.correct_pic.img.l.w;
//                holder.mCHImageView.setHeight((int) (dv * commonWidth));
//                Glide.with(mContext).load(data.correct.correct_pic.img.l.url)
//                        .placeholder(R.color.black_e8e8e8)
//                        .transform(new GlideRoundTransform(mContext, 4)).into(holder.mCHImageView);
//            } else {
//                float dv = (float) data.correct.source_pic.img.l.h / (float) data.correct.source_pic.img.l.w;
//                holder.mCHImageView.setHeight((int) (dv * commonWidth));
//                Glide.with(mContext).load(data.correct.source_pic.img.l.url)
//                        .placeholder(R.color.black_e8e8e8)
//                        .transform(new GlideRoundTransform(mContext, 4)).into(holder.mCHImageView);
//            }
//        }
        if (plogPojo.avatar == null){
            MMKV mmkv = MMKV.defaultMMKV();
            plogPojo.avatar = mmkv.decodeString("avatar");
            plogPojo.uname = mmkv.decodeString("userName");
        }
        if(plogPojo.avatar.startsWith("http")){
            Glide.with(mContext).load(plogPojo.avatar).transform(new GlideCircleTransform(mContext)).into(holder.mUserIcon);
        }
        else Glide.with(mContext).load(URL.IMAGE_URL+plogPojo.avatar).transform(new GlideCircleTransform(mContext)).into(holder.mUserIcon);
        holder.mTvDesc.setText(plogPojo.photo_description);
        holder.mUserName.setText(plogPojo.uname);
        holder.userTag.removeAllViews();
        if (!TextUtils.isEmpty(plogPojo.photo_title) ) {
            holder.userTag.addView(ViewUtils.CreateTagView(mContext, plogPojo.photo_title));
        }

        if (!TextUtils.isEmpty(plogPojo.photo_description)) {
            holder.userTag.addView(ViewUtils.CreateTagView(mContext, plogPojo.photo_description));
        }
    }

    public class ViewHolder extends AbsHolder {
        private CustomHeightImageView mCHImageView;
        private TextView mTvDesc, mUserName;
        private ImageView mUserIcon;
        private LinearLayout userTag;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCHImageView = getViewById(R.id.iv_custom_image);
            mTvDesc = getViewById(R.id.tv_desc);
            mUserName = getViewById(R.id.tv_user_name);
            mUserIcon = getViewById(R.id.iv_user_icon);
            userTag = getViewById(R.id.ll_user_tag);
        }

    }
}
