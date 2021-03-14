package com.code.travellog.core.data.pojo.weather;

import com.code.travellog.core.data.pojo.BasePojo;

import java.util.List;

/**
 * @description:
 * @date: 2021/3/14
 */
public class WeatherPojo extends BasePojo {

    public DataBean data;
    public static class DataBean{
        public String weather;
        public int weather_id;
        public List<String> tags;
        public List<Float> rate;
    }
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
