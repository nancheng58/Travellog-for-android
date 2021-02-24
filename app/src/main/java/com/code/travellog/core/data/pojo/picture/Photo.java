package com.code.travellog.core.data.pojo.picture;

import java.io.Serializable;
import java.util.Date;

public class Photo implements Serializable {    //图片类
    public Integer photo_id;
    public Integer collection_id;
    public Integer uid;
    public Date time;
    public String photo_url;
    public String weather;
    public Integer text_num;
    public String color;
    public Float mass_weight;
}
