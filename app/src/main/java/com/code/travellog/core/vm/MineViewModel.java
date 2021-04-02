package com.code.travellog.core.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.code.travellog.core.data.repository.MineRepository;
import com.mvvm.base.AbsViewModel;

/**
 * @description: 我的页面 VM
 * @date: 2021/2/22
 */
public class MineViewModel extends AbsViewModel<MineRepository> {

    public MineViewModel(@NonNull Application application) {
        super(application);
    }
}