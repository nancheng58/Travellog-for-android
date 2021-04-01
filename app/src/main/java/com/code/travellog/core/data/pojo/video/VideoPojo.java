package com.code.travellog.core.data.pojo.video;

import com.code.travellog.core.data.pojo.album.AlbumPojo;

/**
 * @description:
 * @date: 2021/4/1
 */
public class VideoPojo {

    public int post_id ;
    public int uid ;
    public String uname;
    public int gender;
    public String avatar;
    public String title;
    public String text;
    public int type;
    public String cover;
    public DataBean reference;
    public static class DataBean{
        public int work_id;
        public int uid;
        public String movie_title;
        public String movie_description;
        public String movie_cover;
        public String create_time ;
        public String update_time;
        public int status ;
        public String status_msg;
        public String result_msg;
    }
/**
 {
         "post_id": 12,
         "uid": 2,
         "uname": "T20210435",
         "gender": 0,
         "avatar": "/media/avatars/default.jpg",
         "title": "学校",
         "text": "123",
         "type": 2,
         "cover": "/media/movies/29/covers8v0.jpg",
         "reference":
         {
             "work_id": 29,
             "movie_title": "学校",
             "movie_description": "123",
             "movie_cover": "/media/movies/29/covers8v0.jpg",
             "create_time": "2021-04-01 15:25:30",
             "update_time": "2021-04-01 15:26:17",
             "status": 200,
             "status_msg": "处理完成",
             "result_msg": "/media/movies/29/resultpbd8.mp4"
         },
         "time": "2021-04-01 15:26:17", "like_num": 0
 }

 **/
}
