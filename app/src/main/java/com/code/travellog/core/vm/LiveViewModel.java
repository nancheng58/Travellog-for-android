package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.config.Constants;
import com.code.travellog.core.data.repository.LiveRepository;
import com.mvvm.base.AbsViewModel;

/**
 * @author：tqzhang on 18/7/27 15:23
 */
public class LiveViewModel extends AbsViewModel<LiveRepository> {

    public LiveViewModel(@NonNull Application application) {
        super(application);
    }


    public void getLiveList(String fCatalogId, String id) {
        mRepository.loadLiveList(fCatalogId, id, Constants.PAGE_RN);

    }

    public void getLiveRemList(String id) {
        mRepository.loadLiveRemList(id, Constants.PAGE_RN);

    }

    public void getLiveTypeData() {
        mRepository.loadLiveTypeData();
    }
}
