package com.code.travellog.ui;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.code.travellog.App;
import com.code.travellog.R;
import com.code.travellog.R2;
import com.code.travellog.config.Constants;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.live.LiveDetailsVo;
import com.code.travellog.core.data.pojo.picture.ImageVo;
import com.code.travellog.core.data.pojo.user.User;
import com.code.travellog.core.view.live.LiveDetailsActivity;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.Base64Utils;
import com.code.travellog.util.BitmapUtil;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mvvm.base.BaseActivity;
import com.mvvm.http.HttpHelper;
import com.mvvm.http.rx.RxSchedulers;
import com.tencent.mmkv.MMKV;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends BaseActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(message = "用户名不能为空")
    @Length(min=3,max=12,message = "用户名3~12位之间，且不能全为数字")
    @Pattern(regex = "^\\w+$",message = "仅可输入数字 字母 下划线")
    @BindView(R.id.userName)
    EditText userName;
    @NotEmpty(message = "密码不能为空")
    @Password(min=6,message = "密码最少为6位")
    @Order(2)
    @BindView(R.id.password)
    EditText password;
    @NotEmpty(message = "验证码不能为空")
    @Length(min = 4,max = 4,message = "验证码为4位")
    @Order(3)

    @BindView(R.id.captchacode)
    EditText captchacode;
//    @Order(7)
//    @Checked(message = "必须接受声明条款")
//    @BindView(R.id.checkbox)
//    CheckBox checkbox;
    Validator validator;
    @BindView(R.id.btn_login) Button mloginButton;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.et_captcha_avater) ImageView captcha_avater;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        App.instance().addActivity(this);
        loadManager.showSuccess();
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        mloginButton = findViewById(R.id.btn_login);
        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        GetCaptchaAvater();
    }

    @Override
    public void onValidationSucceeded() {
        Login();
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

    @OnClick(R.id.fab)
    public void to_register_Activity()
    {
        getWindow().setExitTransition(null);
        getWindow().setEnterTransition(null);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent,options.toBundle());
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.et_captcha_avater)
    public void GetCaptchaAvater()
    {
        HttpHelper.getInstance().create(ApiService.class).getCaptchaAvater()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<ImageVo>() {
                    @Override
                    public void onSuccess(ImageVo imageVo) {
                        byte[] bitmapbyte = Base64Utils.decode(imageVo.data.get(0).img);
                        Bitmap bitmap =BitmapFactory.decodeByteArray(bitmapbyte,0,bitmapbyte.length);
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
    @OnClick(R.id.btn_login)
    public void Login() {
        String username = userName.getText().toString();
        String pwd = StringUtil.md5(password.getText().toString());// md5 加密
        String captcha = captchacode.getText().toString();
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("user",username);
        params.put("password",pwd);
        params.put("captcha",captcha);
        HttpHelper.getInstance().create(ApiService.class).LoginApi(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if(user.code!=200) {
                            onFailure(user.msg,user.code);
                            return;
                        }
                        ToastUtils.showToast("登陆成功，欢迎进入！");
                        MMKV kv = MMKV.defaultMMKV();
                        kv.encode("uid",user.data.uid);
                        kv.encode("userName",user.data.uname);
                        kv.encode("phone",user.data.phone);
                        kv.encode("email",user.data.email);
                        kv.encode("gender",user.data.gender);
                        kv.encode("avatar", URL.IMAGE_URL+user.data.avatar);
                        kv.encode("intro",user.data.intro);
                        kv.encode("isLogin",true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);// 当前->目标
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        ToastUtils.showToast(msg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.instance().removeActivity(this);
    }
}

