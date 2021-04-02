package com.mvvm.base;



import androidx.lifecycle.MutableLiveData;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @description 数据仓库基类
 * @time 2021/1/23 7:34
 */

public abstract class AbsRepository {

    private CompositeDisposable mCompositeDisposable;

    public MutableLiveData<String> loadState;


    public AbsRepository() {
        loadState = new MutableLiveData<>();
    }

    protected void postState(String state) {
        if (loadState != null) {
            loadState.postValue(state);
        }

    }


    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
