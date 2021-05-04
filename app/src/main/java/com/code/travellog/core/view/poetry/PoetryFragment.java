package com.code.travellog.core.view.poetry;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.code.travellog.AI.AiBoostManager;
import com.code.travellog.R;
import com.code.travellog.core.data.pojo.poetry.PoetryPojo;
import com.code.travellog.core.data.repository.ApiRepository;
import com.code.travellog.core.viewmodel.ApiViewModel;
import com.code.travellog.util.ToastUtils;
import com.mvvm.base.AbsLifecycleFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
public class PoetryFragment extends AbsLifecycleFragment<ApiViewModel> {

    Unbinder butterKnife;
    /* 图片上传 */
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7461;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rl_title_bar)
    RelativeLayout rlTitleBar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.btn_result)
    Button btnResult;
    @BindView(R.id.tv_poemtype)
    SuperTextView tvpoemtype;
    @BindView(R.id.poemShow)
    TextView poemShow;
    private MediaFile selectedImageFile;
    @BindView(R.id.content_color)
    LinearLayout content_color;
    private EasyImage easyImage;
    private Context mContext;
    private AiBoostManager aiBoostManager = null;

    public static PoetryFragment newInstance() {
        return new PoetryFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_poem;
    }

    @Override
    protected int getContentResId() {
        return R.id.content_color;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
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

        poemShow.setVisibility(View.INVISIBLE);
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

                getImageObjectDetector(selectedImageFile.getFile().getAbsolutePath());

            }
        });
//        textView.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void dataObserver() {
        aiBoostManager = AiBoostManager.newInstance();
        aiBoostManager.initialize(activity, "mobilenet_quant.tflite",
                1001, "mobilenet_quant_labels.txt");
        //物体识别完成
        registerSubscriber(AiBoostManager.EVENT_KEY_OBJECTLIST, null, List.class).observe(this, list -> {
            List<AiBoostManager.Data> mDetectorResult = list;
            Log.w("log","物体识别完成");
            int index = 0;
            int length = !tvpoemtype.getSwitchIsChecked()? 5 : 7;
            StringBuilder factors = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (AiBoostManager.Data entry : mDetectorResult) {
                factors.append(entry.type);
                values.append(entry.value);
                index++;
                if (index != 3) {
                    factors.append(",");
                    values.append(",");
                }
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), selectedImageFile.getFile());
            MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("image", selectedImageFile.getFile().toString(), requestBody)
                    .addFormDataPart("types", String.valueOf(factors))
                    .addFormDataPart("values", String.valueOf(values))
                    .addFormDataPart("length", String.valueOf(length)).build();
            mViewModel.getPoery(multipartBody);
        });
        registerSubscriber(ApiRepository.ENTER_KEY_POETRY, PoetryPojo.class).observe(this, poetryPojo -> {

            if(poetryPojo.code!=200){
                ToastUtils.showToast(poetryPojo.msg);
            }
            else {
                ToastUtils.showToast("获取成功");
                StringBuilder textToShow = new StringBuilder();
                AssetManager mgr = activity.getAssets();
                Typeface tf = Typeface.createFromAsset(mgr, "fonts/balloon.ttf");
                poemShow.setTypeface(tf);
                poemShow.setTextSize(25);
                textToShow.append(poetryPojo.data.poem);
                poemShow.setText(textToShow);
                poemShow.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getImageObjectDetector(String filename) {
        Log.w("file name",filename);
        new Thread(() -> {
            aiBoostManager.setTotal(1);

            aiBoostManager.setResultEmpty();
            Bitmap bitmap = BitmapFactory.decodeFile(filename);

            try {
                aiBoostManager.run(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
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
