package com.code.travellog.core.view.map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.geo.CityPojo;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MapItemHolder extends AbsItemHolder<CityPojo, MapItemHolder.ViewHolder> {


    public MapItemHolder(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_listview;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CityPojo cityPojo) {
        if (!cityPojo.city.equals("")) holder.tvCity.setText(cityPojo.city);
        else holder.tvCity.setText(cityPojo.province);
        holder.tvCountry.setText(cityPojo.county);
//        holder.textView.setHeight(10 + 30);
    }


    static

    public class ViewHolder extends AbsHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_country)
        TextView tvCountry;
        @BindView(R.id.card)
        MaterialCardView card;
        @BindView(R.id.item_country)
        TextView itemCountry;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
