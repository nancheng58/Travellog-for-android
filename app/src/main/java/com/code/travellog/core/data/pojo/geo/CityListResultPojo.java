package com.code.travellog.core.data.pojo.geo;

import com.code.travellog.core.data.pojo.BasePojo;

import java.util.List;

/**
 * @description:
 * @date: 2021/3/24
 */
public class CityListResultPojo extends BasePojo {
    public List<DataBean> data;
    public static class DataBean{
        public String province ;
        public String city ;
        public String county ;
        public String image;
    }
}
