package com.code.travellog.core.data.pojo.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Index implements Serializable {
    public Integer post_num; //论坛总文章数，10条文章一页，据此计算总页数
    public List<PostInShort> new_posts; //最新10条文章
    /*
    return_data = {
        'post_num' : postNum,
        'new_posts' : [],
    }

    for i in new_posts:
        return_data['new_posts'].append({
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
