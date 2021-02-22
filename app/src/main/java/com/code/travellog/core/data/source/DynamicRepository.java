package com.code.travellog.core.data.source;

import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.core.data.pojo.dynamic.DynamicListVo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

/**
 * @author：tqzhang on 18/8/13 15:21
 */
public class DynamicRepository extends BaseRepository {

    public static String EVENT_KEY_DYNAMIC = null;

    public DynamicRepository() {
        if (EVENT_KEY_DYNAMIC == null) {
            EVENT_KEY_DYNAMIC = StringUtil.getEventKey();
        }

    }

    public void loadDynamicList(String rn, String token, String lastId) {
        addDisposable(apiService.getDynamicList(rn, token, lastId)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<DynamicListVo>() {
                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }

                    @Override
                    public void onSuccess(DynamicListVo dynamicListVo) {
                        if (dynamicListVo.data != null) {
                            postData(EVENT_KEY_DYNAMIC, dynamicListVo);
                            postState(StateConstants.SUCCESS_STATE);
                        }
                    }

                    @Override
                    public void onFailure(String msg,int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));

    }
}
