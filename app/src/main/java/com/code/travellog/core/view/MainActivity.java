package com.code.travellog.core.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.code.travellog.App;
import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.code.travellog.core.view.correct.WorkFragment;
import com.code.travellog.core.view.video.VideoFragment;
import com.code.travellog.core.view.home.HomeFragment;
import com.code.travellog.core.view.mine.MineFragment;
import com.code.travellog.util.ToastUtils;
import com.mvvm.base.BaseActivity;


public class MainActivity extends BaseActivity {

    private HomeFragment mHomeFragment;

    private WorkFragment mWorkFragment;

    private VideoFragment mVideoFragment;

    private MineFragment mMineFragment;

    //定义一个变量，来标识是否退出
    private static int isExit=0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
        //init Bottom tabBar
        initNavBar();
        //init fragment
        initFragment(0);
        App.instance().addActivity(this);
    }

    /**
     * @description 初始化底边栏
     * @param
     * @return
     * @time 2021/2/22 11:04
     */
    private void initNavBar() {
        BottomNavigationBar mBottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.tab_home_icon, R.string.home_title_name).setInactiveIconResource(R.mipmap.tab_home_icon_def))
                .addItem(new BottomNavigationItem(R.mipmap.tab_works_icon, R.string.work_title_name).setInactiveIconResource(R.mipmap.tab_works_icon_def))
                .addItem(new BottomNavigationItem(R.mipmap.tab_course_icon, R.string.video_title_name).setInactiveIconResource(R.mipmap.tab_course_icon_def))
                .addItem(new BottomNavigationItem(R.mipmap.tab_mine_icon, R.string.mine_title_name).setInactiveIconResource(R.mipmap.tab_mine_icon_def))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                initFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void initFragment(int i) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction(); //将之前的页面隐藏
        hideFragment(fragmentTransaction);
        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                    fragmentTransaction.add(R.id.home_content, mHomeFragment, Constants.HOME_TAG);
                } else {
                    fragmentTransaction.show(mHomeFragment);
                }
                break;

            case 1:
                if (mWorkFragment == null) {
                    mWorkFragment = WorkFragment.newInstance();
                    fragmentTransaction.add(R.id.home_content, mWorkFragment, Constants.WORK_TAG);
                } else {
                    fragmentTransaction.show(mWorkFragment);
                }
                break;
            case 2:
                if (mVideoFragment == null) {
                    mVideoFragment = VideoFragment.newInstance();
                    fragmentTransaction.add(R.id.home_content, mVideoFragment, Constants.VIDEO_TAG);
                } else {
                    fragmentTransaction.show(mVideoFragment);
                }
                break;
            case 3:
                if (mMineFragment == null) {
                    mMineFragment = MineFragment.newInstance();
                    fragmentTransaction.add(R.id.home_content, mMineFragment, Constants.MINE_TAG);
                } else {
                    fragmentTransaction.show(mMineFragment);
                }
                break;
            default:
                break;
        }
        fragmentTransaction.commit();

    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mHomeFragment != null) {
            fragmentTransaction.hide(mHomeFragment);
        }

        if (mWorkFragment != null) {
            fragmentTransaction.hide(mWorkFragment);
        }
        if (mVideoFragment != null) {
            fragmentTransaction.hide(mVideoFragment);
        }

        if (mMineFragment != null) {
            fragmentTransaction.hide(mMineFragment);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(Fragment fragment : fragmentManager.getFragments())
        {
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    //实现按两次后退才退出
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            isExit--;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            isExit++;
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exit(){
        if(isExit<2){
            ToastUtils.showToast("再按一次退出");

            //利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0,2000);

        }else{
//            super.onBackPressed();
            App.mInstance.exitApp();
        }
    }
}
