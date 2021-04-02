package com.code.travellog.core.view.article;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.core.data.pojo.article.ArticleVo;
import com.code.travellog.core.data.repository.ArticleRepository;
import com.code.travellog.core.vm.ArticleViewModel;
import com.code.travellog.util.AdapterPool;

/**
 * @author：tqzhang on 18/7/2 14:40
 */
public class ArticleListFragment extends BaseListFragment<ArticleViewModel> {

    private String typeId;

    public static ArticleListFragment newInstance() {
        return new ArticleListFragment();
    }

    @Override
    protected void dataObserver() {
        if (getArguments() != null) {
            typeId = getArguments().getString("type_id");
        }
        registerSubscriber(ArticleRepository.EVENT_KEY_ARTICLE_LIST, typeId, ArticleVo.class).observe(this, articleVo -> {
            if (articleVo != null) {
                lastId = articleVo.data.list.get(articleVo.data.list.size() - 1).newsid;
                setUiData(articleVo.data.list);
            }
        });
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getArticleAdapter(activity).build();
    }


    @Override
    protected void getRemoteData() {
        mViewModel.getArticleList(typeId, lastId);
    }


    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        getRemoteData();
    }
}