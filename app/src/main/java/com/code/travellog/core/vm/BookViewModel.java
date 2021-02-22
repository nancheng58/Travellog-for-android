package com.code.travellog.core.vm;

import android.app.Application;
import androidx.annotation.NonNull;

import com.code.travellog.config.Constants;
import com.code.travellog.core.data.source.BookRepository;
import com.mvvm.base.AbsViewModel;


/**
 * @author：tqzhang on 18/7/28 13:32
 */
public class BookViewModel extends AbsViewModel<BookRepository> {

    public BookViewModel(@NonNull Application application) {
        super(application);
    }

    public void getBookList(String mCatalogId, String lastId) {
        mRepository.loadBookList(mCatalogId, lastId, Constants.PAGE_RN);
    }

    public void getBookTypeData() {
        mRepository.loadBookTypeData();
    }

}
