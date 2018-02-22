package com.example.helpme.mvpandroid.module.weather;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.contract.WeatherContract;
import com.example.helpme.mvpandroid.entity.weather.AllCityCode;
import com.example.helpme.mvpandroid.entity.weather.CityInfo;
import com.example.helpme.mvpandroid.entity.weather.Value;
import com.example.helpme.mvpandroid.entity.weather.Weather;
import com.example.helpme.mvpandroid.entity.weather.WeatherCityCache;
import com.example.helpme.mvpandroid.utils.CacheUtils;
import com.example.helpme.mvpandroid.utils.ConstUtils;
import com.example.helpme.mvpandroid.utils.FileUtils;
import com.example.helpme.mvpandroid.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/1/23.
 * @Description Weather相关的Persenter
 */
public class WeatherAyPresenter extends BaseMvpPresenter<WeatherActivity, WeatherContract
        .WeatherAyView> implements TencentLocationListener {
    
    public static final String TAG = WeatherAyPresenter.class.getSimpleName();
    private final String ON_SAVE_INSTANCE_STATE = TAG + "onSaveInstanceState";
    
    private List<WeatherFragment> mWeatherFragments;
    // 天气网络请求实现类
    private WeatherModelImpl mWeatherModel;
    // 腾讯定位Manager
    private TencentLocationManager mLocationManager;
    // 当前城市天气管理
    private WeatherCityCache mCityCache;
    // 当前显示的是第几个城市
    private int mCurrentPageIndex;
    // 由于未知原因的定位错误次数
    private int mLocationCount;
    // 最后一次更新时间
    private String lastDate = null;
    
    private boolean hasLocationPermission = true;
    
    public boolean isOpenLocation() {
        return mCityCache.isOpenLocation();
    }
    
    public Value getCurrentValue(int index) {
        index = index >= 0 ? index : mCurrentPageIndex;
        return mCityCache.getCityValues().get(index);
    }
    
    
    public WeatherAyPresenter() {
        mWeatherModel = new WeatherModelImpl();
    }
    
    
    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            
        }
        
        @Override
        public void onPageSelected(int position) {
            mWeatherFragments.get(mCurrentPageIndex).scrollTo(0);
            mCurrentPageIndex = position;
            if (mCityCache.getCityValues().get(mCurrentPageIndex).getRealtime() == null) {
                getMvpView().changeWeatherView(3);
                getMvpView().onUpdateCityTitle(mCityCache.getCityValues().get(mCurrentPageIndex).getCity(),
                        null);
            } else {
                getMvpView().onUpdateCityTitle(mCityCache.getCityValues().get(mCurrentPageIndex).getCity(),
                        mCityCache.getCityValues().get(mCurrentPageIndex).getRealtime().getTemp() + "°");
                if (mCityCache.getCityValues().get(mCurrentPageIndex).getRealtime().getWeather().contains("雪")) {
                    getMvpView().changeWeatherView(1);
                } else if (mCityCache.getCityValues().get(mCurrentPageIndex).getRealtime().getWeather().contains("雨")) {
                    getMvpView().changeWeatherView(2);
                } else {
                    getMvpView().changeWeatherView(3);
                }
            }
            if (lastDate == null || TimeUtils.getIntervalByNow(lastDate, ConstUtils.TimeUnit.SEC) > 900) {
                // 上次刷新时间超过15分钟自动刷新
                weather(true);
            }
        }
        
        @Override
        public void onPageScrollStateChanged(int state) {
            
        }
    };
    
    public void firstLoadWeather() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCurrentPageIndex = 0;
                String json = CacheUtils.get(mActivity).getAsString(GlobalConfig.CACHE_WEATHER_KEY);
                if (json == null) {
                    mCityCache = new WeatherCityCache(true);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getMvpView().onMoveToActivity(new Intent(mActivity,
                                    CityManagementActivity.class), WeatherActivity.ManageRequestCode);
                        }
                    });
                } else {
                    mCityCache = new Gson().fromJson(json, WeatherCityCache.class);
                    if (mCityCache == null) {
                        mCityCache = new WeatherCityCache(true);
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getMvpView().onMoveToActivity(new Intent(mActivity,
                                        CityManagementActivity.class), WeatherActivity.ManageRequestCode);
                            }
                        });
                    } else {
                        List<Value> values = mCityCache.getCityValues();
                        if (values == null) {
                            values = new ArrayList<>();
                            mCityCache.setCityValues(values);
                        }
                        int size = values.size();
                        if (size == 0) {
                            moveToCityManager();
                        } else {
                            mWeatherFragments = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                mWeatherFragments.add(WeatherFragment.newInstance(i, values.get(i).getCityid()));
                            }
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    weather(true);
                                    getMvpView().onInitViewpager(mWeatherFragments, mCurrentPageIndex);
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }
    
    public void restoreStatus() {
        List<Fragment> listFragments = mActivity.getSupportFragmentManager().getFragments();
        int size = listFragments.size();
        mWeatherFragments = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            mWeatherFragments.add((WeatherFragment) listFragments.get(i));
        }
        getMvpView().onInitViewpager(mWeatherFragments, mCurrentPageIndex);
    }
    
    public void addCity(CityInfo cityInfo) {
        mCityCache.getCityValues().add(new Value(cityInfo.getCity(), cityInfo.getCityId()));
        mWeatherFragments.add(WeatherFragment.newInstance(mWeatherFragments.size(), cityInfo.getCityId()));
        //     getMvpView().onInitViewpager(mWeatherFragments, mWeatherFragments.size() - 1);
        weather(GlobalConfig.MEIZU_WEATHER_URL + cityInfo.getCityId(), false);
        getMvpView().onInitViewpager(null, mWeatherFragments.size() - 1);
    }
    
    
    public void refreshWeather(WeatherCityCache weatherCityCache) {
        boolean refresh = false, change = false;
        int size = weatherCityCache.getCityValues().size();
        if (mCityCache.getCityValues() == null || size != mCityCache.getCityValues().size()) {
            change = true;
        } else {
            for (int i = 0; i < size; i++) {
                if (!change && mCityCache.getCityValues().get(i).getCityid() != weatherCityCache.getCityValues().get
                        (i).getCityid())
                    change = true;
                if (weatherCityCache.getCityValues().get(i).getRealtime() == null)
                    refresh = true;
                if (change && refresh)
                    break;
            }
        }
        mCityCache = weatherCityCache;
        if (change) {
            lastDate = TimeUtils.getCurTimeString();
            List<Value> mCityInfo = mCityCache.getCityValues();
            if (mWeatherFragments == null)
                mWeatherFragments = new ArrayList<>();
            else
                mWeatherFragments.clear();
            size = mCityInfo.size();
            mCurrentPageIndex = size - 1;
            for (int i = 0; i < size; i++) {
                mWeatherFragments.add(WeatherFragment.newInstance(i, mCityInfo.get(i).getCityid()));
            }
            getMvpView().onInitViewpager(mWeatherFragments, mCurrentPageIndex);
        }
        if (refresh) {
            weather(false);
        }
    }
    
    public void moveToCityManager() {
        Intent intent = new Intent(mActivity, CityManagementActivity.class);
        intent.putExtra(GlobalConfig.CITY_MANAGE_KEY, mCityCache);
        getMvpView().onMoveToActivity(intent, WeatherActivity.ManageRequestCode);
    }
    
    public void getWeatherValue(int position) {
        Value value = mCityCache.getCityValues().get(position);
        if (value.getRealtime() == null)
            onSetWeatherView(position, null);
        else
            onSetWeatherView(position, mCityCache.getCityValues().get(position));
    }
    
    
    private void onSetWeatherView(int position, Value weatherValue) {
        mWeatherFragments.get(position).onSetWeatherView(weatherValue);
    }
    
    private void onSetWeatherView(List<Value> weatherValues) {
        int size = weatherValues.size();
        for (int i = 0; i < size; i++) {
            onSetWeatherView(i, weatherValues.get(i));
        }
    }
    
    /**
     * 获取所有城市的天气
     */
    public void weather(boolean isNeedLocation) {
        StringBuilder url = new StringBuilder(GlobalConfig.MEIZU_WEATHER_URL);
        List<Value> values = mCityCache.getCityValues();
        for (Value value : values) {
            url.append(value.getCityid());
            url.append(',');
        }
        url.deleteCharAt(url.length() - 1);
        if (isOpenLocation() && isNeedLocation)
            getLocationInfo();
            //      WeatherActivityPermissionsDispatcher.getLocationInfoWithPermissionCheck(mActivity);
        else
            hasLocationPermission = true;
        weather(url.toString(), false);
    }
    
    /**
     * 获取实时天气信息
     *
     * @param url
     * @param isLocation 是否是定位回调调用的，true表示是，false表示不是
     */
    private void weather(String url, boolean isLocation) {
        try {
            mWeatherModel.weather(url, isLocation, 0, new WeatherContract.WeatherCallBcak<Weather>() {
                @Override
                public void onSuccess(final boolean isLocation, long cityId, final Weather weather) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weather != null && weather.getCode().equals("200")) {
                                getMvpView().onSuccess();
                                List<Value> values = weather.getValue();
                                if (isLocation) {
                                    onSetWeatherView(0, values.get(0));
                                    mCityCache.getCityValues().set(0, values.get(0));
                                } else {
                                    lastDate = TimeUtils.getCurTimeString();
                                    int size = values.size();
                                    if (size == 1) {
                                        for (int i = 0; i < mCityCache.getCityValues().size(); i++) {
                                            if (mCityCache.getCityValues().get(i).getCityid() == values.get(0)
                                                    .getCityid()) {
                                                onSetWeatherView(i, values.get(0));
                                                mCityCache.getCityValues().set(i, values.get(0));
                                            }
                                        }
                                    } else {
                                        if (values.get(0).getCity().equals(mCityCache.getCityValues().get(0).getCity
                                                ())) {
                                            onSetWeatherView(values);
                                            mCityCache.setCityValues(values);
                                        } else {
                                            for (int i = 1; i < size; i++) {
                                                onSetWeatherView(i, values.get(i));
                                                mCityCache.getCityValues().set(i, values.get(i));
                                            }
                                        }
                                    }
                                }
                                if (mCityCache.getCityValues().get(mCurrentPageIndex).getRealtime() != null) {
                                    if (mCityCache.getCityValues().get(mCurrentPageIndex).getRealtime().getWeather()
                                            .contains("雪")) {
                                        getMvpView().changeWeatherView(1);
                                    } else if (mCityCache.getCityValues().get(mCurrentPageIndex).getRealtime()
                                            .getWeather().contains("雨")) {
                                        getMvpView().changeWeatherView(2);
                                    } else {
                                        getMvpView().changeWeatherView(3);
                                    }
                                } else {
                                    getMvpView().changeWeatherView(3);
                                }
                            } else {
                                getMvpView().onFail();
                            }
                        }
                    });
                }
                
                @Override
                public void onFail(boolean isLocation, long cityId, final String errorInfo) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getMvpView().onFail();
                        }
                    });
                }
            });
        } catch (Exception e) {
            getMvpView().onFail();
            e.printStackTrace();
            Log.e(TAG, "firstLoadWeather" + "获取天气信息请求出错");
        }
    }
    
    /**
     * 获取定位信息
     */
    public void getLocationInfo() {
        mLocationCount = 0;
        mLocationManager = TencentLocationManager.getInstance(mActivity);
        TencentLocationRequest request = TencentLocationRequest.create();
        //允许缓存
        request.setAllowCache(true);
        // 允许开启Gps
        request.setAllowGPS(true);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        // 请求周期ms
        request.setInterval(800);
        mLocationManager.requestLocationUpdates(request, this);
    }
    
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
        if (TencentLocation.ERROR_OK == error) {
            // 定位成功
            mLocationManager.removeUpdates(this);
            List<AllCityCode> mAllCityCodes = new Gson().fromJson(FileUtils.getJson("meizu_city.json",
                    mActivity), new TypeToken<List<AllCityCode>>() {
            }.getType());
            String cityName = null;
            long cityId = 0;
            boolean find = false;
            for (AllCityCode city : mAllCityCodes) {
                if (!find && tencentLocation.getCity().contains(city.getCountyname())) {
                    cityId = city.getAreaid();
                    if (tencentLocation.getDistrict().contains(city.getCountyname())) {
                        cityName = city.getCountyname();
                        break;
                    }
                    cityName = city.getCountyname();
                    find = true;
                    continue;
                }
                if (find && tencentLocation.getDistrict().contains(city.getCountyname())) {
                    cityId = city.getAreaid();
                    cityName = city.getCountyname();
                    break;
                }
            }
            if (find || cityName != null) {
                if (cityId != mCityCache.getCityValues().get(0).getCityid())
                    weather(GlobalConfig.MEIZU_WEATHER_URL + cityId, true);
            } else {  // 没有该地区的天气信息
                getMvpView().onFail();
                mActivity.onShowSnackbar("抱歉不支持该地区（" + tencentLocation.getCity() + tencentLocation.getDistrict() +
                        "）的天气！", Snackbar.LENGTH_LONG, "地区有误？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<AllCityCode> cityCodes = new ArrayList<>();
                        for (Value value : mCityCache.getCityValues()) {
                            cityCodes.add(new AllCityCode(value.getCityid(), value.getCity()));
                        }
                        Intent intent = new Intent(mActivity, CityPickerActivity.class);
                        intent.putParcelableArrayListExtra(GlobalConfig.CITY_ADDKEY, cityCodes);
                        getMvpView().onMoveToActivity(intent, WeatherActivity.PickerRequestCode);
                    }
                }, null);
            }
        } else {
            // 定位失败
            Log.d(TAG, "onLocationChanged  " + error + "  " + s);
            switch (error) {
                case 1:
                    getMvpView().onFail();
                    mLocationManager.removeUpdates(this);
                    mActivity.onShowSnackbar("网络不通畅请检查网络！", Snackbar
                            .LENGTH_LONG, "检查", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            mActivity.onMoveToActivity(intent, 0);
                        }
                    }, null);
                    break;
                case 2:
                    getMvpView().onFail();
                    mLocationManager.removeUpdates(this);
                    mActivity.onShowSnackbar("定位失败，请检测定位权限是否打开！", Snackbar
                                    .LENGTH_LONG, "检查",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    mActivity.onMoveToActivity(intent, 0);
                                }
                            }, null);
                    break;
                case 4:
                case 404:
                    mLocationCount++;
                    if (mLocationCount == 3) {
                        getMvpView().onFail();
                        mLocationManager.removeUpdates(this);
                        Toast.makeText(mActivity, "定位失败", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
            }
        }
    }
    
    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        
    }
    
    @Override
    public void onCreatePersenter(@Nullable Bundle savedState) {
        super.onCreatePersenter(savedState);
        if (savedState != null) {
            mCityCache = savedState.getParcelable(ON_SAVE_INSTANCE_STATE);
            mCurrentPageIndex = savedState.getInt("page");
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", mCurrentPageIndex);
        outState.putParcelable(ON_SAVE_INSTANCE_STATE, mCityCache);
    }
    
    @Override
    public void onStopPersenter() {
        super.onStopPersenter();
        CacheUtils cache = CacheUtils.get(mActivity);
        Gson gson = new Gson();
        cache.put(GlobalConfig.CACHE_WEATHER_KEY, gson.toJson(mCityCache));
    }
    
    @Override
    public void onDestroyPersenter() {
        super.onDestroyPersenter();
        mWeatherModel.cancelAllRequest();
    }
}

