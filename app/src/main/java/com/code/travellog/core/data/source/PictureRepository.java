package com.code.travellog.core.data.source;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
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
import com.code.travellog.core.data.pojo.geo.CityListPojo;
import com.code.travellog.core.data.pojo.geo.CityListResultPojo;
import com.code.travellog.core.data.pojo.geo.CityPojo;
import com.code.travellog.core.data.pojo.geo.CityResultPojo;
import com.code.travellog.core.data.pojo.geo.GeoPojo;
import com.code.travellog.core.data.pojo.picture.PictureExifPojo;
import com.code.travellog.core.data.pojo.weather.WeatherPojo;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.GeoUtil;
import com.code.travellog.util.JsonUtils;
import com.code.travellog.util.StringUtil;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mvvm.event.LiveBus;
import com.mvvm.http.rx.RxSchedulers;
import com.mvvm.stateview.StateConstants;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import tech.spiro.addrparser.parser.Location;

/**
 * @description: 本地图片仓库
 * @date: 2021/3/20
 */
public class PictureRepository extends BaseRepository {

    public static final int currentLevel =13;
    public static String EVENT_KEY_PICEXIF = null ;
    public static String EVENT_KEY_PICPATH = null ;
    public static String ENTER_KEY_GEO = null;
    public static String ENTER_KEY_CITYLIST = null;
    public PictureRepository() {
        if(EVENT_KEY_PICEXIF == null) EVENT_KEY_PICEXIF = StringUtil.getEventKey();
        if(EVENT_KEY_PICPATH == null) EVENT_KEY_PICPATH = StringUtil.getEventKey();
        if(ENTER_KEY_GEO == null) ENTER_KEY_GEO = StringUtil.getEventKey();
        if(ENTER_KEY_CITYLIST == null) ENTER_KEY_CITYLIST = StringUtil.getEventKey();
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
            @SuppressLint("Recycle") Cursor imagecursor =
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 进行反转集合
//        Collections.reverse(galleryList);
        LiveBus.getDefault().postEvent(EVENT_KEY_PICPATH,galleryList);
        postState(StateConstants.SUCCESS_STATE);
    }

    public void getGalleryExif(ContentResolver resolver) throws IOException {

        ArrayList<PictureExifPojo> galleryList0,galleryList;
        MMKV mmkv = MMKV.defaultMMKV();

        String decodeStrings= mmkv.decodeString ("GalleryExif");
        Gson gson = new Gson();
        galleryList0 = gson.fromJson(decodeStrings,new TypeToken<ArrayList<PictureExifPojo>>() {}.getType());
//        galleryList0 = JsonUtils.jsonToArrayList(decodeStrings);

        if (decodeStrings != null && galleryList0 != null && galleryList0.size() != 0){
            Log.w("mmkv0",decodeStrings);
            Log.w("MMKV",galleryList0.toString());
            LiveBus.getDefault().postEvent(EVENT_KEY_PICEXIF,galleryList0);
            return;
        }
        galleryList  = new ArrayList<PictureExifPojo>();

        String DCIMPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Camera/";
        new Thread(()->{
            ExifInterface exifInterface  = null;
            try {
            //获取所在相册和相册id
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            //按照id排序
            final String orderBy = MediaStore.Images.Media._ID;
            //相当于sql语句默认升序排序orderBy，如果降序则最后一位参数是是orderBy+" desc "
            Cursor imagecursor =
                    resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                            null, orderBy);
            //从数据库中取出图存入list集合中
            if (imagecursor != null && imagecursor.getCount() > 0) {
                int tot  =  imagecursor.getCount();

                while (imagecursor.moveToNext()) {
                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                            int lanColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                    String path = imagecursor.getString(dataColumnIndex);
                    if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
                        Uri uri = Uri.fromFile(new File(path));
                        Uri newuri = MediaStore.setRequireOriginal(uri);
                        InputStream stream = resolver.openInputStream(newuri);
                        exifInterface = new ExifInterface(stream);
                    }else {
                        exifInterface = new ExifInterface(path);
                    }
//                    exifInterface = new ExifInterface(path);
                    float a[] = new float[2];
                    exifInterface.getLatLong(a);
                    String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    String lon = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    String latRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                    String lngRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                    String E =exifInterface.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
//                            assert lat != null ;
                    Log.e("TAGGGGG", path+"\n"+Arrays.toString(a));
                    if(a[0] != 0.0 && a[1] != 0.0){
                        PictureExifPojo pictureExifPojo = new PictureExifPojo() ;
                        pictureExifPojo.path = path ;

//                                pictureExifPojo.lan = GeoUtil.convertRationalLatLonToFloat(lat,latRef);
//                                pictureExifPojo.lon = GeoUtil.convertRationalLatLonToFloat(lon,lngRef);
                        pictureExifPojo.lan = (double) a[0];
                        pictureExifPojo.lon = (double) a[1];
                        galleryList.add(pictureExifPojo);
                        Log.d("TAG", "getGalleryPhotos: " + path);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
            // 进行反转集合
//        Collections.reverse(galleryList);
            LiveBus.getDefault().postEvent(EVENT_KEY_PICEXIF,galleryList);
            String json = JsonUtils.toJson(galleryList);
            Log.w("toJosn",json);
            mmkv.encode("GalleryExif",json);
        }).start();

    }
    public void getGeoExif(ArrayList<PictureExifPojo> galleryList) {

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
                assert dataBean != null;
                dataBean.path.add(pictureExifPojo.path);
            }
            else {
                GeoPojo.DataBean dataBean = new GeoPojo.DataBean() ;
                dataBean.lan = pictureExifPojo.lan;
                dataBean.lng = pictureExifPojo.lon;
                dataBean.path = new ArrayList<>();
                dataBean.path.add(pictureExifPojo.path);
                geoPojo.geo.put(pos,dataBean);
            }
            Log.w("geoText",pos.toString());
        }
//        return geoPojo;
        LiveBus.getDefault().postEvent(ENTER_KEY_GEO,geoPojo);


    }

    public void getCityList(GeoPojo geoPojo ) {

//        GeoPojo geoPojo  = getGeoExif(resolver);
        HashMap<Long, GeoPojo.DataBean> geo = geoPojo.geo;
        StringBuilder latitude = new StringBuilder();
        StringBuilder longitude = new StringBuilder();
        int total = geo.size() , now = 0;
        for (Long  cellid :geo.keySet()){
            GeoPojo.DataBean dataBean = geo.get(cellid);
            assert dataBean != null;
            latitude.append(dataBean.lan);
            longitude.append(dataBean.lng);
            if(++now != total) {
                latitude.append(",");
                longitude.append(",");
            }
        }
        addDisposable(apiService.getCityList(longitude.toString(),latitude.toString())
                .compose(RxSchedulers.io_main())
                .subscribeWith(new RxSubscriber<CityListResultPojo>() {
                    @Override
                    public void onSuccess(CityListResultPojo cityListResultPojo) {
                        if (cityListResultPojo.code == 200){
                            updateCityList(geoPojo,cityListResultPojo);
                        }
                        else {
                            postState(StateConstants.ERROR_STATE);
                        }
                    }

                    @Override
                    public void onFailure(String msg, int code) {
                        postState(StateConstants.ERROR_STATE);
                    }
                }));
//        Location location = App.regionDataengine.parse(118.750934,32.038634);

    }
    private void updateCityList(GeoPojo geoPojo , CityListResultPojo cityListResultPojo){
        HashMap<Long, GeoPojo.DataBean> geo = geoPojo.geo;
        CityListPojo cityListPojo = new CityListPojo() ;
        cityListPojo.cityPojos = new ArrayList<>() ;
        HashMap<String,CityPojo> map = new HashMap<>();
        CityPojo cityPojo;
        int total = geo.size() , i = 0;
        for (Long cellid :geo.keySet()){
            GeoPojo.DataBean dataBean = geo.get(cellid);
            assert dataBean != null;
            dataBean.province = cityListResultPojo.data.get(i).province;
            dataBean.city = cityListResultPojo.data.get(i).city;
            dataBean.county = cityListResultPojo.data.get(i).county;
            cityPojo = map.get(dataBean.county);
            if(cityPojo==null){
                cityPojo  = new CityPojo() ;
                cityPojo.city = dataBean.city ;
                cityPojo.county = dataBean.county ;
                cityPojo.province = dataBean.province ;
                cityPojo.lan = dataBean.lan;
                cityPojo.lng  = dataBean.lng;
                cityPojo.path = dataBean.path ;
                cityListPojo.cityPojos.add(cityPojo);
                map.put(cityPojo.county,cityPojo);
            }
            else {
                cityPojo.path.addAll(dataBean.path );
            }
            i++;
        }
//        LiveBus.getDefault().postEvent(ENTER_KEY_GEO,geoPojo);
        LiveBus.getDefault().postEvent(ENTER_KEY_CITYLIST,cityListPojo);
        postState(StateConstants.SUCCESS_STATE);
    }

}
