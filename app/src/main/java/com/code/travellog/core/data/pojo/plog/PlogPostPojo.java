package com.code.travellog.core.data.pojo.plog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @date: 2021/3/11
 */
public class PlogPostPojo implements Serializable {
    public ArrayList<String> images;
    public ArrayList<Data> factors;
    public String title;
    public String description;
    public Boolean share ;
    public double latitude ;
    public double longitude ;
    public static class DataBean{
        public String path;
    }
    public static class Data{
        public List<String> types ;
        public List<Float> values ;
    }
}
