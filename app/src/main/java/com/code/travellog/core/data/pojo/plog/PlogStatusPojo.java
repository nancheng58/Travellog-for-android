package com.code.travellog.core.data.pojo.plog;

import com.code.travellog.core.data.pojo.BasePojo;

import java.util.List;

/**
 * @description:
 * @date: 2021/3/13
 */
public class PlogStatusPojo extends BasePojo {

    public DataBean data;
    public static class DataBean{
        public int work_id;
        public int uid;
        public String movie_title;
        public String movie_description;
        public String create_time ;
        public String update_time;
        public int status ;
        public String status_msg;
        public String result_msg;
        public int image_num;
        public List<String> image_urls;
    }

    /**
        "data": {
        "work_id": 1,
                "uid": 10018,
                "create_time": "2021-03-09 00:14:29",
                "update_time": "2021-03-09 00:37:06",
                "status": 3,
                "description": "处理完成",
                "info": null,
                "image_num": 2,
                "image_urls": [
        "/media/movies/1/1615221875058025.png",
                "/media/movies/1/1615220094945325.jpg"
        ]
    }
     **/
}
