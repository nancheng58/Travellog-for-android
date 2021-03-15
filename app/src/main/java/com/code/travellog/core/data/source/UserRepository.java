package com.code.travellog.core.data.source;

import com.code.travellog.config.URL;
import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.image.ImagePojo;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description:
 * @date: 2021/3/14
 */
public class UserRepository extends BaseRepository {
    public static String ENTER_KEY_LOGIN = null;
    public static String ENTER_KEY_CAP = null;
    public static String ENTER_KEY_RES = null;
    public static String ENTER_KEY_RERWD = null;
    public static String ENTER_KEY_USERINFO = null;
    public UserRepository()
    {
        if(ENTER_KEY_LOGIN == null) ENTER_KEY_LOGIN = StringUtil.getEventKey();
        if(ENTER_KEY_RES == null) ENTER_KEY_RES = StringUtil.getEventKey();
        if(ENTER_KEY_CAP == null) ENTER_KEY_CAP = StringUtil.getEventKey();
        if(ENTER_KEY_RERWD == null) ENTER_KEY_RERWD = StringUtil.getEventKey();
        if (ENTER_KEY_USERINFO == null) ENTER_KEY_USERINFO = StringUtil.getEventKey();
    }
    public void login(String username,String password ,String captcha){
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("user",username);
        params.put("password",password);
        params.put("captcha",captcha);
        addDisposable(apiService.LoginApi(params)
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<UserPojo>(){
                    @Override
                    public void onSuccess(UserPojo userPojo) {
                        if(userPojo.code == 200) {
                            ToastUtils.showToast("登陆成功，欢迎进入！");
                            MMKV kv = MMKV.defaultMMKV();
                            kv.encode("uid", userPojo.data.uid);
                            kv.encode("userName", userPojo.data.uname);
                            kv.encode("phone", userPojo.data.phone);
                            kv.encode("email", userPojo.data.email);
                            kv.encode("gender", userPojo.data.gender);
                            kv.encode("avatar", URL.IMAGE_URL + userPojo.data.avatar);
                            kv.encode("intro", userPojo.data.intro);
                            kv.encode("isLogin", true);
                        }
                        postData(ENTER_KEY_LOGIN, userPojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }
    public void getCaptcha(String type) {
        addDisposable(apiService.getCaptchaAvater()
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<ImagePojo>() {
                    @Override
                    public void onSuccess(ImagePojo imagePojo) {
                        postData(ENTER_KEY_CAP,type, imagePojo);
                        postState(StateConstants.SUCCESS_STATE);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
    }
    public void postRegister(HashMap<String,String> parms)
    {
        addDisposable(apiService.RegisterApi(parms)
        .compose(RxSchedulers.io_main())
        .subscribeWith(new RxSubscriber<BasePojo>() {
            @Override
            public void onSuccess(BasePojo basePojo) {
                postData(ENTER_KEY_RES,basePojo);
                postState(StateConstants.SUCCESS_STATE);
            }

            @Override
            public void onFailure(String msg, int code) {
                postState(StateConstants.ERROR_STATE);
            }
        }));
    }
    public void postUserInfo(HashMap<String,String> parms){
        addDisposable(apiService.postUserInfo(parms)
        .compose(RxSchedulers.io_main())
        .subscribeWith(new RxSubscriber<UserPojo>() {
            @Override
            public void onSuccess(UserPojo userPojo) {
                postData(ENTER_KEY_USERINFO,userPojo);
                postState(StateConstants.SUCCESS_STATE);
            }

            @Override
            public void onFailure(String msg, int code) {
                postState(StateConstants.ERROR_STATE);
            }
        }));
    }
    public void postRePwd(HashMap<String,String> parms){
        addDisposable(apiService.changePwd(parms)
        .compose(RxSchedulers.io_main())
        .subscribeWith(new RxSubscriber<BasePojo>() {
            @Override
            public void onSuccess(BasePojo basePojo) {
                postData(ENTER_KEY_USERINFO,basePojo);
                postState(StateConstants.SUCCESS_STATE);
            }

            @Override
            public void onFailure(String msg, int code) {
                postState(StateConstants.ERROR_STATE);
            }
        })
        );
    }
}
