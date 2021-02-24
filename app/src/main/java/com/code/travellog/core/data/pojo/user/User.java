package com.code.travellog.core.data.pojo.user;

import java.io.Serializable;

public class User implements Serializable {
    public Integer uid;
    public String uname;
    public String phone;
    public String email;
    public Integer gender;
    public String intro;
    public String avatar;
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
