package com.example.helpme.mvpandroid.contract;

import android.content.Intent;
import android.support.design.widget.Snackbar.Callback;
import android.view.View;

import com.example.helpme.mvp.view.BaseMvpView;
import com.example.helpme.mvpandroid.entity.weather.Value;
import com.example.helpme.mvpandroid.module.weather.WeatherActivity;
import com.example.helpme.mvpandroid.module.weather.WeatherFragment;

import java.util.List;

/**
 * @Created by helpme on 2018/1/23.
 * @Description 契约类来统一管理module中weather模块的view与presenter的所有的接口
 */
public interface WeatherContract {
    
    /**
     * weather activity view interface
     */
     interface WeatherAyView extends BaseMvpView<WeatherActivity> {
        
        void onInitViewpager(List<WeatherFragment> mWeatherFragments, int page);
        
        void onSuccess();
        
        void onFail();
        
        void onUpdateCityTitle(String city, String extra);
        
        void onMoveToActivity(Intent intent, int requestCode);
        
        void onShowSnackbar(String msg, int time, String action, View.OnClickListener
                onClickListener, Callback callback);
    
        void changeWeatherView(int type);
    }
    
    /**
     * fragment activity view interface
     */
    interface WeatherFtView extends BaseMvpView<WeatherActivity> {
        
        void onSetWeatherView(Value weather);
    
        void refreshSun();
    }
    
     interface WeatherModel {
        
        void weather(String url, boolean isLocation,long cityId, WeatherCallBcak callBcak) throws Exception;
        
    }
    
     interface WeatherCallBcak<T> {
        
        void onSuccess(boolean isLocation, long cityId, T weather);
        
        void onFail(boolean isLocation, long cityId, String errorInfo);
    }
    
    
    
}
