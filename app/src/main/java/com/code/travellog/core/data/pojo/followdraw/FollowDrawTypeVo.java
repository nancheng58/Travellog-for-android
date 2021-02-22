package com.code.travellog.core.data.pojo.followdraw;

import com.code.travellog.core.data.pojo.BaseVo;

import java.util.List;

public class FollowDrawTypeVo extends BaseVo {


    public List<DataBean> data;

    public static class DataBean {
        public int maintypeid;
        public String maintypename;
    }
}
