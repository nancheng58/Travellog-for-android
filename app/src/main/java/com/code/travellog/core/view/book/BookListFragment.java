package com.code.travellog.core.view.book;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.base.BaseListFragment;
import com.code.travellog.core.data.pojo.book.BookListVo;
import com.code.travellog.core.data.source.BookRepository;
import com.code.travellog.core.vm.BookViewModel;
import com.code.travellog.util.AdapterPool;

/**
 * @author：tqzhang on 18/7/2 14:40
 */
public class BookListFragment extends BaseListFragment<BookViewModel> {
    private String typeId;

    public static BookListFragment newInstance() {
        return new BookListFragment();
    }


    @Override
    protected void dataObserver() {
        if (getArguments() != null) {
            typeId = getArguments().getString("type_id");
        }

       registerSubscriber(BookRepository.EVENT_KEY_BOOK_LIST,typeId, BookListVo.class).observe(this, bookListVo -> {
            if (bookListVo == null) {
                return;
            }
            if (bookListVo.data.content.size() > 0) {
                lastId = bookListVo.data.content.get(bookListVo.data.content.size() - 1).bookid;
                setUiData(bookListVo.data.content);
            }
        });
    }


    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getBookAdapter(activity).build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getBookList(typeId, lastId);
    }


    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        getRemoteData();
    }
}
