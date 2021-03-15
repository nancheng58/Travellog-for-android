package com.code.travellog.core.data.source;

import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.core.data.pojo.extraction.ColorPojo;
import com.code.travellog.core.data.pojo.poetry.PoetryPojo;
import com.code.travellog.core.data.pojo.weather.WeatherPojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;

import okhttp3.MultipartBody;

/**
 * @description: 各种不便分类接口的数据仓库
 * @date: 2021/3/15
 */
public class ApiRepository extends BaseRepository {
    public static String ENTER_KEY_WRATHER = null;
    public static String ENTER_KEY_COLOR = null;
    public static String ENTER_KEY_POETRY = null;
    public ApiRepository(){
        if(ENTER_KEY_WRATHER == null) ENTER_KEY_WRATHER = StringUtil.getEventKey();
        if(ENTER_KEY_COLOR == null) ENTER_KEY_COLOR = StringUtil.getEventKey();
        if(ENTER_KEY_POETRY == null) ENTER_KEY_POETRY = StringUtil.getEventKey();
    }
    public void loadWeatherResult(MultipartBody multipartBody)
    {
        addDisposable(apiService.getWeather(multipartBody)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<WeatherPojo>() {
                    @Override
                    public void onSuccess(WeatherPojo weatherPojo) {
                        postData(ENTER_KEY_WRATHER,weatherPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }

    public void loadColorResult(MultipartBody multipartBody){
        addDisposable(apiService.getColor(multipartBody)
        .compose(RxSchedulers.io_main())
        .subscribeWith(new RxSubscriber<ColorPojo>() {
            @Override
            public void onSuccess(ColorPojo colorPojo) {
                postData(ENTER_KEY_COLOR,colorPojo);
                postState(StateConstants.SUCCESS_STATE);

            }

            @Override
            public void onFailure(String msg, int code) {
                postState(StateConstants.ERROR_STATE);

            }
        }));
    }
    public void loadPoetry(String keyword ,int length ,int experience ,int history){
        addDisposable(apiService.getPoetry(keyword,length,experience,history)
        .compose(RxSchedulers.io_main())
        .subscribeWith(new RxSubscriber<PoetryPojo>() {
            @Override
            public void onSuccess(PoetryPojo poetryPojo) {
                postData(ENTER_KEY_POETRY,poetryPojo);
                postState(StateConstants.SUCCESS_STATE);

            }

            @Override
            public void onFailure(String msg, int code) {
                postState(StateConstants.ERROR_STATE);

            }
        }));
    }
}
