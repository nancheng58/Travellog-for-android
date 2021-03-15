package com.mvvm.base;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.mvvm.util.TUtil;


/**
 * @description ViewModel基类
 * @time 2021/3/15 7:33
 */

public class AbsViewModel<T extends AbsRepository> extends AndroidViewModel {


    public T mRepository;

    public AbsViewModel(@NonNull Application application) {
        super(application);
        mRepository = TUtil.getNewInstance(this, 0);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRepository != null) {
            mRepository.unDisposable();
        }
    }

}
