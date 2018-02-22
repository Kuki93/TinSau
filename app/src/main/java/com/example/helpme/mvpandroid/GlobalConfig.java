package com.example.helpme.mvpandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.example.helpme.mvpandroid.utils.DensityUtils;

/**
 * @Created by helpme on 2018/1/23.
 * @Description 全局配置
 */
public class GlobalConfig {
    public static String TU_CHONG_URL;
    
    public static final String MEIZU_WEATHER_URL = "http://aider.meizu.com/app/weather/listWeather?cityIds=";
    
    public static final String CACHE_WEATHER_KEY = "cache_weather_key";
    
    public static final String CITY_MANAGE_KEY = "city_managekey";
    
    public static final String CITY_ADDKEY = "city_addkey";
    
    public static final String TU_CHONG_RECOMMEND_BASE_URL = "https://api.tuchong.com/";
    
    public static final String TU_CHONG_RECOMMEND_URL = TU_CHONG_RECOMMEND_BASE_URL + "feed-app?";
    
    public static final String TU_CHONG_FIND_URL = TU_CHONG_RECOMMEND_BASE_URL + "discover-app?";
    
    public static final String TU_CHONG_EVENTS_URL = TU_CHONG_RECOMMEND_BASE_URL + "events?";
    
    public static final String IMAGE_DETAIL = "image_detail";
    
    public static final String IMAGE_CATEGORIES = "image_categories";
    
    public static void initUrl(Context context) {
        StringBuilder url = new StringBuilder();
        String deviceId = getDeviceUniqID(context).substring(0, 11);
        url.append("os_api=").append(Build.VERSION.SDK_INT).append("&device_type=").append(Build.MODEL).append
                ("&device_platform=android&ssmix=a&manifest_version_code=350&dpi=").append(DensityUtils.getDensityDpi
                (context)).append("&channel=tuchong&uuid=" + deviceId + "&device_id=" + deviceId +
                "&version_code=350&app_name=tuchong" + "&version_name=3.5.0" + "&openudid" +
                "=fbea6475d51e4fb8&resolution=").append(DensityUtils.getScreenWidth(context) + '*' + DensityUtils
                .getScreenHeight(context)).append("&os_version=").append(Build.VERSION.RELEASE).append
                ("&ac=wifi&aid=1130").append("&language=zh");
        TU_CHONG_URL = url.toString();
    }
    
    /**
     * 获取设备唯一ID
     *
     * @param context
     * @return
     */
    @NonNull
    public static String getDeviceUniqID(Context context) {
        context = context.getApplicationContext();
        android.telephony.TelephonyManager TelephonyMgr = (android.telephony.TelephonyManager) context.getSystemService
                (Context
                        .TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager
                .PERMISSION_GRANTED) {
            // TODO: Consider calling
            return null;
        }
        String unique_id = TelephonyMgr.getDeviceId();
        if (TextUtils.isEmpty(unique_id)) {
            unique_id = "41783522533";
        }
        return unique_id;
    }
    
}

