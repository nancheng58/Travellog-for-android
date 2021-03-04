package com.code.travellog.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.code.travellog.App;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author：tqzhang  on 18/7/23 11:33
 */
public class NetworkUtils {

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isNetworkAvailable(){
        return isNetworkAvailable(App.instance());
    }
    public static MultipartBody.Part createPartByPathAndKey(String path, String key){
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key,file.getName(),body);
        return part;
    }

}
