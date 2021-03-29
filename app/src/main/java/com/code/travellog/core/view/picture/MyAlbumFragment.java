package com.code.travellog.core.view.picture;

import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.code.travellog.config.Constants;
import com.ibbhub.album.AlbumBean;
import com.ibbhub.album.AlbumFragment;
import com.ibbhub.album.ITaDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：chezi008 on 2018/8/19 14:57
 * @description ：
 * @email ：chezi008@163.com
 */
public class MyAlbumFragment extends AlbumFragment {
    @Override
    public List<File> buildAlbumSrc() {
        File mediaFile  = null;
        Log.w("11111","testttt");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/DCIM/Camera/IMG_20210327_195230.jpg";
        Log.w("11",path);
        List<File> fileList = new ArrayList<>();
        fileList.add(new File(path));
        path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/DCIM/Camera/IMG_20210327_195230.jpg";
        fileList.add(new File(path));
        return fileList;
    }
    @Override
    public Boolean obtainFile(File file) {
        return file.getName().endsWith(".mp4") || file.getName().endsWith(".jpg");
    }

    @Override
    public ITaDecoration buildDecoration() {
        return null;
    }

    @Override
    public String fileProviderName() {
        return Constants.APPID +".album.provider";
    }

    @Override
    public void loadOverrideImage(String path, ImageView iv) {
        Glide.with(iv)
                .load(path)
                .thumbnail(0.1f)
                .apply(buildOptions())
                .into(iv);
    }

    @Override
    public void loadImage(String path, ImageView iv) {
        Glide.with(iv)
                .load(path)
                .thumbnail(0.1f)
                .into(iv);
    }

    @Override
    public void onChooseModeChange(boolean isChoose) {
//        ((AlbumActivity)getActivity()).onChooseModeChange(isChoose);
    }

    public static RequestOptions buildOptions() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.override(100, 100);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    @Override
    public void start2Preview(ArrayList<AlbumBean> data, int pos) {
//        MyPreviewActivity.start(getContext(), data, pos);
    }
}
