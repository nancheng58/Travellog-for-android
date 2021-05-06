package com.code.travellog.core.view.plog.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.code.travellog.util.BitmapUtil;
import com.code.travellog.util.DisplayUtil;
import com.code.travellog.core.view.base.widget.CustomHeightImageView;
import com.code.travellog.util.ImageSaveUtil;
import com.code.travellog.util.ToastUtils;
import com.code.travellog.util.ViewUtils;

/**
 * @description
 * @time 2021/4/3 0:19
 */

public class PlogPicHolder extends AbsItemHolder<PlogPojo, PlogPicHolder.ViewHolder> {

    public PlogPicHolder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_plog_pic;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull PlogPicHolder.ViewHolder holder, @NonNull PlogPojo plogPojo) {
        RecyclerView.LayoutParams clp = (RecyclerView.LayoutParams) holder.ll_root.getLayoutParams();
        if (clp instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) clp).setFullSpan(true);
        }
//        if (!TextUtils.isEmpty(object.data.tweet_info.avatar)) {
//            Glide.with(mContext).load(object.data.teacher_info.avatar).transform(new GlideCircleTransform(mContext)).into(holder.teacherIcon);
//        }
        if(plogPojo.avatar.startsWith("http")){
            Glide.with(mContext).load(plogPojo.avatar).transform(new GlideCircleTransform(mContext)).into(holder.workPic);
        }
        else Glide.with(mContext).load(URL.IMAGE_URL+plogPojo.avatar).transform(new GlideCircleTransform(mContext)).into(holder.workPic);

//        holder.teacherName.setText(object.data.teacher_info.sname);
        holder.userName.setText(plogPojo.uname);
//        PlogPicVo correct_img = null;
//        if (object.data.status.equals("1")) {
//            if (object.data.correct_pic == null) {
//                correct_img = object.data.source_pic;
//            } else {
//                correct_img = object.data.correct_pic;
//            }
//
//        } else {
//            correct_img = object.data.source_pic;
//        }
        int hightc = DisplayUtil.getScreenWidth(mContext) * plogPojo.photo_height / plogPojo.photo_width;
        LinearLayout.LayoutParams cparams = new LinearLayout.LayoutParams(
                DisplayUtil.getScreenWidth(mContext), hightc);
        holder.imagePic.setLayoutParams(cparams);
        Glide.with(mContext).load(URL.IMAGE_URL+plogPojo.result_msg).placeholder(R.color.black_e8e8e8).override(DisplayUtil.getScreenWidth(mContext), hightc).into(holder.imagePic);
        holder.userTag.removeAllViews();
        if (!TextUtils.isEmpty(plogPojo.photo_title) ) {
            holder.userTag.addView(ViewUtils.CreateTagView(mContext, plogPojo.photo_title));
        }

        if (!TextUtils.isEmpty(plogPojo.photo_description)) {
            holder.userTag.addView(ViewUtils.CreateTagView(mContext, plogPojo.photo_description));
        }
        holder.saveBtn.setOnClickListener(v -> {

            new Thread(()->{
                Bitmap bitmap = BitmapUtil.returnBitMap(URL.IMAGE_URL+plogPojo.result_msg);
                ImageSaveUtil.saveAlbum(mContext,bitmap, Bitmap.CompressFormat.JPEG,100,true);
            }).start();
            ToastUtils.showToast("保存成功");
        });
        holder.shareBtn.setOnClickListener(v -> {
            new Thread(()->{
                Bitmap bitmap = BitmapUtil.returnBitMap(URL.IMAGE_URL+plogPojo.result_msg);
                Uri uri= ImageSaveUtil.saveAlbum(mContext,bitmap, Bitmap.CompressFormat.JPEG,100,true);
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND); //设置分享行为
                share_intent.setType("image/*") ;  //设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_STREAM, uri);
                share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");//添加分享内容标题
                share_intent = Intent.createChooser(share_intent, "分享");
                mContext.startActivity(share_intent);
            }).start();
        });
    }

    static class ViewHolder extends AbsHolder {
        private CustomHeightImageView imagePic;
        private ImageView workPic, teacherIcon;
        private TextView teacherName, userName;
        private LinearLayout ll_root, userTag;
        private Button saveBtn;
        private Button shareBtn;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_root = getViewById(R.id.ll_root);
            userTag = getViewById(R.id.ll_user_tag);
            imagePic = getViewById(R.id.iv_pic);
            workPic = getViewById(R.id.iv_header_pic);
            userName = getViewById(R.id.tv_name);
            saveBtn = getViewById(R.id.btn_save);
            shareBtn = getViewById(R.id.btn_share);
        }

    }


}
