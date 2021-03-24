package com.code.travellog.core.data.pojo.extraction;

import com.code.travellog.core.data.pojo.BasePojo;

import java.io.Serializable;
import java.util.List;

public class ColorPojo extends BasePojo {    //色彩提取

    public DataBean data;
    public static class DataBean{
        int k;
        public List<List<Float>> result ;
    }
//    public static class ColorData {
//        public List<Float> TODO;
//    }

    /*
    {
        "code": 200,
        "msg": "获取成功",
        "data": {
            "k" : 3,
            "result" :
                [
                    32.281213191990574,
                    92.47355712603063,
                    87.32547114252061
                ],
                [
                    245.56026363473447,
                    185.5199219450031,
                    172.16564783656239
                ],
                [
                    220.0655982521882,
                    102.63987333895656,
                    64.77920048673239
                ]
        }
    }
     */
}
