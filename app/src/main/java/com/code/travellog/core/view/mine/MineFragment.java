package com.code.travellog.core.view.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.user.User;
import com.code.travellog.glide.GlideCircleTransform;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.ui.AboutActivity;
import com.code.travellog.ui.LoginActivity;
import com.code.travellog.ui.MainActivity;
import com.code.travellog.ui.UserInfoActivity;
import com.code.travellog.util.NetworkUtils;
import com.code.travellog.util.ToastUtils;
import com.leon.lib.settingview.LSettingItem;
import com.mvvm.base.BaseFragment;
import com.mvvm.http.HttpHelper;
import com.tencent.mmkv.MMKV;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    @BindView(R.id.userMore) ImageView userMore;
    protected Context mContext;
    protected MMKV kv;
    protected Unbinder butterKnife;
    /* 图片上传 */
    private static final int MY_ADD_CASE_CALL_PHONE = 6;    //调取系统摄像头的请求码
    private static final int MY_ADD_CASE_CALL_PHONE2 = 7;    //打开相册的请求码
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View layout;
    private TextView takePhotoTV;
    private TextView choosePhotoTV;
    private TextView cancelTV;

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
        butterKnife =ButterKnife.bind(this, rootView);
        this.mContext = activity;
        kv = MMKV.defaultMMKV();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTitleBar = getViewById(R.id.rl_title_bar);
        mTitle = getViewById(R.id.tv_title);
        setTitle(getResources().getString(R.string.mine_title_name));

        String url = kv.decodeString("avatar");
        String username = kv.decodeString("userName");
        String intro = kv.decodeString("intro");

        userInfo.setLeftTopString(username).setLeftBottomString(intro);
        mabout = getActivity().findViewById(R.id.tv_about);
        Glide.with(mContext).load(url)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
        imageView = getActivity().findViewById(R.id.avater);
        imageView.setOnClickListener(v -> {
            UpdatePhoto();
        });
        mabout.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(activity, AboutActivity.class);
                startActivity(intent);
            }
        });
        userMore.setOnClickListener(v -> {
            Intent intent = new Intent(activity,UserInfoActivity.class);
            startActivity(intent);
        });
    }

    public void UpdatePhoto() {

        builder = new AlertDialog.Builder(activity);//创建对话框
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.dialog_select_photo, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        takePhotoTV = layout.findViewById(R.id.photograph);
        choosePhotoTV = layout.findViewById(R.id.photo);
        cancelTV = layout.findViewById(R.id.cancel);
        //设置监听
        takePhotoTV.setOnClickListener(v -> {
            //"点击了照相";
            //  6.0之后动态申请权限 摄像头调取权限,SD卡写入权限
            //判断是否拥有权限，true则动态申请
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_ADD_CASE_CALL_PHONE);
            } else {
                try {
                    //有权限,去打开摄像头
                    takePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
        });
        choosePhotoTV.setOnClickListener(v -> {
            //"点击了相册";
            //  6.0之后动态申请权限 SD卡写入权限
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_ADD_CASE_CALL_PHONE2);

            } else {
                //打开相册
                choosePhoto();
            }
            dialog.dismiss();
        });
        cancelTV.setOnClickListener(v -> {
            dialog.dismiss();//关闭对话框

        });
    }
    private void takePhoto() throws IOException {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        // 获取文件
        File file = createFileIfNeed("UserIcon.png");
        //拍照后原图回存入此路径下
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            /**
             * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
             * 并且这样可以解决MIUI系统上拍照返回size为0的情况
             */
            uri = FileProvider.getUriForFile(mContext, "com.code.travellog.fileprovider", file);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 1);
    }

    // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
    private File createFileIfNeed(String fileName) throws IOException {
        String fileA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/picCache";
        File fileJA = new File(fileA);
        if (!fileJA.exists()) {
            fileJA.mkdirs();
        }
        File file = new File(fileA, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * 打开相册
     */
    private void choosePhoto() {
        //这是打开系统默认的相册(就是你系统怎么分类,就怎么显示,首先展示分类列表)
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, 2);
    }

    /**
     * 申请权限回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_ADD_CASE_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    takePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtils.showToast("拒绝了你的请求");
                //"权限拒绝");
            }
        }


        if (requestCode == MY_ADD_CASE_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                //"权限拒绝");
                ToastUtils.showToast("拒绝了你的请求");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * startActivityForResult执行后的回调方法，接收返回的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照
        if (requestCode == 1 && resultCode != Activity.RESULT_CANCELED) {

            String state = Environment.getExternalStorageState();
            if (!state.equals(Environment.MEDIA_MOUNTED)) return;
            // 把原图显示到界面上
            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            Tiny.getInstance().source(readpic()).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                    saveImageToServer(bitmap, outfile);//显示图片到imgView上
                }
            });
        }
        // 上传图片
        else if (requestCode == 2 && resultCode == Activity.RESULT_OK
                && null != data) {
            try {
                Uri selectedImage = data.getData();//获取路径
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(selectedImage).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                    @Override
                    public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                        saveImageToServer(bitmap, outfile);
                    }
                });
            } catch (Exception e) {
                ToastUtils.showToast("上传失败，请重试");
            }
        }
    }

    /**
     * 从保存原图的地址读取图片
     */
    private String readpic() {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/picCache/" + "UserIcon.png";
        return filePath;
    }

    @SuppressLint("CheckResult")
    private void saveImageToServer(final Bitmap bitmap, String outfile) {
        File file = new File(outfile);
        Log.w("filename",outfile);
        Glide.with(mContext).load(bitmap)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);
        String uid = Integer.toString(MMKV.defaultMMKV().decodeInt("uid"));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid",uid)
                .addFormDataPart("code","travel")
                .addFormDataPart("avatar",outfile,requestBody).build();
                NetworkUtils.createPartByPathAndKey(outfile,"file");
        HttpHelper.getInstance().create(ApiService.class).postCaptchaAvater(multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<BasePojo>() {
                    @Override
                    public void onSuccess(BasePojo basePojo) {
                        if(basePojo.code!=200) {
                            onFailure(basePojo.msg,basePojo.code);
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



    @Override
    protected void onStateRefresh() {

    }
    /**
     * set title
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
