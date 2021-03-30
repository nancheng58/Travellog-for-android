package com.code.travellog.core.view.map;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.geo.CityPojo;


public class MapItemHolder  extends AbsItemHolder<CityPojo, MapItemHolder.ViewHolder> {

    private Context mContext;

    public MapItemHolder(Context mContext){
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
        holder.textView.setText(cityPojo.county);

//        holder.textView.setHeight(10 + 30);
    }

//    @Override
//    public MapItemHolder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(View.inflate(mContext, R.layout.item_listview, null));
//    }

//    @Override
//    protected void onBindViewHolder(ViewHolder holder, GeoPojo geoPojo) {
////        holder.textView.setText("测试" + position);
//    }

//    @Override
//    public int getItemCount() {
//        return 50;
//    }



    public class ViewHolder extends AbsHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_country);
        }

        TextView textView;
    }
}
