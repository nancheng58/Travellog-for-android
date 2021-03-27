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
////        int status = albumResultPojo.data.status;
//        ((List<String>) list).add("您已提交定单，等待系统确认");
//        ((List<String>) list).add("您的商品需要从外地调拨，我们会尽快处理，请耐心等待");
//        ((List<String>) list).add("您的订单已经进入亚洲第一仓储中心1号库准备出库");
//        ((List<String>) list).add("您的订单预计6月23日送达您的手中，618期间促销火爆，可能影响送货时间，请您谅解，我们会第一时间送到您的手中");
//        ((List<String>) list).add("您的订单已打印完毕");
//        ((List<String>) list).add("您的订单已拣货完成");
//        ((List<String>) list).add("扫描员已经扫描");
//        ((List<String>) list).add("打包成功");
//        ((List<String>) list).add("您的订单在京东【华东外单分拣中心】发货完成，准备送往京东【北京通州分拣中心】");
//        ((List<String>) list).add("您的订单在京东【北京通州分拣中心】分拣完成");
//        ((List<String>) list).add("您的订单在京东【北京通州分拣中心】发货完成，准备送往京东【北京中关村大厦站】");
//        ((List<String>) list).add("您的订单在京东【北京中关村大厦站】验货完成，正在分配配送员");
//        ((List<String>) list).add("配送员【包牙齿】已出发，联系电话【130-0000-0000】，感谢您的耐心等待，参加评价还能赢取好多礼物哦");
//        ((List<String>) list).add("感谢你在京东购物，欢迎你下次光临！");
        holder.stepView.setStepsViewIndicatorComplectingPosition(((List<String>) list).size() - 2)//设置完成的步数
                .setStepViewTexts(list)//总步骤
                .setTextSize(12)
                .setLinePaddingProportion(0.85f)//设置indicator线与线间距的比例系数
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(mContext, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(mContext, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(mContext, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(mContext, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(mContext, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(mContext, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(mContext, R.drawable.attention));//设置StepsViewIndicator AttentionIcon
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
