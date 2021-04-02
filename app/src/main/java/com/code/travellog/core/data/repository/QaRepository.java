package com.code.travellog.core.data.repository;

import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.qa.QaListVo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

/**
 * @author：tqzhang on 18/8/2 10:52
 */
public class QaRepository extends BaseRepository {

    public static String EVENT_KEY_QA = null;

    public QaRepository() {
        if (EVENT_KEY_QA == null) {
            EVENT_KEY_QA = StringUtil.getEventKey();
        }
    }

    public void loadQAList(String lastId) {
        addDisposable(apiService.getQAList(lastId, Constants.PAGE_RN)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<QaListVo>() {
                    @Override
                    protected void onNoNetWork() {
                        postState(StateConstants.NET_WORK_STATE);
                    }

                    @Override
                    public void onSuccess(QaListVo qaListVo) {
                        postData(EVENT_KEY_QA, qaListVo);
                        postState(StateConstants.SUCCESS_STATE);

                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);

                    }
                }));
    }
}
