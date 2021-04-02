package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.core.data.repository.QaRepository;
import com.mvvm.base.AbsViewModel;

/**
 * @author：tqzhang  on 18/8/2 10:53
 */
public class QaViewModel extends AbsViewModel<QaRepository> {


    public QaViewModel(@NonNull Application application) {
        super(application);
    }

    public void getQAList(String lastId) {
        mRepository.loadQAList(lastId);
    }
}
