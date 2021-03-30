package com.code.travellog.network;

import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.activity.ActivityListVo;
import com.code.travellog.core.data.pojo.album.AlbumListPojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.core.data.pojo.album.AlbumWorkPojo;
import com.code.travellog.core.data.pojo.article.ArticleTypeVo;
import com.code.travellog.core.data.pojo.article.ArticleVo;
import com.code.travellog.core.data.pojo.banner.BannerListVo;
import com.code.travellog.core.data.pojo.book.BookListVo;
import com.code.travellog.core.data.pojo.book.BookTypeVo;
import com.code.travellog.core.data.pojo.correct.WorkDetailVo;
import com.code.travellog.core.data.pojo.correct.WorkRecommentVo;
import com.code.travellog.core.data.pojo.correct.WorksListVo;
import com.code.travellog.core.data.pojo.course.CourseDetailRemVideoVo;
import com.code.travellog.core.data.pojo.course.CourseDetailVo;
import com.code.travellog.core.data.pojo.course.CourseListVo;
import com.code.travellog.core.data.pojo.course.CourseRemVo;
import com.code.travellog.core.data.pojo.course.CourseTypeVo;
import com.code.travellog.core.data.pojo.dynamic.DynamicListVo;
import com.code.travellog.core.data.pojo.extraction.ColorPojo;
import com.code.travellog.core.data.pojo.followdraw.FollowDrawRecommendVo;
import com.code.travellog.core.data.pojo.followdraw.FollowDrawTypeVo;
import com.code.travellog.core.data.pojo.geo.CityListResultPojo;
import com.code.travellog.core.data.pojo.geo.CityPojo;
import com.code.travellog.core.data.pojo.geo.CityResultPojo;
import com.code.travellog.core.data.pojo.home.HomeListVo;
import com.code.travellog.core.data.pojo.live.LiveDetailsVo;
import com.code.travellog.core.data.pojo.live.LiveListVo;
import com.code.travellog.core.data.pojo.live.LiveTypeVo;
import com.code.travellog.core.data.pojo.material.MaterialRecommendVo;
import com.code.travellog.core.data.pojo.material.MaterialTypeVo;
import com.code.travellog.core.data.pojo.material.MaterialVo;
import com.code.travellog.core.data.pojo.image.ImagePojo;
import com.code.travellog.core.data.pojo.poetry.PoetryPojo;
import com.code.travellog.core.data.pojo.qa.QaListVo;
import com.code.travellog.core.data.pojo.user.UserPojo;
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
 * @author tqzhang
 */
public interface ApiService {

    // login
    @FormUrlEncoded
    @POST(URL.BASE_URL2+"user/login")
    Flowable<UserPojo> LoginApi(@FieldMap Map<String,String> params);
    //register
    @FormUrlEncoded
    @POST(URL.BASE_URL2+"user/register")
    Flowable<BasePojo> RegisterApi(@FieldMap Map<String,String> params);
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

    @GET(URL.BASE_URL2+"movie/new")
    Flowable<AlbumWorkPojo> getAlbumWorkid();

    @GET
    Flowable<BasePojo> startAlbum(@Url String url);

    @GET
    Flowable<AlbumResultPojo> getAlbumResult(@Url String url);

    @GET(URL.ALBUM_URL+"list")
    Flowable<AlbumListPojo> getAlbumList();


    @POST(URL.API_URL+"color")
    Flowable<ColorPojo> getColor(@Body MultipartBody multipartBody);

    @POST(URL.API_URL+"weather")
    Flowable<WeatherPojo> getWeather(@Body MultipartBody multipartBody);

    @POST(URL.API_URL+"poem")
    @FormUrlEncoded
    Flowable<PoetryPojo> getPoetry(@Field("keyword") String keyword ,@Field("length") int length ,@Field("experience") int experience ,@Field("history") int history);

    @POST(URL.API_URL+"location")
    @FormUrlEncoded
    Flowable<CityListResultPojo> getCityList(@Field("longitude") String longitude,@Field("latitude") String latitude);

    @GET(URL.API_URL+"location")
    @FormUrlEncoded
    Flowable<CityResultPojo> getCity(@Field("longitude") String longitude,@Field("latitude") String latitude);













    @POST(URL.HOME_LIST)
    @FormUrlEncoded
    Flowable<HomeListVo> getHomeData(@Field("professionid") String professionId);

    @POST(URL.WORK_LIST)
    @FormUrlEncoded
    Flowable<WorksListVo> getWorkData(@Field("corrected") String corrected, @Field("rn") String rn);

    @POST(URL.WORK_MORE_LIST)
    @FormUrlEncoded
    Flowable<WorksListVo> getWorkMoreData(@Field("last_id") String last_id, @Field("utime") String utime, @Field("rn") String rn);

    @POST(URL.WORK_DETAIL)
    @FormUrlEncoded
    Flowable<WorkDetailVo> getWorkDetailData(@Field("correctid") String correctid);

    @POST(URL.WORK_RECOMMEND)
    @FormUrlEncoded
    Flowable<WorkRecommentVo> getWorkRecommendData(@Field("correctid") String correctid);


    @POST(URL.BANNER)
    @FormUrlEncoded
    Flowable<BannerListVo> getBannerData(@Field("pos_type") String posType,
                                           @Field("f_catalog_id") String f_catalog_id,
                                           @Field("s_catalog_id") String s_catalog_id,
                                           @Field("t_catalog_id") String t_catalog_id,
                                           @Field("province") String province
    );

    @GET(URL.VIDEO_TYPE)
    Flowable<CourseTypeVo> getCourseType();

    @GET(URL.RECOMMEND_COURSE)
    Flowable<CourseRemVo> getCourseRemList();

    @POST(URL.VIDEO_LIST)
    @FormUrlEncoded
    Flowable<CourseListVo> getCourseList(@Field("f_catalog_id") String f_catalog_id, @Field("lastid") String lastid, @Field("rn") String rn);

    @GET(URL.MATERIAL_TYPE)
    Flowable<MaterialTypeVo> getMaterialInfo();

    @GET(URL.LIVING_TYPE)
    Flowable<LiveTypeVo> getLiveType();

    @GET(URL.ARTICLE_TYPE)
    Flowable<ArticleTypeVo> getArticleType();

    @GET(URL.FOLLOW_D_TYPE)
    Flowable<FollowDrawTypeVo> getFollowDrawType();

    @GET(URL.BOOK_TYPE)
    Flowable<BookTypeVo> getBookType();

    @POST(URL.DYNAMIC_LIST)
    @FormUrlEncoded
    Flowable<DynamicListVo> getDynamicList(@Field("rn") String rn, @Field("token") String token, @Field("lastid") String lastid);


    @POST(URL.ARTICLE_LIST)
    @FormUrlEncoded
    Flowable<ArticleVo> getArticleRemList(@Field("lecture_level1") String lecture_level1, @Field("lastid") String lastid, @Field("rn") String rn);

    @POST(URL.RECOMMEND_BOOKS_LIST)
    @FormUrlEncoded
    Flowable<BookListVo> getBookList(@Field("f_catalog_id") String f_catalog_id, @Field("last_id") String last_id, @Field("rn") String rn);

    @POST(URL.MATERIAL_SUBJECT_LIST)
    @FormUrlEncoded
    Flowable<MaterialRecommendVo> getMaterialRemList(@Field("f_catalog_id") String f_catalog_id, @Field("lastid") String lastid, @Field("rn") String rn);


    @POST(URL.MATERIAL_LIST_NEW)
    @FormUrlEncoded
    Flowable<MaterialVo> getMaterialList(@Field("f_catalog_id") String f_catalog_id, @Field("mlevel") String mlevel, @Field("rn") String rn);

    @POST(URL.MATERIAL_LIST)
    @FormUrlEncoded
    Flowable<MaterialVo> getMaterialMoreList(@Field("f_catalog_id") String f_catalog_id, @Field("mlevel") String mlevel, @Field("lasttid") String lasttid, @Field("rn") String rn);

    @POST(URL.FOLLOW_RECOMMEND)
    @FormUrlEncoded
    Flowable<FollowDrawRecommendVo> getFollowDrawRemList(@Field("lastid") String lastid, @Field("rn") String rn);

    @POST(URL.FOLLOW_LIST)
    @FormUrlEncoded
    Flowable<FollowDrawRecommendVo> getollowDrawList(@Field("maintypeid") String maintypeid, @Field("lastid") String lastid, @Field("rn") String rn);

    @POST(URL.ACTIVITY_LIST)
    @FormUrlEncoded
    Flowable<ActivityListVo> getActivityList(@Field("lastid") String lastid, @Field("rn") String rn);

    @POST(URL.QA_LIST)
    @FormUrlEncoded
    Flowable<QaListVo> getQAList(@Field("lastid") String lastid, @Field("rn") String rn);

    @POST(URL.LIVE_LIST)
    @FormUrlEncoded
    Flowable<LiveListVo> getLiveRem(@Field("lastid") String lastid, @Field("rn") String rn);

    @POST(URL.LIVING_LIST)
    @FormUrlEncoded
    Flowable<LiveListVo> getLiveList(@Field("f_catalog_id") String f_catalog_id, @Field("lastid") String lastid, @Field("rn") String rn);

    @POST(URL.VIDEO_DETAILS_DATA)
    @FormUrlEncoded
    Flowable<CourseDetailVo> getVideoDetailsData(@Field("courseid") String courseId, @Field("notbrowse") String notBrowse);

    @POST(URL.LIVE_DETAILS_DATA)
    @FormUrlEncoded
    Flowable<LiveDetailsVo> getLiveData(@Field("liveid") String liveid);

    @POST(URL.VIDEO_DETAILS_ABOUT_DATA)
    @FormUrlEncoded
    Flowable<CourseDetailRemVideoVo> getVideoAboutData(@Field("courseid") String courseId
            , @Field("f_catalog_id") String f_catalog_id
            , @Field("s_catalog_id") String s_catalog_id
            , @Field("teacherid") String teacherId
            , @Field("rn") String rn
    );
}
