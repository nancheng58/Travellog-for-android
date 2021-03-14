package com.code.travellog.core.data.pojo.user;

import com.code.travellog.core.data.pojo.BasePojo;

import java.io.Serializable;

public class UserPojo extends BasePojo {

    public DataBean data;
    public static class DataBean{
        public Integer uid;
        public String uname;
        public String phone;
        public String email;
        public Integer gender;
        public String intro;
        public String avatar;
    }

    /*
    return_data = {
        'uid' : user_obj.uid,
        'uname' : user_obj.uname,
        'phone' : user_obj.phone,
        'email' : user_obj.email,
        'gender' : user_obj.gender,
        'intro' : user_obj.intro,
        'avatar' : 'media/'+user_obj.avatar.name
    }
     */
}
