package com.code.travellog.core.data.repository;

import com.code.travellog.network.ApiService;
import com.mvvm.base.AbsRepository;
import com.mvvm.event.LiveBus;
import com.mvvm.http.HttpHelper;


/**
 * @description
 * @time 2021/3/12 22:36
 */

public class BaseRepository extends AbsRepository {

    protected ApiService apiService;


    public BaseRepository() {
        if (null == apiService) {
            apiService = HttpHelper.getInstance().create(ApiService.class);
        }
    }


    protected void postData(Object eventKey, Object t) {
        postData(eventKey, null, t);
    }


    protected void showPageState(Object eventKey, String state) {
        postData(eventKey, state);
    }

    protected void showPageState(Object eventKey, String tag, String state) {
        postData(eventKey, tag, state);
    }

    protected void postData(Object eventKey, String tag, Object t) {
        LiveBus.getDefault().postEvent(eventKey, tag, t);
    }

}
