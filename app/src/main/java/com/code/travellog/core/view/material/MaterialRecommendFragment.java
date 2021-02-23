package com.code.travellog.core.view.material;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adapter.adapter.DelegateAdapter;
import com.code.travellog.ui.base.BaseListFragment;
import com.code.travellog.core.data.pojo.material.MaterialRecommendVo;
import com.code.travellog.core.data.source.MaterialRepository;
import com.code.travellog.core.vm.MaterialViewModel;
import com.code.travellog.util.AdapterPool;


/**
 * @author：tqzhang on 18/7/2 14:39
 */
public class MaterialRecommendFragment extends BaseListFragment<MaterialViewModel> {

    public static MaterialRecommendFragment newInstance() {
        return new MaterialRecommendFragment();
    }

    @Override
    protected void dataObserver() {
        registerSubscriber(MaterialRepository.EVENT_KEY_MT_RED, MaterialRecommendVo.class).observe(this, materialRecommendVo -> {
            if (materialRecommendVo != null) {
                lastId = materialRecommendVo.data.content.get(materialRecommendVo.data.content.size() - 1).subjectid;
                setUiData(materialRecommendVo.data.content);
            }

        });
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getMaterialRemAdapter(activity).build();
    }

    @Override
    protected void getRemoteData() {
        mViewModel.getMaterialRemList("0", lastId);
    }

    @Override
    public void onLoadMore(boolean isLoadMore, int pageIndex) {
        super.onLoadMore(isLoadMore, pageIndex);
        getRemoteData();
    }
}
