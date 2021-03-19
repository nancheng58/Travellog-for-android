package com.code.travellog.core.view.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.glide.GlideCircleTransform;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.ui.AboutActivity;
import com.code.travellog.ui.MapActivity;
import com.code.travellog.ui.MyAlbumActivity;
import com.code.travellog.ui.UserInfoActivity;
import com.code.travellog.util.ToastUtils;
import com.leon.lib.settingview.LSettingItem;
import com.mvvm.base.BaseFragment;
import com.mvvm.http.HttpHelper;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

/**
 * @description "我的"页面
 * @time 2021/2/22 18:18
 */

public class MineFragment extends BaseFragment {

    protected RelativeLayout mTitleBar;
    protected TextView mTitle;
    protected LSettingItem mabout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avater)
    protected CircleImageView imageView;
    @BindView(R.id.userInfo)
    SuperTextView userInfo;
    @BindView(R.id.userMore)
    ImageView userMore;
    protected Context mContext;
    protected MMKV kv;
    protected Unbinder butterKnife;
    /* 图片上传 */
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.rl_title_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.tv_album)
    LSettingItem tvAlbum;
    @BindView(R.id.tv_theme)
    LSettingItem tvTheme;
    @BindView(R.id.tv_setting)
    LSettingItem tvSetting;
    @BindView(R.id.tv_update)
    LSettingItem tvUpdate;
    @BindView(R.id.tv_about)
    LSettingItem tvAbout;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.tv_test)
    LSettingItem tvTest;
    @BindView(R.id.tv_gaode)
    LSettingItem tvGaode;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View layout;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;
    private EasyImage easyImage;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    public int getContentResId() {
        return R.id.content_layout;
    }

    @Override
    public void initView(Bundle state) {
        loadManager.showSuccess();
        butterKnife = ButterKnife.bind(this, rootView);
        this.mContext = activity;
        kv = MMKV.defaultMMKV();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleBar = getViewById(R.id.rl_title_bar);
        mTitle = getViewById(R.id.tv_title);
        setTitle(getResources().getString(R.string.mine_title_name));
        easyImage = new EasyImage.Builder(mContext)
                .setChooserTitle("上传头像")
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("picCache")
                .allowMultiple(false)
                .build();
        String url = kv.decodeString("avatar");
        String username = kv.decodeString("userName");
        String intro = kv.decodeString("intro");

        userInfo.setLeftTopString(username).setLeftBottomString(intro);
        mabout = getActivity().findViewById(R.id.tv_about);
        Glide.with(mContext).load(url)
                .error(R.drawable.user_default)
                .transform(new GlideCircleTransform(mContext))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);

        imageView.setOnClickListener(v -> {
            String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (arePermissionsGranted(necessaryPermissions)) {
                easyImage.openChooser(activity);
            } else {
                requestPermissionsCompat(necessaryPermissions, CHOOSER_PERMISSIONS_REQUEST_CODE);
            }
        });
        userMore.setOnClickListener(v -> {
            Intent intent = new Intent(activity, UserInfoActivity.class);
            startActivity(intent);
        });
        tvAlbum.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(activity, MyAlbumActivity.class);
                startActivity(intent);
            }
        });
        mabout.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(activity, AboutActivity.class);
                startActivity(intent);
            }
        });
        tvGaode.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(activity, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.w("PermissionsResult:", requestCode + grantResults.toString() + permissions);
            easyImage.openChooser(activity);
        }
    }

    /**
     * startActivityForResult执行后的回调方法，接收返回的图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, activity, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(@NotNull MediaFile[] imgFiles, @NotNull MediaSource mediaSource) {
                for (MediaFile imageFile : imgFiles) {
                    Log.d("Easyimage", imageFile.getFile().toString());
                }
                saveImageToServer(imgFiles);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    @SuppressLint("CheckResult")
    private void saveImageToServer(@NotNull MediaFile[] returnedPhotos) {

        Log.w("Imagefile", returnedPhotos[0].getFile().toString());
        Glide.with(mContext).load(returnedPhotos[0].getFile())
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
        String uid = Integer.toString(MMKV.defaultMMKV().decodeInt("uid"));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), returnedPhotos[0].getFile());
        MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", uid)
                .addFormDataPart("code", "travel")
                .addFormDataPart("avatar", returnedPhotos[0].getFile().toString(), requestBody).build();
//        NetworkUtils.createPartByPathAndKey(returnedPhotos[0].getFile().getPath(), "file");
        HttpHelper.getInstance().create(ApiService.class).postAvater(multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<BasePojo>() {
                    @Override
                    public void onSuccess(BasePojo basePojo) {
                        if (basePojo.code != 200) {
                            onFailure(basePojo.msg, basePojo.code);
                            return;
                        }
                        ToastUtils.showToast("更改头像成功！");
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        ToastUtils.showToast(msg);
                    }
                });
    }

    /**
     * @param
     * @return
     * @description 权限申请
     * @time 2021/3/6 16:35
     */

    protected void requestPermissionsCompat(String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    /**
     * @param
     * @return
     * @description 权限检测
     * @time 2021/3/6 16:36
     */
    protected boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        }
        return true;
    }

    @Override
    protected void onStateRefresh() {

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
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }
}