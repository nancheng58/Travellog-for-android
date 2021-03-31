package com.code.travellog.core.vm;

import android.app.Application;
import android.content.ContentResolver;

import androidx.annotation.NonNull;

import com.code.travellog.core.data.pojo.geo.GeoPojo;
import com.code.travellog.core.data.pojo.picture.PictureExifPojo;
import com.code.travellog.core.data.source.PictureRepository;
import com.mvvm.base.AbsViewModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @description:
 * @date: 2021/3/20
 */
public class PictureViewModel extends AbsViewModel<PictureRepository> {
    public PictureViewModel(@NonNull Application application) {
        super(application);
    }
    public void getGalleryExif(ContentResolver contentResolver) throws IOException {
        mRepository.getGalleryExif(contentResolver);
    }
    public void getGeoExif(ArrayList<PictureExifPojo> arrayList){
        mRepository.getGeoExif(arrayList);
    }
    public void getGalleryPhotosPath(ContentResolver contentResolver){
        mRepository.getGalleryPhotosPath(contentResolver);
    }
    public void getCityList(GeoPojo geoPojo)  {
        mRepository.getCityList(geoPojo);
    }
}
