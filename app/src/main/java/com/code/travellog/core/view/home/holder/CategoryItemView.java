package com.code.travellog.core.view.home.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.home.CatagoryInfoVo;
import com.code.travellog.core.data.pojo.home.CategoryVo;
import com.code.travellog.core.view.common.CommonActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @time 2021/3/24 13:56
 */

public class CategoryItemView extends AbsItemHolder<CategoryVo, CategoryItemView.ViewHolder> {
    private String[] tvNames;
    private int[] tvIcons;
    private List<CatagoryInfoVo> list = new ArrayList<>();
    private HomeCategoryAdapter adapter;

    public CategoryItemView(Context context) {
        super(context);
        tvNames = new String[]{mContext.getResources().getString(R.string.weather_identification_name),
                mContext.getResources().getString(R.string.color_extraction),
                mContext.getResources().getString(R.string.poetry_generation),
                mContext.getResources().getString(R.string.style_transfer_name),
                mContext.getResources().getString(R.string.object_detection),
                mContext.getResources().getString(R.string.super_resolution)
        };
        tvIcons = new int[]{R.drawable.weather_icon, R.drawable.color_icon, R.drawable.poem_icon, R.drawable.style_icon, R.drawable.object_icon, R.drawable.super_icon};
        initData();
        adapter = new HomeCategoryAdapter(mContext, list, 0);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_category;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CategoryVo categoryTop) {
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setNestedScrollingEnabled(false);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener((v, position) -> {
            if (list.get(position).title.equals(mContext.getResources().getString(R.string.weather_identification_name))) {
                CommonActivity.start(mContext, Constants.WEATHER, mContext.getResources().getString(R.string.weather_identification_name));
            } else if (list.get(position).title.equals(mContext.getResources().getString(R.string.color_extraction))) {
                CommonActivity.start(mContext, Constants.COLOR, mContext.getResources().getString(R.string.color_extraction));
            } else if (list.get(position).title.equals(mContext.getResources().getString(R.string.poetry_generation))) {
                CommonActivity.start(mContext, Constants.POETRY, mContext.getResources().getString(R.string.poetry_generation));
            } else if (list.get(position).title.equals(mContext.getResources().getString(R.string.style_transfer_name))) {
                CommonActivity.start(mContext, Constants.STYLE, mContext.getResources().getString(R.string.style_transfer_name));
            } else if (list.get(position).title.equals(mContext.getResources().getString(R.string.image_supervision))) {
                CommonActivity.start(mContext, Constants.SUPERVISION, mContext.getResources().getString(R.string.image_supervision));
            } else if (list.get(position).title.equals(mContext.getResources().getString(R.string.object_detection))) {
                CommonActivity.start(mContext, Constants.OBJECT, mContext.getResources().getString(R.string.object_detection));
            } else if (list.get(position).title.equals(mContext.getResources().getString(R.string.super_resolution))) {
                CommonActivity.start(mContext, Constants.SUPER, mContext.getResources().getString(R.string.super_resolution));
            } else if (list.get(position).title.equals(mContext.getResources().getString(R.string.activity_title_name))) {
                CommonActivity.start(mContext, Constants.ACTIVITY, mContext.getResources().getString(R.string.activity_title_name));
            }
        });
    }

    @Override
    protected void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        RecyclerView.LayoutParams clp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (clp instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) clp).setFullSpan(true);
        }
    }

    public static class ViewHolder extends AbsHolder {

        private RecyclerView recyclerView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = getViewById(R.id.recycler_view);
        }
    }

    private void initData() {
        list.clear();
        for (int i = 0; i < tvNames.length; i++) {
            list.add(new CatagoryInfoVo(tvNames[i], tvIcons[i]));
        }
    }

}
