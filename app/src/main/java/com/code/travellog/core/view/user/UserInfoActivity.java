package com.code.travellog.core.view.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.chaychan.viewlib.PowerfulEditText;
import com.code.travellog.App;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.core.data.repository.UserRepository;
import com.code.travellog.core.viewmodel.UserViewModel;
import com.code.travellog.util.StringUtil;
import com.code.travellog.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mvvm.base.AbsLifecycleActivity;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @description: 用户信息 界面
 * @date: 2021/2/23
 */
public class UserInfoActivity extends AbsLifecycleActivity<UserViewModel> implements Validator.ValidationListener,View.OnClickListener{

    protected String gender="0";
    protected Validator validator;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.rl_title_bar)
    RelativeLayout mTitleBar;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.intro)
    EditText intro;
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.femle)
    RadioButton femle;
    @BindView(R.id.genderRG)
    RadioGroup genderRG;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.et_email)
    EditText email;
    @BindView(R.id.btn_userInfo)
    Button btnUserInfo;
    @BindView(R.id.tv_title2)
    SuperTextView superTextView;
    @NotEmpty(message = "密码不能为空")
    @Length(min=6,message = "密码最少为6位")
    @BindView(R.id.oldpwd)
    PowerfulEditText oldpwd;
    @NotEmpty(message = "新密码不能为空")
    @Password(min=6,message = "新密码最少为6位")
    @BindView(R.id.password)
    PowerfulEditText password;
    @NotEmpty(message = "确认密码不能为空")
    @ConfirmPassword(message = "两次密码输入不一致")
    @BindView(R.id.repassword)
    PowerfulEditText repassword;
    @BindView(R.id.btn_changePwd)
    Button btnChangePwd;
    @BindView(R.id.content)
    LinearLayout content;
    protected MMKV kv ;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        App.instance().addActivity(this);
        ButterKnife.bind(this);
        super.initViews(savedInstanceState);
        kv = MMKV.defaultMMKV();
        setTitle(getResources().getString(R.string.move_title_name));
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        dataObserver();
        btnChangePwd.setOnClickListener(v -> {validator.validate();});
        superTextView.setLeftImageViewClickListener(imageView -> {
            UserInfoActivity.super.onBackPressed();
        });
        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.male:
                        gender="1";
                    break;
                    case R.id.femle:
                        gender="2";
                    default:break;
                }
            }
        });
    }

    @Override
    protected void dataObserver() {
        registerSubscriber(UserRepository.ENTER_KEY_USERINFO,UserPojo.class).observe(this,user -> {
            if(user.code!=200) {
                ToastUtils.showToast(user.msg);
            }
            ToastUtils.showToast("修改成功！");
            kv.encode("uid",user.data.uid);
            kv.encode("userName",user.data.uname);
            kv.encode("phone",user.data.phone);
            kv.encode("email",user.data.email);
            kv.encode("gender",user.data.gender);
            kv.encode("avatar",user.data.avatar);
            kv.encode("intro",user.data.intro);
        });
        registerSubscriber(UserRepository.ENTER_KEY_RERWD,BasePojo.class).observe(this,basePojo -> {
            if(basePojo.code != 200) ToastUtils.showToast(basePojo.msg);
            else{
                ToastUtils.showToast("修改成功！");
            }
        });
    }

    /**
     * set title
     *
     * @param titleName
     */
    protected void setTitle(String titleName) {
        mTitleBar.setVisibility(View.VISIBLE);
        mTitle.setText(titleName);
    }

    @SuppressLint("CheckResult")
    public void PostUserInfo()
    {
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("gender",gender);
        params.put("phone",phone.getText().toString());
        params.put("email",email.getText().toString());
        params.put("intro",intro.getText().toString());
        mViewModel.postUserInfo(params);
    }

    @SuppressLint("CheckResult")
    public void changePwd()
    {
        String pwd = StringUtil.md5(oldpwd.getText().toString());
        String newpwd = password.getText().toString();
        String newrepwd = repassword.getText().toString();
        if ((!newpwd.equals(newrepwd))){
            ToastUtils.showToast("两次密码输入不一致，请重新输入");
            return;
        }
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("old_password",pwd);
        params.put("new_password",StringUtil.md5(newpwd));
        mViewModel.postRePwd(params);
    }
    @OnClick({R.id.btn_logout,R.id.btn_userInfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                new AlertDialog.Builder(this).setTitle("确认退出吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                App.instance().removeAllActivity();
                                kv.encode("isLogin",false);

                                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();
                break;
            case R.id.btn_userInfo:
                PostUserInfo();
                break;
            case R.id.iv_back:finish();
            default:break;
        }
    }
    @Override
    public void onValidationSucceeded() {
        changePwd();
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
