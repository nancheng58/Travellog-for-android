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

    /*

    return_data = {
        'post_id': post_obj.post_id,
        'uid' : post_obj.uid.uid,
        'uname' : post_obj.uid.uname,
        'gender' : post_obj.uid.gender,
        'avatar' : '/media/'+post_obj.uid.avatar.name,
        'title' : post_obj.title,
        'text' : post_obj.text,
        'type' : post_obj.type,
        'time' : post_obj.time.strftime("%Y-%m-%d %H:%I:%S"),
        'like_num' : post_obj.like_num,
        'comment_num' : comments.count(),
        'comments' : []
    }


    for i in comments:
        return_data['comments'].append({
            'comment_id': i.comment_id,
            'uid' : i.uid.uid,
            'uname' : i.uid.uname,
            'gender' : post_obj.uid.gender,
            'avatar' : '/media/'+i.uid.avatar.name,
            'text' : i.text,
            'time' : i.time.strftime("%Y-%m-%d %H:%I:%S"),
            'like_num' : i.like_num,
        })

     */
}
