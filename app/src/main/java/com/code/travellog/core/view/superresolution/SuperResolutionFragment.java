package com.code.travellog.core.view.superresolution;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.code.travellog.App;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.supervision.SuperVisionPojo;
import com.code.travellog.core.data.repository.ApiRepository;
import com.code.travellog.core.viewmodel.ApiViewModel;
import com.code.travellog.util.Base64Utils;
import com.code.travellog.util.BitmapUtil;
import com.code.travellog.util.ImageSaveUtil;
import com.code.travellog.util.ToastUtils;
import com.coloros.ocs.ai.cv.CVUnitClient;
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
public class SuperResolutionFragment extends AbsLifecycleFragment<ApiViewModel> {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    //    @BindView(R.id.rl_title_bar)
//    RelativeLayout rlTitleBar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.btn_result)
    Button btnResult;
    Unbinder butterKnife;
    /* 图片上传 */
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7462;
    @BindView(R.id.btn_get)
    Button btnGet;
    private MediaFile selectedImageFile;
    private EasyImage easyImage;
    private Context mContext;
    private Bitmap outbitmap;
    public static SuperResolutionFragment newInstance() {
        return new SuperResolutionFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_superresolution;
    }

    @Override
    protected int getContentResId() {
        return R.id.content_superresolution;
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
        ToastUtils.showToast("请点击图片以进行选择");
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
                btnGet.setVisibility(View.INVISIBLE);
                mViewModel.getResolutionResult(multipartBody);
            }
        });
        btnGet.setVisibility(View.INVISIBLE);
        btnGet.setOnClickListener(v -> {
            new Thread(()->{
//                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), outbitmap, null, null));
                ImageSaveUtil.saveAlbum(mContext,outbitmap, Bitmap.CompressFormat.JPEG,100,true);
            }).start();
            ToastUtils.showToast("保存成功");

        });
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void dataObserver() {
        registerSubscriber(ApiRepository.ENTER_KEY_RESOLUTION, SuperVisionPojo.class).observe(this, superVisionPojo -> {

            if(superVisionPojo.code!=200){
                ToastUtils.showToast(superVisionPojo.msg);
            }
            else {
                byte[] bitmapbyte = Base64Utils.decode(superVisionPojo.data);
                Bitmap bitmap =BitmapFactory.decodeByteArray(bitmapbyte,0,bitmapbyte.length);
                btnGet.setVisibility(View.VISIBLE);
//                bitmap = BitmapUtil.ChangeSize(bitmap,120,200);
                Glide.with(this).load(bitmap).into(image);
                image.setImageBitmap(bitmap);
                outbitmap = bitmap ;
//                if(BitmapUtil.saveMyBitmap(outbitmap))
                ToastUtils.showToast("获取成功");


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
