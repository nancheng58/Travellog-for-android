package com.code.travellog.ui.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @date：2020-01-13 17:58
 * @describe：长按事件
 */
public interface OnItemLongClickListener {
    void onItemLongClick(RecyclerView.ViewHolder holder, int position, View v);
}
