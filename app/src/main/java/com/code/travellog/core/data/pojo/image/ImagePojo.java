package com.code.travellog.core.data.pojo.image;

import com.code.travellog.core.data.pojo.BasePojo;

import java.util.List;

/**
 * @author: 14407
 * @date: 2021/2/25
 */
public class ImagePojo extends BasePojo {

    public List<DataBean> data ;
    public static class DataBean{
        public int index;
        public String img;
    }

}
