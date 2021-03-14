package com.code.travellog.core.data.pojo.album;

import com.code.travellog.core.data.pojo.BasePojo;

import java.util.List;

/**
 * @description: 个人影集列表
 * @date: 2021/3/13
 */
public class AlbumListPojo extends BasePojo {

    public DataBean data ;
    public static class DataBean{
        public int uid ;
        public int movie_num;
        public List<Datalist> movies;
    }
    public static class Datalist{
        public int work_id;
        public String create_time ;
        public String update_time;
        public int status ;
        public String description;
        public String info ;
    }
    /**
    "data": {
        "uid": 1,
                "movie_num": 4,
                "movies": [
        {
            "work_id": 1,
                "create_time": "2021-03-08 21:00:29",
                "update_time": "2021-03-09 20:12:59",
                "status": -2,
                "description": "已过期",
                "info": null
        },
        {
            "work_id": 2,
                "create_time": "2021-03-09 12:45:29",
                "update_time": "2021-03-09 14:51:54",
                "status": 3,
                "description": "处理完成",
                "info": "1c6a1705-21a2-4f72-8551-7da0716eaf31"
        }
        ]
    }
     **/
}
