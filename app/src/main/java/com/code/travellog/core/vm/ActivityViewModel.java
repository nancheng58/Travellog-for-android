package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.config.Constants;
import com.code.travellog.core.data.source.ActivityRepository;
import com.mvvm.base.AbsViewModel;

/**
 * @author：tqzhang on 18/7/27 15:23
 */
public class ActivityViewModel extends AbsViewModel<ActivityRepository> {

    public ActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void getActivityList(String id) {
        mRepository.loadActivityList(id, Constants.PAGE_RN);
    }
}
