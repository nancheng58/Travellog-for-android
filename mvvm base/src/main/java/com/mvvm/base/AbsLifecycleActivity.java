package com.mvvm.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.mvvm.event.LiveBus;
import com.mvvm.stateview.ErrorState;
import com.mvvm.stateview.LoadingState;
import com.mvvm.stateview.StateConstants;
import com.mvvm.util.TUtil;
import com.tqzhang.stateview.stateview.BaseStateControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：tqzhang on 18/8/10 11:40
 */
public abstract class AbsLifecycleActivity<T extends AbsViewModel> extends BaseActivity {

    protected T mViewModel;
    private List<Object> eventKeys = new ArrayList<>();
    public AbsLifecycleActivity() {

    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mViewModel = VMProviders(this, (Class<T>) TUtil.getInstance(this, 0));
        if(null != mViewModel){
            dataObserver();
            mViewModel.mRepository.loadState.observe(this,observer);
        }

    }


    protected <T extends ViewModel> T VMProviders(AppCompatActivity fragment, @NonNull Class modelClass) {
        return (T) ViewModelProviders.of(fragment).get(modelClass);

    }

    protected void dataObserver() {

    }

    protected <T> MutableLiveData<T> registerSubscriber(Object eventKey, Class<T> tClass) {

        return registerSubscriber(eventKey, null, tClass);
    }

    protected <T> MutableLiveData<T> registerSubscriber(Object eventKey, String tag, Class<T> tClass) {
        String event;
        if (TextUtils.isEmpty(tag)) {
            event = (String) eventKey;
        } else {
            event = eventKey + tag;
        }
        eventKeys.add(event);
        return LiveBus.getDefault().subscribe(eventKey, tag, tClass);
    }
    @Override
    protected void onStateRefresh() {
        showLoading();
    }

    protected void showError(Class<? extends BaseStateControl> stateView, Object tag) {
        loadManager.showStateView(stateView, tag);
    }

    protected void showError(Class<? extends BaseStateControl> stateView) {
        showError(stateView, null);
    }

    protected void showSuccess() {
        loadManager.showSuccess();
    }

    protected void showLoading() {
        loadManager.showStateView(LoadingState.class);
    }


    protected Observer observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String state) {
            if (!TextUtils.isEmpty(state)) {
                if (StateConstants.ERROR_STATE.equals(state)) {
                    showError(ErrorState.class, "2");
                } else if (StateConstants.NET_WORK_STATE.equals(state)) {
                    showError(ErrorState.class, "1");
                } else if (StateConstants.LOADING_STATE.equals(state)) {
                    showLoading();
                } else if (StateConstants.SUCCESS_STATE.equals(state)) {
                    showSuccess();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearEvent();
    }
    private void clearEvent() {
        if (eventKeys != null && eventKeys.size() > 0) {
            for (int i = 0; i < eventKeys.size(); i++) {
                LiveBus.getDefault().clear(eventKeys.get(i));
            }
        }
    }
}
