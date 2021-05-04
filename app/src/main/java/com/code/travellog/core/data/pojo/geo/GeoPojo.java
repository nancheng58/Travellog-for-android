package com.code.travellog.core.data.pojo.geo;

import com.code.travellog.core.data.pojo.album.AlbumPostPojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @date: 2021/3/21
 */
public class GeoPojo {

    public HashMap<Long, DataBean> geo;
    public static class DataBean{
        public Long pos ;
        public double lan ;
        public double lng;
        public String province ;
        public String city ;
        public String county ;
        public String imageUrl;
        public ArrayList<String> path ;
    }
}
