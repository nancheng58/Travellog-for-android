package com.code.travellog.core.view.album.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.album.AlbumResultDescriptionPojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/13
 */
public class AlbumResultStepHolder extends AbsItemHolder<AlbumResultDescriptionPojo, AlbumResultStepHolder.ViewHolder> {

    public Context mContext;

    public AlbumResultStepHolder(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_album_result_step;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AlbumResultDescriptionPojo albumResultDescriptionPojo) {
        List<String> list = albumResultDescriptionPojo.descriptions;
//        Collections.reverse(list);
////        int status = albumResultPojo.data.status;
//        holder.stepView.ondrawIndicator();
//        view.invalidate();
//        holder.convertView.invalidate();
        holder.stepView.setStepsViewIndicatorComplectingPosition(((List<String>) list).size())//设置完成的步数
                .setStepViewTexts(list)//总步骤
                .reverseDraw(true)
                .setTextSize(12)
                .setLinePaddingProportion(0.85f)//设置indicator线与线间距的比例系数
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(mContext, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(mContext, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(mContext, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(mContext, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(mContext, R.drawable.attention))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(mContext, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(mContext, R.drawable.complted));//设置StepsViewIndicator AttentionIcon
    }

    static class ViewHolder extends AbsHolder {
        @SuppressLint("NonConstantResourceId")
        @BindView(R.id.step_view)
        VerticalStepView stepView;
        ViewHolder(@NonNull View view) {
            super(view);

            ButterKnife.bind(this, view);

        }
    }

}
