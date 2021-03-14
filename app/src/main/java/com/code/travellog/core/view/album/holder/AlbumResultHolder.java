package com.code.travellog.core.view.album.holder;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.adapter.holder.AbsHolder;
import com.adapter.holder.AbsItemHolder;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/13
 */
public class AlbumResultHolder extends AbsItemHolder<AlbumResultPojo, AlbumResultHolder.ViewHolder> {

    public Context mContext;
    public AlbumResultHolder(Context context) {
        super(context);
        mContext = context ;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_album_result;
    }

    @Override
    public ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull AlbumResultPojo albumResultPojo) {
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("接单",1);
        StepBean stepBean1 = new StepBean("打包",1);
        StepBean stepBean2 = new StepBean("出发",1);
        StepBean stepBean3 = new StepBean("送单",0);
        StepBean stepBean4 = new StepBean("完成",-1);
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        stepsBeanList.add(stepBean4);
        holder.stepView
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(android.R.color.white)//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(R.color.uncompleted_text_color)//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor( android.R.color.white)//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor( R.color.uncompleted_text_color)//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.complted,null))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.default_icon,null))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.attention,null));//设置StepsViewIndicator AttentionIcon
    }

    static
    class ViewHolder extends AbsHolder {
        @BindView(R.id.step_view)
        HorizontalStepView stepView;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
