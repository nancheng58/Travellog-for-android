package com.code.travellog.core.data.pojo.material;


import java.util.List;

public class MaterialVo {


    public int errno;
    public DataEntity data;

    public static class DataEntity {
        public String totalnum;
        public List<MaterialInfoVo> content;
    }
}
