package com.code.travellog.core.data.pojo.forum;

import java.io.Serializable;
import java.util.Date;

public class PostInShort implements Serializable {  //这里没有获取评论信息，仅文章摘要
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
