package com.code.travellog.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.picture.ImageVo;
import com.code.travellog.core.data.pojo.user.User;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.Base64Utils;
import com.code.travellog.util.BitmapUtil;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.Validator;
import com.mvvm.base.BaseActivity;
import com.mvvm.http.HttpHelper;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description : 找回密码
 * @date: 2021/3/4
 */
public class ForgetPwdActivity extends BaseActivity {
    @BindView(R.id.userInfo)
    EditText userInfo;
    @BindView(R.id.userInfoText)
    TextInputLayout userInfoText;
    @BindView(R.id.captchacode)
    EditText captchacode;
    @BindView(R.id.et_captcha_avater)
    ImageView etCaptchaAvater;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.cv)
    CardView cv;
    @BindView(R.id.content)
    LinearLayout linearLayout;
    @Override
    public int getLayoutId() {
        return R.layout.activity_forgetpwd;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        ButterKnife.bind(this);
        getCaptchaAvater();
    }
    void changeUi()
    {
        userInfoText.setVisibility(View.GONE);
//        linearLayout.removeView(userInfoText);
    }
    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_submit)
    public void onClick() {
        HashMap<String,String> params = new HashMap<String, String>();
        String parm = userInfo.getText().toString();
        if (parm.contains("@")) params.put("email",parm);
        else params.put("phone",parm);
        params.put("captcha",captchacode.getText().toString());
        HttpHelper.getInstance().create(ApiService.class).resetPwd(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<BasePojo>() {
                    @Override
                    public void onSuccess(BasePojo basePojo) {
                        if(basePojo.code!=200) {
                            onFailure(basePojo.msg,basePojo.code);
                            return;
                        }
                        ToastUtils.showToast(basePojo.msg);
                        Intent intent = new Intent(ForgetPwdActivity.this , ResetPwdActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        ToastUtils.showToast(msg);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.et_captcha_avater)
    public void getCaptchaAvater()
    {
        HttpHelper.getInstance().create(ApiService.class).getCaptchaAvater()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<ImageVo>() {
                    @Override
                    public void onSuccess(ImageVo imageVo) {
                        byte[] bitmapbyte = Base64Utils.decode(imageVo.data.get(0).img);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapbyte,0,bitmapbyte.length);
                        ImageView img =findViewById(R.id.et_captcha_avater);
                        bitmap = BitmapUtil.ChangeSize(bitmap,120,200);
                        img.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        ToastUtils.showToast(msg);
                    }

                });
    }
}
