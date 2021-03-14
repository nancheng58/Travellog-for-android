package com.code.travellog.core.vm;

import android.app.Application;

import androidx.annotation.NonNull;

import com.code.travellog.core.data.source.UserRepository;
import com.mvvm.base.AbsViewModel;

import java.util.HashMap;

/**
 * @description:
 * @date: 2021/3/14
 */
public class UserViewModel  extends AbsViewModel<UserRepository> {
    public UserViewModel(@NonNull Application application) {
        super(application);
    }
    public void postLogin(String user,String password ,String captcha) {
        mRepository.login(user,password,captcha);
    }
    public void getCaptcha(String type) {
        mRepository.getCaptcha(type);
    }
    public void postRegister(HashMap<String,String> parms){
        mRepository.postRegister(parms);
    }
}
