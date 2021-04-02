package com.code.travellog.core.view.user;

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

import com.code.travellog.App;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.image.ImagePojo;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.Base64Utils;
import com.code.travellog.util.BitmapUtil;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mvvm.base.BaseActivity;
import com.mvvm.http.HttpHelper;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @description: 重置密码
 * @date: 2021/3/5
 */
public class ResetPwdActivity extends BaseActivity implements Validator.ValidationListener {

    @Length(min=6,max=6,message = "重置验证码为6位")
    @BindView(R.id.emailcode)
    EditText emailcode;
    @BindView(R.id.userInfoText)
    TextInputLayout userInfoText;
    @NotEmpty(message = "新密码不能为空")
    @Password(min=6,message = "新密码最少为6位")
    @BindView(R.id.password)
    EditText password;
    @NotEmpty(message = "确认密码不能为空")
    @ConfirmPassword(message = "两次密码输入不一致")
    @BindView(R.id.repassword)
    EditText repassword;
    @Length(min=4,max=4,message = "验证码为4位")
    @BindView(R.id.captchacode)
    EditText captchacode;
    @BindView(R.id.et_captcha_avater)
    ImageView etCaptchaAvater;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.cv)
    CardView cv;
    protected Validator validator;

    @Override
    public int getLayoutId() {
        return R.layout.activity_resetpwd;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        getCaptchaAvater();
    }
    @SuppressLint("CheckResult")
    @OnClick(R.id.et_captcha_avater)
    public void getCaptchaAvater()
    {
        HttpHelper.getInstance().create(ApiService.class).getCaptchaAvater()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<ImagePojo>() {
                    @Override
                    public void onSuccess(ImagePojo imagePojo) {
                        byte[] bitmapbyte = Base64Utils.decode(imagePojo.data.get(0).img);
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
    @SuppressLint("CheckResult")
    public void submit()
    {
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("code",emailcode.getText().toString());
        params.put("captcha",captchacode.getText().toString());
        params.put("new_password", StringUtil.md5(password.getText().toString()));
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
                        Intent intent = new Intent(ResetPwdActivity.this, LoginActivity.class);
                        App.instance().removeAllActivity();
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        ToastUtils.showToast(msg);
                    }
                });
    }
    @OnClick(R.id.btn_submit)
    public void onClick() {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        submit();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages ;)
            if(view instanceof EditText){
                view.requestFocus();
                ((EditText) view).setError(message);
            }
            else ToastUtils.showToast(message);
        }
    }
}
