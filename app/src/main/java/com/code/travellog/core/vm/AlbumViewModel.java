package com.code.travellog.core.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.code.travellog.core.data.source.AlbumRepository;
import com.code.travellog.ui.base.BaseViewHolder;
import com.mvvm.base.AbsViewModel;

/**
 * @description:
 * @date: 2021/3/7
 */
public class AlbumViewModel extends AbsViewModel<AlbumRepository> {
    public AlbumViewModel(@NonNull Application application) {
        super(application);
    }

    public void getAlbumResult(int workid){
        mRepository.loadAlbumResult(workid);
    }
}
