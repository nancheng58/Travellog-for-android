package com.code.travellog.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.code.travellog.R;

/**
 * @description
 * @time 2021/4/6 0:21
 */

public class ViewUtils {

    public static View CreateTagView(Context context, String tagContent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_tag, null);
        TextView tvTag = view.findViewById(R.id.tv_tag);
        tvTag.setText(tagContent);
        return view;
    }

}
