package com.code.travellog.core.view.base;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * @description
 * @time 2021/2/3 0:35
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;

    public View convertView;

    public BaseViewHolder(final View view) {
        super(view);
        this.views = new SparseArray<>();
        this.convertView = view;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}