package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.config.Constants;
import com.code.travellog.core.data.repository.ArticleRepository;
import com.mvvm.base.AbsViewModel;

/**
 * @author：tqzhang on 18/7/26 16:15
 */
public class ArticleViewModel extends AbsViewModel<ArticleRepository> {

    public ArticleViewModel(@NonNull Application application) {
        super(application);
    }

    public void getArticleList(String lectureLevel1, String lastId) {
        mRepository.loadArticleRemList(lectureLevel1, lastId, Constants.PAGE_RN);
    }

    public void getArticleTypeData() {
        mRepository.loadArticleType();
    }


}
