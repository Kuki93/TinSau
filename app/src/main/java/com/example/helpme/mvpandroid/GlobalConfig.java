package com.example.helpme.mvpandroid;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.example.helpme.mvpandroid.utils.DensityUtils;

import java.util.Random;

/**
 * @Created by helpme on 2018/1/23.
 * @Description 全局配置
 */
public class GlobalConfig {
    
    public static boolean priority720P;
    
    public static String TU_CHONG_URL;
    
    public static String NEI_HAN_DUAN_ZI_URL;
    
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
    
    public static final String NEI_HAN_DUAN_ZI_720P = "720p";
    
    public static final String NEI_HAN_DUAN_ZI_VIDEO_BASE_URL = "http://iu.snssdk.com/neihan/stream/mix/v1/?";
    
    public static void initImageUrl(Context context) {
        StringBuilder url = new StringBuilder();
        //  String deviceId = deviceId(11);
        String deviceId = getDeviceUniqID(context).substring(0, 11);
        url.append("os_api=").append(Build.VERSION.SDK_INT).append("&device_type=").append(Build.MODEL).append
                ("&device_platform=android&ssmix=a&manifest_version_code=350&dpi=").append(DensityUtils.getDensityDpi
                (context)).append("&channel=tuchong&uuid=").append(deviceId).append("&device_id=").append(deviceId).append(
                "&version_code=350&app_name=tuchong" + "&version_name=3.5.0" + "&openudid" +
                        "=fbea6475d51e4fb8&resolution=").append(DensityUtils.getScreenWidth(context) + '*' + DensityUtils
                .getScreenHeight(context)).append("&os_version=").append(Build.VERSION.RELEASE).append
                ("&ac=wifi&aid=1130").append("&language=zh");
        TU_CHONG_URL = url.toString();
    }
    
    public static void initVideoUrl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("helpme", Context.MODE_PRIVATE);
        priority720P = preferences.getBoolean(NEI_HAN_DUAN_ZI_720P, true);
        StringBuilder url = new StringBuilder();
        String id = getDeviceUniqID(context);
        String userId = id.substring(id.length() - 10, id.length());
        String deviceId = id.substring(0, 11);
        url.append("mpic=1&webp=1&essence=1&message_cursor=-1&double_col_mode=0&iid=").append(userId).append
                ("&device_id=").append(deviceId).append("&screen_width=").append(DensityUtils.getScreenWidth(context)).append
                ("&ac=wifi&channel=360&aid=7&app_name=joke_essay").append("&count=30").append
                ("&version_code=686&version_name=6.8.6&device_platform=android&ssmix=a&device_type=").append(Build.MODEL)
                .append("&device_brand=").append(Build.MANUFACTURER).append("&os_api=").append(Build.VERSION.SDK_INT)
                .append("&os_version=").append(Build.VERSION.RELEASE).append
                ("&uuid=326135942187625&openudid=3dg6s95rhg2a3dg5&manifest_version_code=686&resolution=").append
                (DensityUtils.getScreenWidth(context) + '*' + DensityUtils.getScreenHeight(context)).append
                ("&dpi").append("&update_version_code=6860");
        NEI_HAN_DUAN_ZI_URL = url.toString();
    }
    
    public static String deviceId(int count) {
        StringBuilder sb = new StringBuilder();
        String str = "0123456789";
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            int num = r.nextInt(10);
            sb.append(str.charAt(num));
        }
        return sb.toString();
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

