package com.code.travellog.core.view.user;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.code.travellog.core.data.pojo.image.ImagePojo;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.core.data.repository.UserRepository;
import com.code.travellog.core.viewmodel.UserViewModel;
import com.code.travellog.util.Base64Utils;
import com.code.travellog.util.BitmapUtil;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import androidx.cardview.widget.CardView;

import com.code.travellog.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mvvm.base.AbsLifecycleActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AbsLifecycleActivity<UserViewModel> implements Validator.ValidationListener {
    @Order(1)
    @NotEmpty(message = "用户名不能为空")
    @Length(min=6,max=12,message = "用户名6~12位之间，且不能全为数字")
    @BindView(R.id.et_rusername)
    EditText userName;
    @Order(2)
    @NotEmpty(message = "密码不能为空")
    @Password(min=6,message = "密码最少为6位")
    @BindView(R.id.et_rpassword)
    EditText password;
    @NotEmpty(message = "确认密码不能为空")
    @ConfirmPassword(message = "两次密码输入不一致")
    @Order(3)
    @BindView(R.id.et_repeatpassword)
    EditText password2;
    @Order(4)
    @NotEmpty(message = "邮箱不能为空")
    @Pattern(regex = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$",message = "邮箱格式不正确")
    @BindView(R.id.et_email)
    EditText email;
    @Order(5)
    @BindView(R.id.et_phone)
    EditText phone;
    @NotEmpty(message = "验证码不能为空")
    @Length(min = 4,max = 4,message = "验证码为4位")
    @BindView(R.id.et_rcaptcha) EditText captchacode;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.et_rcaptcha_avater) ImageView captcha_avater;
    @BindView(R.id.cv_add)
    CardView cvAdd;
    @BindView(R.id.btn_register) Button button;
    Validator validator ;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }
    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        ButterKnife.bind(this);
        super.initViews(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        dataObserver();
        getCaptchaAvater();
    }

    @Override
    protected void dataObserver() {
        registerSubscriber(UserRepository.ENTER_KEY_RES, UserPojo.class).observe(this, userPojo -> {
            if (userPojo.code == 200){
                ToastUtils.showToast("注册成功，请登录！");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);// 当前->目标
                startActivity(intent);
                finish();
            }
            else ToastUtils.showToast(userPojo.msg);
        });
        String regist = "register";
        registerSubscriber(UserRepository.ENTER_KEY_CAP,regist,ImagePojo.class).observe(this, imagePojo -> {
            if (imagePojo.code == 200){
                byte[] bitmapbyte = Base64Utils.decode(imagePojo.data.get(0).img);
                Bitmap bitmap =BitmapFactory.decodeByteArray(bitmapbyte,0,bitmapbyte.length);
                ImageView img =findViewById(R.id.et_rcaptcha_avater);
                bitmap = BitmapUtil.ChangeSize(bitmap,120,200);
                img.setImageBitmap(bitmap);
            }
            else ToastUtils.showToast(imagePojo.msg);
        });
    }


    @Override
    public void onValidationSucceeded() {
        Register();
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

    @OnClick(R.id.btn_register)
    public void Register()
    {
        String pwd = StringUtil.md5(password.getText().toString());// md5 加密
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("uname",userName.getText().toString());
            params.put("password",pwd);
            params.put("phone",phone.getText().toString());
            params.put("email",email.getText().toString());
            params.put("captcha",captchacode.getText().toString());
            mViewModel.postRegister(params);
    }
    @OnClick(R.id.et_rcaptcha_avater)
    public void getCaptchaAvater() {
        mViewModel.getCaptcha("register");
    }

    @OnClick(R.id.fab)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }
            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }
            @Override
            public void onTransitionCancel(Transition transition) { }
            @Override
            public void onTransitionPause(Transition transition) { }
            @Override
            public void onTransitionResume(Transition transition) { }

        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
