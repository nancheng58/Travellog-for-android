package com.code.travellog.core.data.pojo.picture;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {  //天气识别
    public String weather;
    public Integer weather_id;
    public List<String> weather_specises;
    public List<String> rate_list;
    /*
    {
        "code": 200,
        "msg": "获取成功",
        "data": {
            "weather": "sunny",
            "weather_id": 3,
            "tags": [
                "cloudy",
                "rainy",
                "snow",
                "sunny"
            ],
            "rate": [
                -3.381439208984375,
                -0.9108459949493408,
                0.3688693344593048,
                3.5914697647094727
            ]
        }
    }
     */
}
