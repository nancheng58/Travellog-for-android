package com.code.travellog.core.view.home.holder;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.ui.adapter.BaseRecyclerAdapter;
import com.code.travellog.ui.base.BaseViewHolder;
import com.code.travellog.core.data.pojo.home.CatagoryInfoVo;

import java.util.List;


public class HomeCategoryAdapter extends BaseRecyclerAdapter<CatagoryInfoVo> {


    public HomeCategoryAdapter(Context context, @Nullable List<CatagoryInfoVo> list, int itemLayoutId) {
        super(context, list, R.layout.item_classify);
    }

    @Override
    protected void convert(BaseViewHolder holder, final CatagoryInfoVo catagoryInfoVo, int position, List payloads) {
        ImageView categroyIcon = holder.getView(R.id.iv_classify);
        TextView categroyName = holder.getView(R.id.tv_classify);
        Glide.with(getContext())
                .load(catagoryInfoVo.resId)
                .into(categroyIcon);
        categroyName.setText(catagoryInfoVo.title);

    }
}
