package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.config.Constants;
import com.code.travellog.core.data.repository.FollowDrawRepository;
import com.mvvm.base.AbsViewModel;

/**
 * @author：tqzhang on 18/7/31 16:05
 */
public class FollowDrawViewModel extends AbsViewModel<FollowDrawRepository> {


    public FollowDrawViewModel(@NonNull Application application) {
        super(application);
    }


    public void getFollowDrawTypeData() {
        mRepository.loadFollowDrawType();

    }

    public void getFollowDrawList(String maintypeid, String lastId) {
        mRepository.loadFollowDrawList(maintypeid, lastId, Constants.PAGE_RN);

    }

    public void getFollowDrawRemList(String lastId) {
        mRepository.loadFollowDrawRemList(lastId, Constants.PAGE_RN);

    }
}
