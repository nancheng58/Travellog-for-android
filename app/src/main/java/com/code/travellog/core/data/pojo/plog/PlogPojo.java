package com.code.travellog.core.data.pojo.plog;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @date: 2021/3/30
 */
public class PlogPojo implements Serializable {
    public int work_id;
    public int uid;
    public String avatar;
    public String uname;
    public String photo_title;
    public String photo_description;
    public String photo_cover;
    public int photo_height;
    public int photo_width;
    public int cover_height;
    public int cover_width;
    public String create_time ;
    public String update_time;
    public int status ;
    public int w;
    public int h;
    public String status_msg;
    public String result_msg;
}
/**
     {
         "work_id": 5,
         "uid": 10002,
         "uname": "Test01",
         "photo_title": "春",
         "photo_description": "春和日丽",
         "photo_cover": "/media/photos/5/coverq31w.jpg",
         "create_time": "2021-04-03 18:00:00",
         "update_time": "2021-04-03 21:30:40",
         "status": 200,
         "status_msg": "处理完成",
         "result_msg": "/media/photos/5/photo8kxb.jpg"
     },
 **/