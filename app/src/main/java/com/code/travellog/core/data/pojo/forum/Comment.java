package com.code.travellog.core.data.pojo.forum;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {  //评论类
    public Integer comment_id;
    public Integer uid;
    public String uname;
    public Integer gender;
    public String avatar;
    public String text;
    public Date time;
    public Integer like_num;
}
