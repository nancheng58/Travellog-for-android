package com.code.travellog.ui;

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

import com.chaychan.viewlib.PowerfulEditText;
import com.code.travellog.App;
import com.code.travellog.R;
import com.mvvm.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @deprecated: 用户信息 界面
 * @date: 2021/2/23
 */
public class UserInfoActivity extends BaseActivity {
    protected RelativeLayout mTitleBar;
    protected TextView mTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_title_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.intro)
    EditText intro;
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.femle)
    RadioButton femle;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_userInfo)
    Button btnUserInfo;
    @BindView(R.id.oldpwd)
    PowerfulEditText oldpwd;
    @BindView(R.id.password)
    PowerfulEditText password;
    @BindView(R.id.repassword)
    PowerfulEditText repassword;
    @BindView(R.id.btn_changePwd)
    Button btnChangePwd;
    @BindView(R.id.content)
    LinearLayout content;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        App.instance().addActivity(this);
        mTitleBar = findViewById(R.id.rl_title_bar);
        mTitle = findViewById(R.id.tv_title);
        setTitle(getResources().getString(R.string.move_title_name));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_logout, R.id.et_email, R.id.btn_userInfo, R.id.btn_changePwd})
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
                                Intent intent = new Intent(UserInfoActivity.this,LoginActivity.class);
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
                break;
            case R.id.btn_changePwd:
                break;
        }
    }
}
