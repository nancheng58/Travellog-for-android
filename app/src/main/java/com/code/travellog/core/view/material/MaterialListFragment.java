package com.code.travellog.core.view.material;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.material.MaterialVo;
import com.code.travellog.core.data.repository.MaterialRepository;
import com.code.travellog.core.vm.MaterialViewModel;
import com.code.travellog.util.AdapterPool;

/**
 * @author：tqzhang on 18/7/2 14:40
 */
public class MaterialListFragment extends BaseListFragment<MaterialViewModel> {
    private String subId;

    public static MaterialListFragment newInstance() {
        return new MaterialListFragment();
    }


    @Override
    protected void dataObserver() {
        if (getArguments() != null) {
            subId = getArguments().getString(Constants.SUB_ID);
        }
        registerSubscriber(MaterialRepository.EVENT_KEY_MT_LIST,subId, MaterialVo.class).observe(this, materialListVo -> {
            if (materialListVo != null) {
                lastId = materialListVo.data.content.get(materialListVo.data.content.size() - 1).tid;
                mItems.clear();
                mItems.addAll(materialListVo.data.content);
                setData();
            }
        });
        registerSubscriber(MaterialRepository.EVENT_KEY_MT_MORE_LIST,subId, MaterialVo.class).observe(this, materialListVo -> {
            if (materialListVo != null && materialListVo.data != null && materialListVo.data.content.size() > 0) {
                lastId = materialListVo.data.content.get(materialListVo.data.content.size() - 1).tid;
                mItems.addAll(materialListVo.data.content);
                setMoreData();
            }
        });
    }


    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getMaterialListAdapter(activity).build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getMaterialList("0", subId);
    }


    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        mViewModel.getMaterialMoreList("0", subId, lastId);
    }
}
