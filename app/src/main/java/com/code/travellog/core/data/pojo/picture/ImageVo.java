package com.code.travellog.core.data.pojo.picture;

import com.code.travellog.core.data.pojo.BasePojo;

import java.util.List;

/**
 * @author: 14407
 * @date: 2021/2/25
 */
public class ImageVo extends BasePojo {

    public List<DataBean> data ;
    public static class DataBean{
        public int index;
        public String img;
        public int code ;
    }

}
