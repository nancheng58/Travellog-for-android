package com.code.travellog.core.view.base.listener;

/**
 * @description 拖拽监听事件
 * @param
 * @return
 * @time 2021/3/7 15:56
 */

public interface DragListener {
    /**
     * 是否将 item拖动到删除处，根据状态改变颜色
     *
     * @param isDelete
     */
    void deleteState(boolean isDelete);

    /**
     * 是否于拖拽状态
     *
     * @param start
     */
    void dragState(boolean isStart);
}
