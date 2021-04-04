package com.code.travellog.network;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.album.AlbumListPojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.core.data.pojo.album.AlbumWorkPojo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.extraction.ColorPojo;
import com.code.travellog.core.data.pojo.geo.CityListResultPojo;
import com.code.travellog.core.data.pojo.geo.CityResultPojo;
import com.code.travellog.core.data.pojo.image.ImagePojo;
import com.code.travellog.core.data.pojo.plog.PlogListPojo;
import com.code.travellog.core.data.pojo.plog.PlogResultPojo;
import com.code.travellog.core.data.pojo.plog.PlogWorkPojo;
import com.code.travellog.core.data.pojo.poetry.PoetryPojo;
import com.code.travellog.core.data.pojo.supervision.SuperVisionPojo;
import com.code.travellog.core.data.pojo.user.UserPojo;
import com.code.travellog.core.data.pojo.video.VideoListPojo;
import com.code.travellog.core.data.pojo.weather.WeatherPojo;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @description RestFul 接口
 * @time 2021/4/3 0:41
 */

public interface ApiService {

    // login
    @FormUrlEncoded
    @POST(URL.BASE_URL2+"user/login")
    Flowable<UserPojo> LoginApi(@FieldMap Map<String,String> params);
    //register
    @FormUrlEncoded
    @POST(URL.BASE_URL2+"user/register")
    Flowable<UserPojo> RegisterApi(@FieldMap Map<String,String> params);
    // get captcha
    @GET(URL.BASE_URL2+"user/captcha")
    Flowable<ImagePojo> getCaptchaAvater();

    @POST(URL.BASE_URL2+"user/avatar")
    Flowable<BasePojo> postAvater(@Body MultipartBody multipartBody);

    @FormUrlEncoded
    @POST(URL.BASE_URL2+"user/info")
    Flowable<UserPojo> postUserInfo(@FieldMap Map<String,String> params);

    @GET(URL.BASE_URL2+"user/info")
    Flowable<UserPojo> getUserInfo();

    @FormUrlEncoded
    @POST(URL.BASE_URL2+"user/password")
    Flowable<BasePojo> changePwd(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST(URL.BASE_URL2+"user/reset")
    Flowable<BasePojo> resetPwd(@FieldMap Map<String,String> params);

    @POST
    Flowable<BasePojo> postPic(@Url String url ,@Body MultipartBody multipartBody);

    @GET(URL.ALBUM_URL+"new")
    Flowable<AlbumWorkPojo> getAlbumWorkid();

    @GET
    Flowable<BasePojo> startAlbum(@Url String url);

    @GET
    Flowable<AlbumResultPojo> getAlbumResult(@Url String url);

    @GET(URL.ALBUM_URL+"list")
    Flowable<AlbumListPojo> getUserAlbumList();


    @POST(URL.API_URL+"color")
    Flowable<ColorPojo> getColor(@Body MultipartBody multipartBody);

    @POST(URL.API_URL+"weather")
    Flowable<WeatherPojo> getWeather(@Body MultipartBody multipartBody);

    @POST(URL.API_URL+"img2poem")
    Flowable<PoetryPojo> getPoetry(@Body MultipartBody multipartBody);

    @POST(URL.API_URL+"fsrcnn")
    Flowable<SuperVisionPojo> getResolution(@Body MultipartBody multipartBody);
    @POST(URL.API_URL+"location")
    @FormUrlEncoded
    Flowable<CityListResultPojo> getCityList(@Field("longitude") String longitude,@Field("latitude") String latitude);

    @GET(URL.API_URL+"location")
    @FormUrlEncoded
    Flowable<CityResultPojo> getCity(@Field("longitude") String longitude,@Field("latitude") String latitude);

    @GET(URL.BASE_URL2+"forum")
    Flowable<VideoListPojo> getVideoList();

    @GET(URL.PLOG_URL+"new")
    Flowable<PlogWorkPojo> getPlogWorkid();

    @GET
    Flowable<BasePojo> startPlog(@Url String url);

    @GET
    Flowable<PlogResultPojo> getPlogResult(@Url String url);


    @GET(URL.PLOG_URL+"all")
    Flowable<PlogListPojo> getPlogList();

    @GET(URL.PLOG_URL+"list")
    Flowable<PlogListPojo> getUserPlogList();
//
//    @POST(URL.WORK_LIST)
//    @FormUrlEncoded
//    Flowable<PlogsListVo> getWorkData(@Field("corrected") String corrected, @Field("rn") String rn);
//
//    @POST(URL.WORK_MORE_LIST)
//    @FormUrlEncoded
//    Flowable<PlogsListVo> getWorkMoreData(@Field("last_id") String last_id, @Field("utime") String utime, @Field("rn") String rn);
//
//    @POST(URL.WORK_DETAIL)
//    @FormUrlEncoded
//    Flowable<PlogDetailVo> getWorkDetailData(@Field("correctid") String correctid);
//
//    @POST(URL.WORK_RECOMMEND)
//    @FormUrlEncoded
//    Flowable<PlogRecommentVo> getWorkRecommendData(@Field("correctid") String correctid);


    @GET(URL.BANNER)
    Flowable<BannerListVo> getBannerData();

}
