package com.code.travellog.core.data.source;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.amap.api.services.geocoder.GeocodeSearch;
import com.code.travellog.App;
import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.core.data.pojo.geo.GeoPojo;
import com.code.travellog.core.data.pojo.picture.PictureExifPojo;
import com.code.travellog.util.GeoUtil;
import com.code.travellog.util.StringUtil;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.mvvm.event.LiveBus;
import com.mvvm.stateview.StateConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import tech.spiro.addrparser.parser.Location;

/**
 * @description: 本地图片仓库
 * @date: 2021/3/20
 */
public class PictureRepository extends BaseRepository {
    public static String EVENT_KEY_PICEXIF = null ;
    public static String EVENT_KEY_PICPATH = null ;
    public static final int currentLevel =13;
    public PictureRepository() {
        if(EVENT_KEY_PICEXIF == null) EVENT_KEY_PICEXIF = StringUtil.getEventKey();
        if(EVENT_KEY_PICPATH == null) EVENT_KEY_PICPATH = StringUtil.getEventKey();

    }

    //获取所有图片存入list集合返回,MediaStore.Images.Media.DATA中的Images
    public void getGalleryPhotosPath(ContentResolver resolver) {
        ArrayList<String> galleryList = new ArrayList<String>();
        ExifInterface exifInterface  = null;
        String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Camera/";
        try {
            //获取所在相册和相册id
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.LATITUDE};
            //按照id排序
            final String orderBy = MediaStore.Images.Media._ID;

            //相当于sql语句默认升序排序orderBy，如果降序则最后一位参数是是orderBy+" desc "
            Cursor imagecursor =
                    resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                            null, orderBy);

            //从数据库中取出图存入list集合中
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    int lanColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
//                    Log.d("tgw7", "getGalleryPhotos: " + dataColumnIndex);
                    String path = imagecursor.getString(dataColumnIndex);
//                    Float lan = imagecursor.getFloat(lanColumnIndex);

                    if(true){
                        exifInterface = new ExifInterface(path);
                        String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        String lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        if(lat != null && lon != null){
                            Log.d("TAG",lat);
                            galleryList.add(path);
                            Log.d("TAG", "getGalleryPhotos: " + path);
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 进行反转集合
//        Collections.reverse(galleryList);
        LiveBus.getDefault().postEvent(EVENT_KEY_PICPATH,galleryList);
        postState(StateConstants.SUCCESS_STATE);
    }

    private ArrayList<PictureExifPojo> getGalleryExif(ContentResolver resolver) throws IOException {

        ArrayList<PictureExifPojo> galleryList = new ArrayList<PictureExifPojo>();
        ExifInterface exifInterface  = null;
        String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Camera/";
        try {
                //获取所在相册和相册id
                final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,MediaStore.Images.Media.LATITUDE};
                //按照id排序
                final String orderBy = MediaStore.Images.Media._ID;
                //相当于sql语句默认升序排序orderBy，如果降序则最后一位参数是是orderBy+" desc "
                Cursor imagecursor =
                        resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                                null, orderBy);
                //从数据库中取出图存入list集合中
                if (imagecursor != null && imagecursor.getCount() > 0) {
                        while (imagecursor.moveToNext()) {
                            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                            int lanColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                            String path = imagecursor.getString(dataColumnIndex);
                            if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
                                Uri uri = Uri.fromFile(new File(path));
                                Uri newuri = MediaStore.setRequireOriginal(uri);
                                InputStream stream = resolver.openInputStream(newuri);
                                exifInterface = new ExifInterface(stream);
                            }else {
                                exifInterface = new ExifInterface(path);
                            }

                            String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                            String lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                            String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                            String lngRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                            String E =exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
                            Log.e("TAGGGGG",E);
                            if(lat != null && lon != null){
                                PictureExifPojo pictureExifPojo = new PictureExifPojo() ;
                                pictureExifPojo.path = path ;

                                pictureExifPojo.lan = GeoUtil.convertRationalLatLonToFloat(lat,latRef);
                                pictureExifPojo.lon = GeoUtil.convertRationalLatLonToFloat(lon,lngRef);
                                galleryList.add(pictureExifPojo);
                                Log.d("TAG", "getGalleryPhotos: " + path);
                            }
                        }
                    }
            } catch (Exception e) { e.printStackTrace(); }
        // 进行反转集合
//        Collections.reverse(galleryList);
        return galleryList;
    }
    public void getGeoExif(ContentResolver resolver ) throws IOException {
        ArrayList<PictureExifPojo> galleryList =getGalleryExif(resolver);
        GeoPojo geoPojo = new GeoPojo();
        geoPojo.geo = new HashMap<>();
        for (PictureExifPojo pictureExifPojo :galleryList){
            GeoUtil.LatLng latLng  = new GeoUtil.LatLng(pictureExifPojo.lan,pictureExifPojo.lon);
            latLng = GeoUtil.gcj02ToWgs84(latLng);// 地理坐标变换：GCJ-02 -> WGS-84
            S2LatLng s2LatLng = S2LatLng.fromDegrees(latLng.latitude,latLng.longitude);
            S2CellId cellId = S2CellId.fromLatLng(s2LatLng).parent(currentLevel);
            Long pos = cellId.id() ;

            if(geoPojo.geo.get(pos)!=null) {
                GeoPojo.DataBean dataBean = geoPojo.geo.get(pos);
                dataBean.path.add(pictureExifPojo.path);
            }
            else {
                GeoPojo.DataBean dataBean = new GeoPojo.DataBean() ;
                dataBean.lan = pictureExifPojo.lan;
                dataBean.lng = pictureExifPojo.lon;
                dataBean.path = new ArrayList<>();
                geoPojo.geo.put(pos,dataBean);
            }
            Log.w("geoText",pos.toString());
        }
        LiveBus.getDefault().postEvent(EVENT_KEY_PICEXIF,geoPojo);
    }

    public void getCity(ContentResolver resolver) throws IOException {

        ArrayList<PictureExifPojo> galleryList =getGalleryExif(resolver);
        GeoPojo geoPojo = new GeoPojo();
        geoPojo.geo = new HashMap<>();
        for (PictureExifPojo pictureExifPojo :galleryList){
            GeoUtil.LatLng latLng  = new GeoUtil.LatLng(pictureExifPojo.lan,pictureExifPojo.lon);
            latLng = GeoUtil.gcj02ToWgs84(latLng);// 地理坐标变换：GCJ-02 -> WGS-84
            S2LatLng s2LatLng = S2LatLng.fromDegrees(latLng.latitude,latLng.longitude);
            S2CellId cellId = S2CellId.fromLatLng(s2LatLng).parent(currentLevel);
            Long pos = cellId.id() ;

            if(geoPojo.geo.get(pos)!=null) {
                GeoPojo.DataBean dataBean = geoPojo.geo.get(pos);
                dataBean.path.add(pictureExifPojo.path);
            }
            else {
                GeoPojo.DataBean dataBean = new GeoPojo.DataBean() ;
                dataBean.lan = pictureExifPojo.lan;
                dataBean.lng = pictureExifPojo.lon;
                dataBean.path = new ArrayList<>();
                geoPojo.geo.put(pos,dataBean);
            }
            Log.w("geoText",pos.toString());
        }
        LiveBus.getDefault().postEvent(EVENT_KEY_PICEXIF,geoPojo);
        Location location = App.regionDataengine.parse(118.750934,32.038634);

    }
}
