package com.code.travellog.core.data.pojo.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Page implements Serializable{ //页面类
    public class PostInPage {   //这里没有获取评论信息，仅文章摘要
        public Integer post_id;
        public Integer uid;
        public String uname;
        public Integer gender;
        public String avatar;
        public String title;
        public Integer type;
        public String text;
        public Date time;
        public Integer like_num;
    }

    public Integer page_num; //当前页数
    public Integer post_num; //当前页中文章数
    public List<PostInPage> posts; //文章列表
}
