package com.code.travellog.core.data.pojo.picture;

import java.io.Serializable;

public class Color implements Serializable {    //色彩提取
    public Integer k;
    public Float[][] result;
    public Color(int in_k){
        this.k=in_k;
        result=new Float[k][3];
    }
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