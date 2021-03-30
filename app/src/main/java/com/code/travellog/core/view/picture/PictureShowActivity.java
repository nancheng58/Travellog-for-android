package com.code.travellog.core.view.picture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.code.travellog.R;
import com.code.travellog.core.data.pojo.geo.CityPojo;
import com.gyf.immersionbar.ImmersionBar;
import com.ibbhub.album.AlbumFragment;
import com.mvvm.base.BaseActivity;

import java.util.List;

public class PictureShowActivity extends AppCompatActivity {

    public static List<String> file = null ;
    public static CityPojo cityPojo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_album);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(cityPojo.county);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true) //解决软键盘与底部输入框冲突问题，默认为 false，还有一个重载方法，可以指定软键盘 mode
                .keyboardMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                ).init(); //单独指定软键盘模式
        initView();

    }

//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_album;
//    }
//
//    @Override
//    public void initViews(Bundle savedInstanceState) {
//
//    }

    private MenuItem chooseMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        chooseMenu = menu.findItem(R.id.action_choose);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_choose) {
            if (item.getTitle().equals("选择")) {
                albumFragment.enterChoose();
            } else {
                albumFragment.cancelChoose();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private AlbumFragment albumFragment;
    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        albumFragment = (AlbumFragment) getSupportFragmentManager().findFragmentByTag("album");
        if (albumFragment == null) {
            albumFragment = new MyAlbumFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flParent, albumFragment);
        ft.commit();
    }

    public void onChooseModeChange(boolean isChoose) {
        chooseMenu.setTitle(isChoose ? "取消" : "选择");
    }

    public static void start(Context context, CityPojo cityPojo1) {
        cityPojo = cityPojo1 ;
        file = cityPojo.path ;
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("picPath", (Parcelable) files);
        Intent starter = new Intent(context, PictureShowActivity.class);

        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
