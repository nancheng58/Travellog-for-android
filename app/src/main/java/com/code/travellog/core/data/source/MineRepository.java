package com.code.travellog.core.data.source;

import com.code.travellog.core.data.BaseRepository;
import com.code.travellog.util.StringUtil;

/**
 * @description 用户信息获取仓库
 * @date: 2021/2/22
 */
public class MineRepository extends BaseRepository {
    public static String EVENT_KEY_MINE_LIST = null ;
    public MineRepository()
    {
        if(EVENT_KEY_MINE_LIST == null) {
            EVENT_KEY_MINE_LIST = StringUtil.getEventKey();
        }
    }
    public void loadUserInfo(){

    }
    public void UserLoginCheck()
    {

    }
//    public void loadCapture()
//    {
//        addDisposable(apiService);
//    }
}