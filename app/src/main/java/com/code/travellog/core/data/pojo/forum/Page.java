package com.code.travellog.core.data.pojo.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Page implements Serializable{ //页面类
    public Integer page_num; //当前页数
    public Integer post_num; //当前页中文章数
    public List<PostInShort> posts; //文章列表

    /*

    return_data = {
        'page_num' : page_num,
        'post_num' : page_obj.count(),
        'posts' : []
    }
    for i in page_obj:
        return_data['posts'].append({
            'post_id': i.post_id,
            'uid' : i.uid.uid,
            'uname' : i.uid.uname,
            'gender' : i.uid.gender,
            'avatar' : '/media/'+i.uid.avatar.name,
            'title' : i.title,
            'type' : i.type,
            'text' : i.text[:50],
            'time' : i.time.strftime("%Y-%m-%d %H:%I:%S"),
            'like_num' : i.like_num,
        })

     */
}
