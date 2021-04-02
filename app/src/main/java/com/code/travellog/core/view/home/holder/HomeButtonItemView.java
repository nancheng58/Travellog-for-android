package com.code.travellog.core.view.home.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.home.ButtonPojo;
import com.code.travellog.core.view.album.MakeAlbumActivity;
import com.code.travellog.core.view.map.MapActivity;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/30
 */
public class HomeButtonItemView extends AbsItemHolder<ButtonPojo, HomeButtonItemView.ViewHolder> {


    public HomeButtonItemView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_home_card;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ButtonPojo buttonPojo) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
        if (buttonPojo.id == 1) {
            holder.card.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, MakeAlbumActivity.class);
                mContext.startActivity(intent);
            });
        } else if (buttonPojo.id == 2){
            Glide.with(mContext).load(R.drawable.home_buttom2).placeholder(R.color.black_e8e8e8).into(holder.image);
            holder.title.setText("足迹地图");
            holder.secondtext.setText("查看个性化旅游回忆");
            holder.card.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, MapActivity.class);
                mContext.startActivity(intent);
            });
        }
    }


    public static class ViewHolder extends AbsHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.secondtext)
        TextView secondtext;
        @BindView(R.id.card)
        MaterialCardView card;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
