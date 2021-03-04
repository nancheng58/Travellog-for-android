package com.code.travellog.core.data.pojo.file;

import com.tencent.mmkv.MMKV;

/**
 * @author: 14407
 * @date: 2021/3/4
 */
public class FileBaseVo {
    public String code ="travel";
    public int uid = MMKV.defaultMMKV().decodeInt("uid");
}
