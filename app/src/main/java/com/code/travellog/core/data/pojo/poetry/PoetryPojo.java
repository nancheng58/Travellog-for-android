package com.code.travellog.core.data.pojo.poetry;

import com.code.travellog.core.data.pojo.BasePojo;

import java.util.List;

/**
 * @description:
 * @date: 2021/3/14
 */
public class PoetryPojo extends BasePojo {

    public DataBean data;
    public static class DataBean{
        public String poem;
        public String keywords;
    }
}
