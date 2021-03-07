package com.code.travellog.core.view.album;

import com.code.travellog.core.vm.AlbumViewModel;
import com.mvvm.base.AbsLifecycleFragment;

/**
 * @description:
 * @date: 2021/3/7
 */
public class AlbumResultFragment extends AbsLifecycleFragment<AlbumViewModel> {
    public static AlbumResultFragment newInstance(){
        return new AlbumResultFragment();
    }
    @Override
    public int getLayoutResId() {
        return 0;
    }
}
