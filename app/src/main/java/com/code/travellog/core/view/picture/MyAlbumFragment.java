package com.code.travellog.core.view.picture;

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
 * @description
 * @time 2021/3/29 17:06
 */

public class MyAlbumFragment extends AlbumFragment {
    @Override
    public List<File> buildAlbumSrc() {

        List<File> fileList = new ArrayList<>();
         assert PictureShowActivity.file !=null;
         for(String filename : PictureShowActivity.file){
             fileList.add(new File(filename));
         }
        Log.w("11111","test"+ PictureShowActivity.file.size());
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/DCIM/Camera/IMG_20210327_195230.jpg";
//        Log.w("11",path);
//        fileList.add(new File(path));
//        path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/DCIM/Camera/IMG_20210327_195230.jpg";
//        fileList.add(new File(path));
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
        ((PictureShowActivity)getActivity()).onChooseModeChange(isChoose);
    }

    public static RequestOptions buildOptions() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.override(100, 100);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    @Override
    public void start2Preview(ArrayList<AlbumBean> data, int pos) {
        MyPreviewActivity.start(getContext(), data, pos);
    }
}
