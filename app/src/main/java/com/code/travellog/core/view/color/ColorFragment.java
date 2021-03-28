package com.code.travellog.core.view.color;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.extraction.ColorPojo;
import com.code.travellog.core.data.source.ApiRepository;
import com.code.travellog.core.vm.ApiViewModel;
import com.code.travellog.util.ToastUtils;
import com.mvvm.base.AbsLifecycleFragment;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

/**
 * @description:
 * @date: 2021/3/23
 */
public class ColorFragment extends AbsLifecycleFragment<ApiViewModel> {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    //    @BindView(R.id.rl_title_bar)
//    RelativeLayout rlTitleBar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.btn_result)
    Button btnResult;
    Unbinder butterKnife;
    /* 图片上传 */
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7461;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.maincolor)
    ImageView maincolor;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.secondarycolor)
    ImageView secondarycolor;
    @BindView(R.id.result)
    LinearLayout result;
    @BindView(R.id.maintextView)
    TextView maintextView;
    @BindView(R.id.secondarytextView)
    TextView secondarytextView;
    private MediaFile selectedImageFile;
    @BindView(R.id.content_color)
    LinearLayout content_color;
    private EasyImage easyImage;
    private Context mContext;

    public static ColorFragment newInstance() {
        return new ColorFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_color;
    }

    @Override
    protected int getContentResId() {
        return R.id.content_color;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        butterKnife = ButterKnife.bind(this, rootView);
        super.initView(savedInstanceState);
        this.mContext = activity;
        easyImage = new EasyImage.Builder(mContext)
                .setChooserTitle("选择图片")
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("picCache")
                .allowMultiple(false)
                .build();

        loadManager.showSuccess();
        ToastUtils.showToast("请点击图片并选择照片");
        image.setOnClickListener(v -> {
            String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (arePermissionsGranted(necessaryPermissions)) {
                easyImage.openChooser(activity);
            } else {
                requestPermissionsCompat(necessaryPermissions, CHOOSER_PERMISSIONS_REQUEST_CODE);
            }
        });
        btnResult.setOnClickListener(v -> {
            if (selectedImageFile == null) {
                ToastUtils.showToast("请先选择图片");
            } else {
                ToastUtils.showToast("获取中，请稍后");
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), selectedImageFile.getFile());
                MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("image", selectedImageFile.getFile().toString(), requestBody).build();

                mViewModel.getColorResult(multipartBody);
            }
        });

        result.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void dataObserver() {
        registerSubscriber(ApiRepository.ENTER_KEY_COLOR, ColorPojo.class).observe(this, colorPojo -> {

            if (colorPojo.code != 200) {
                ToastUtils.showToast(colorPojo.msg);
            } else {
                ToastUtils.showToast("获取成功");
                StringBuilder textToShow = new StringBuilder();
                float r = 0, b = 0, g = 0;
                for (int i = 0; i < colorPojo.data.result.size(); i++) {
                    r = colorPojo.data.result.get(i).get(0);
                    g = colorPojo.data.result.get(i).get(1);
                    b = colorPojo.data.result.get(i).get(2);
                    textToShow.append(String.format("\n rgb值为(%.0f ,%.0f ,%.0f)", r, g, b));
                    if (i == 0) {
                        ColorStateList colors = ColorStateList.valueOf(Color.argb(255, r, g, b));
                        Drawable drawable = DrawableCompat.wrap(maincolor.getDrawable());
                        DrawableCompat.setTintList(drawable, colors);
                        maincolor.setImageDrawable(drawable);
                        maintextView.setText(textToShow);
                    } else {
                        ColorStateList colors = ColorStateList.valueOf(Color.argb(255, r, g, b));
                        Drawable drawable = DrawableCompat.wrap(secondarycolor.getDrawable());
                        DrawableCompat.setTintList(drawable, colors);
                        secondarycolor.setImageDrawable(drawable);
                        secondarytextView.setText(textToShow);
                    }
                    textToShow = new StringBuilder();
                }
//                textView.setTextColor();
//                textView.setText(textToShow);
//                textView.setVisibility(View.VISIBLE);
//                textView1.setText("1111");
//                textView1.setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);
            }
        });
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
                setUI(imgFiles);
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

    public void setUI(@NotNull MediaFile[] returnedPhotos) {
        Log.w("Imagefile", returnedPhotos[0].getFile().toString());
        Glide.with(mContext).load(returnedPhotos[0].getFile())
                .into(image);
        selectedImageFile = returnedPhotos[0];

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
     * set title
     *
     * @param titleName
     */
    protected void setTitle(String titleName) {
        //rlTitleBar.setVisibility(View.VISIBLE);
        tvTitle.setText(titleName);
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
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }


}