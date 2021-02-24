package com.code.travellog.core.data.pojo.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable { //文章类
    public Integer post_id;
    public Integer uid;
    public String uname;
    public Integer gender;  //0 未设置 1 男 2 女
    public String avatar;   //头像链接
    public String title;
    public Integer type;
    public String text;
    public Date time;
    public Integer like_num;

    public Integer comment_num;
    public List<Comment> comments; //评论列表
}
