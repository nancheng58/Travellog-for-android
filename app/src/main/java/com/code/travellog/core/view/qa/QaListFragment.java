package com.code.travellog.core.view.qa;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.core.data.pojo.qa.QaListVo;
import com.code.travellog.core.data.source.QaRepository;
import com.code.travellog.core.vm.QaViewModel;
import com.code.travellog.util.AdapterPool;

/**
 * @author：tqzhang on 18/7/4 14:10
 */
public class QaListFragment extends BaseListFragment<QaViewModel> {

    public static QaListFragment newInstance() {
        return new QaListFragment();
    }


    @Override
    protected void dataObserver() {
        registerSubscriber(QaRepository.EVENT_KEY_QA, QaListVo.class).observe(this, qaListVo -> {
            if (qaListVo == null && qaListVo.data != null && qaListVo.data.size() == 0) {
                return;
            }
            lastId = qaListVo.data.get(qaListVo.data.size() - 1).newsid;
            setUiData(qaListVo.data);
        });
    }



    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getQaAdapter(activity).build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getQAList(lastId);
    }

    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        getRemoteData();
    }
}
