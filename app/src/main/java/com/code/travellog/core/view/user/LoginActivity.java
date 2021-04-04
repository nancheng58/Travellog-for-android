package com.code.travellog.core.view.user;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.travellog.App;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.image.ImagePojo;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.core.data.repository.UserRepository;
import com.code.travellog.core.vm.UserViewModel;
import com.code.travellog.core.view.MainActivity;
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
import com.mvvm.base.AbsLifecycleActivity;
import com.tencent.mmkv.MMKV;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AbsLifecycleActivity<UserViewModel> implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(message = "用户名不能为空")
    @Length(min=3,max=20,message = "输入必须在3~20位之间")
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
    @BindView(R.id.tv_forgetpwd) TextView forgetpwd;

    public UserViewModel userViewModel ;
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        App.instance().addActivity(this);
        loadManager.showSuccess();
        super.initViews(savedInstanceState);
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
        forgetpwd.setOnClickListener(v -> {
            Intent intent = new Intent(this, ForgetPwdActivity.class);
            startActivity(intent);
        });
        dataObserver();
        getCaptchaAvater();
    }
    @Override
    protected void dataObserver() {
        registerSubscriber(UserRepository.ENTER_KEY_LOGIN,UserPojo.class).observe(this,userPojo -> {
            Log.w("Tagafa",userPojo.toString());
            if(userPojo.code!=200) {ToastUtils.showToast(userPojo.msg);getCaptchaAvater();}
            else{
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
                loginto();
            }
        });
        String login = "login";
        registerSubscriber(UserRepository.ENTER_KEY_CAP,login,ImagePojo.class).observe(this,imagePojo -> {
            if(imagePojo.code == 200){
                byte[] bitmapbyte = Base64Utils.decode(imagePojo.data.get(0).img);
                Bitmap bitmap =BitmapFactory.decodeByteArray(bitmapbyte,0,bitmapbyte.length);
                ImageView img =findViewById(R.id.et_captcha_avater);
                bitmap = BitmapUtil.ChangeSize(bitmap,120,200);
                img.setImageBitmap(bitmap);
            }
            else ToastUtils.showToast(imagePojo.msg);
        });
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
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent,options.toBundle());
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.et_captcha_avater)
    public void getCaptchaAvater() {
        mViewModel.getCaptcha("login");
    }
    @SuppressLint("CheckResult")
    @OnClick(R.id.btn_login)
    public void Login() {
        String username = userName.getText().toString();
        String pwd = StringUtil.md5(password.getText().toString());// md5 加密
        String captcha = captchacode.getText().toString();
        mViewModel.postLogin(username,pwd,captcha);
    }
    public void loginto()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);// 当前->目标
        startActivity(intent);
//        onDestroy();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.instance().removeActivity(this);
    }
}

