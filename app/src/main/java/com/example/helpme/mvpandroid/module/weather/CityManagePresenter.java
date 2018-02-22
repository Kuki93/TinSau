package com.example.helpme.mvpandroid.module.weather;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.CityRecyclerAdapter;
import com.example.helpme.mvpandroid.contract.CityContract;
import com.example.helpme.mvpandroid.contract.WeatherContract;
import com.example.helpme.mvpandroid.entity.weather.AllCityCode;
import com.example.helpme.mvpandroid.entity.weather.CityInfo;
import com.example.helpme.mvpandroid.entity.weather.Value;
import com.example.helpme.mvpandroid.entity.weather.Weather;
import com.example.helpme.mvpandroid.entity.weather.WeatherCityCache;
import com.example.helpme.mvpandroid.utils.CacheUtils;
import com.example.helpme.mvpandroid.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Created by helpme on 2018/1/27.
 * @Description 城市管理management
 */
public class CityManagePresenter extends BaseMvpPresenter<CityManagementActivity, CityContract.CityView> implements
        TencentLocationListener, CityRecyclerAdapter.OnDataChangeListener {
    
    public static final String TAG = CityManagePresenter.class.getSimpleName();
    
    private final String ON_SAVE_INSTANCE_STATE = TAG + "onSaveInstanceState";
    
    private CityRecyclerAdapter mAdapter;
    
    private WeatherCityCache mCityCache;
    
    private WeatherModelImpl mWeatherModel;
    
    private TencentLocationManager mLocationManager;
    private boolean mIsLocationing = false;
    private int mLocationCount;
    
    public WeatherCityCache getCityCache() {
        return mCityCache;
    }
    
    public CityManagePresenter() {
        mWeatherModel = new WeatherModelImpl();
    }
    
    public void initRecycleView(WeatherCityCache weatherCityCache) {
        mCityCache = weatherCityCache;
        List<CityInfo> cityInfos = new ArrayList<>();
        if (mCityCache == null) { //自动定位中
            mCityCache = new WeatherCityCache(true);
            cityInfos.add(new CityInfo("定位中..", CityInfo.TYPE_LOCATION_ON));
            getLocationInfo();
        } else {
            List<Value> values = mCityCache.getCityValues();
            if (values == null) {
                values = new ArrayList<>();
                mCityCache.setCityValues(values);
            }
            int size = values.size();
            if (size == 0) {
                if (mCityCache.isOpenLocation())  //自动定位中
                {
                    cityInfos.add(new CityInfo("定位中..", CityInfo.TYPE_LOCATION_ON));
                    getLocationInfo();
                } else {
                    cityInfos.add(new CityInfo("自动定位", CityInfo.TYPE_LOCATION_OFF));
                }
            } else {
                for (int i = 0; i < size; i++) {
                    CityInfo cityInfo;
                    if (i == 0) {
                        if (mCityCache.isOpenLocation()) {
                            cityInfo = new CityInfo(CityInfo.TYPE_LOCATION_ON, values.get(i).getCity(), values.get(i)
                                    .getCityid(), values.get(i).getRealtime().getWeather(), values.get(i).getRealtime
                                    ().getTemp());
                        } else {
                            cityInfos.add(new CityInfo("自动定位", CityInfo.TYPE_LOCATION_OFF));
                            cityInfo = new CityInfo(values.get(i).getCity(), values.get(i).getCityid(), values.get(i)
                                    .getRealtime()
                                    .getWeather(), values.get(i).getRealtime().getTemp());
                        }
                    } else {
                        cityInfo = new CityInfo(values.get(i).getCity(), values.get(i).getCityid(), values.get(i)
                                .getRealtime()
                                .getWeather(), values.get(i).getRealtime().getTemp());
                    }
                    cityInfos.add(cityInfo);
                }
            }
        }
        mAdapter = new CityRecyclerAdapter(cityInfos);
        mAdapter.setOnDataChangeListener(this);
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.dragview, false);
        mAdapter.setOnItemDragListener(onItemDragListener);
        getMvpView().onNotificationList(mAdapter, itemTouchHelper);
    }
    
    
    /**
     * 定位
     */
    public void getLocationInfo() {
        mIsLocationing = true;
        mLocationCount = 0;
        mLocationManager = TencentLocationManager.getInstance(mActivity);
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setAllowCache(true);
        request.setAllowGPS(true);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        request.setInterval(800);
        mLocationManager.requestLocationUpdates(request, this);
    }
    
    
    private OnItemDragListener onItemDragListener = new OnItemDragListener() {
        
        int startPos;
        
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            startPos = pos;
        }
        
        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView
                .ViewHolder target, int to) {
            
        }
        
        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            Collections.swap(mCityCache.getCityValues(), startPos, pos);
        }
    };
    
    @Override
    public void onWeather(long cityId) {
        weather(cityId, GlobalConfig.MEIZU_WEATHER_URL + cityId, false);
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == mCityCache.isOpenLocation())
            return;
        mCityCache.setOpenLocation(isChecked);
        if (isChecked) {
            if (mIsLocationing)
                return;
            mAdapter.getData().get(0).setType(CityInfo.TYPE_LOCATION_ON);
            mAdapter.getData().get(0).setCity("定位中..");
            mAdapter.notifyItemChanged(0);
            getLocationInfo();
            //  CityManagementActivityPermissionsDispatcher.getLocationInfoWithPermissionCheck(mActivity);
        } else {
            notifyItemChanged(false, CityInfo.TYPE_LOCATION_OFF, "自动定位", 0);
        }
    }
    
    @Override
    public void ondeleteItem(int index) {
        mCityCache.getCityValues().remove(index - mAdapter.getData().size() + mCityCache.getCityValues().size());
        mAdapter.getData().remove(index);
        mAdapter.notifyItemRemoved(index);
    }
    
    public void addCity(CityInfo cityInfo) {
        int size = mCityCache.getCityValues().size();
        for (int i = 0; i < size; i++) {
            if (mCityCache.getCityValues().get(i).getCityid() == cityInfo.getCityId()) {
                if (mCityCache.isOpenLocation() && mCityCache.getExtraValue() == null && i == 0) {
                    mCityCache.setExtraValue(mCityCache.getCityValues().get(0));
                }
                return;
            }
        }
        mCityCache.getCityValues().add(new Value(cityInfo.getCity(), cityInfo.getCityId()));
        mAdapter.getData().add(cityInfo);
        mAdapter.notifyItemInserted(mAdapter.getData().size());
    }
    
    public void onMoveToActivity() {
        ArrayList<AllCityCode> cityCodes = new ArrayList<>();
        for (Value value : mCityCache.getCityValues()) {
            cityCodes.add(new AllCityCode(value.getCityid(), value.getCity()));
        }
        Intent intent = new Intent(mActivity, CityPickerActivity.class);
        intent.putParcelableArrayListExtra(GlobalConfig.CITY_ADDKEY, cityCodes);
        getMvpView().onMoveToActivity(intent, CityManagementActivity.RequestCode);
    }
    
    public void weather(long cityId, String url, boolean isLocation) {
        
        try {
            mWeatherModel.weather(url, isLocation, cityId, new WeatherContract.WeatherCallBcak<Weather>() {
                @Override
                public void onSuccess(final boolean isLocation, final long cityId, final Weather weather) {
                    if (!mCityCache.isOpenLocation() && isLocation)
                        return;
                    if (weather != null && weather.getCode().equals("200")) {
                        Value values = weather.getValue().get(0);
                        if (isLocation) {
                            mAdapter.getData().get(0).setWeather(values.getRealtime().getWeather());
                            mAdapter.getData().get(0).setTemperature(values.getRealtime().getTemp());
                            mCityCache.getCityValues().set(0, values);
                        } else {
                            for (int i = 1; i < mAdapter.getData().size(); i++) {
                                if (mAdapter.getData().get(i).getCityId() == cityId) {
                                    mAdapter.getData().get(i).setWeather(values.getRealtime().getWeather());
                                    mAdapter.getData().get(i).setTemperature(values.getRealtime().getTemp());
                                    if (mCityCache.isOpenLocation())
                                        mCityCache.getCityValues().set(i, values);
                                    else
                                        mCityCache.getCityValues().set(i - 1, values);
                                    break;
                                }
                            }
                        }
                    } else {
                        if (isLocation) {
                            mAdapter.getData().get(0).setWeather("暂无");
                        } else {
                            for (int i = 1; i < mAdapter.getData().size(); i++) {
                                if (mAdapter.getData().get(i).getCityId() == cityId) {
                                    mAdapter.getData().get(i).setWeather("暂无");
                                    break;
                                }
                            }
                        }
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
                
                @Override
                public void onFail(boolean isLocation, long cityId, final String errorInfo) {
                    if (!mCityCache.isOpenLocation() && isLocation)
                        return;
                    if (isLocation) {
                        mAdapter.getData().get(0).setWeather("暂无");
                    } else {
                        for (int i = 1; i < mAdapter.getData().size(); i++) {
                            if (mAdapter.getData().get(i).getCityId() == cityId) {
                                mAdapter.getData().get(i).setWeather("暂无");
                                break;
                            }
                        }
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } catch (Exception e) {
            if (!mCityCache.isOpenLocation() && isLocation)
                return;
            if (isLocation) {
                mAdapter.getData().get(0).setWeather("暂无");
            } else {
                for (int i = 1; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getCityId() == cityId) {
                        mAdapter.getData().get(i).setWeather("暂无");
                        break;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            e.printStackTrace();
            Log.e(TAG, "firstLoadWeather" + "获取天气信息请求出错");
        }
    }
    
    /**
     * 刷新头部
     *
     * @param isOpen
     * @param type
     * @param cityName
     * @param cityId
     */
    private void notifyItemChanged(boolean isOpen, int type, String cityName, long cityId) {
        if (!mCityCache.isOpenLocation() && isOpen)
            return;
        mCityCache.setOpenLocation(isOpen);
        List<Value> values = mCityCache.getCityValues();
        List<CityInfo> cityInfos = mAdapter.getData();
        if (isOpen) {
            int index = 0;
            for (int i = 1; i < cityInfos.size(); i++) {
                if (cityInfos.get(i).getCityId() == cityId) {
                    index = i;
                    break;
                }
            }
            if (index != 0) {  //
                cityInfos.remove(index);
                mAdapter.notifyItemRangeRemoved(index, 1);
                Value value = values.get(index - 1);
                mCityCache.setExtraValue(value);
                values.remove(value);
                values.add(0, value);
            } else {
                if (values == null) {
                    values = new ArrayList<>();
                    mCityCache.setCityValues(values);
                }
                values.add(0, new Value(cityName, cityId));
            }
        } else {
            if (cityInfos.get(0).getCityId() != cityId) {
                values.remove(0);
                if (mCityCache.getExtraValue() != null && cityInfos.get(0).getCityId() == mCityCache.getExtraValue()
                        .getCityid()) {
                    values.add(mCityCache.getExtraValue());
                    if (mCityCache.getExtraValue().getRealtime() == null) {
                        cityInfos.add(new CityInfo(mCityCache.getExtraValue().getCity(), mCityCache.getExtraValue()
                                .getCityid()));
                    } else {
                        cityInfos.add(new CityInfo(mCityCache.getExtraValue().getCity(), mCityCache.getExtraValue()
                                .getCityid(), mCityCache.getExtraValue().getRealtime().getWeather(), mCityCache
                                .getExtraValue().getRealtime().getTemp()));
                    }
                    mCityCache.setExtraValue(null);
                }
            }
        }
        cityInfos.get(0).setType(type);
        cityInfos.get(0).setCity(cityName);
        cityInfos.get(0).setCityId(cityId);
        mAdapter.notifyItemRangeChanged(0, 1);
    }
    
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
        if (!mCityCache.isOpenLocation()) {
            mIsLocationing = false;
            return;
        }
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
                Toast.makeText(mActivity, "定位成功：" + tencentLocation.getProvince() + tencentLocation.getCity() +
                        tencentLocation.getDistrict(), Toast.LENGTH_SHORT).show();
                notifyItemChanged(true, CityInfo.TYPE_LOCATION_ON, cityName, cityId);
                weather(cityId, GlobalConfig.MEIZU_WEATHER_URL + cityId, true);
            } else {  // 没有该地区的天气信息
                notifyItemChanged(false, CityInfo.TYPE_LOCATION_OFF, "自动定位", 0);
                mActivity.onShowSnackbar("抱歉不支持该地区（" + tencentLocation.getCity() + tencentLocation.getDistrict() +
                                "）的查询！",
                        Snackbar.LENGTH_LONG, "地区有误？", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onMoveToActivity();
                            }
                        }, null);
            }
        } else {
            // 定位失败
            Log.d(TAG, "onLocationChanged  " + error + "  " + s);
            switch (error) {
                case 1:
                    mLocationManager.removeUpdates(this);
                    notifyItemChanged(false, CityInfo.TYPE_LOCATION_OFF, "自动定位", 0);
                    mActivity.onShowSnackbar("网络不通畅请检查网络！", Snackbar
                            .LENGTH_LONG, "检查", new View
                            .OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            mActivity.onMoveToActivity(intent, 0);
                        }
                    }, null);
                    break;
                case 2:
                    mLocationManager.removeUpdates(this);
                    notifyItemChanged(false, CityInfo.TYPE_LOCATION_OFF, "自动定位", 0);
                    mActivity.onShowSnackbar("定位失败，请检测定位权限是否打开！", Snackbar
                                    .LENGTH_LONG, "检查",
                            new View
                                    .OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    mActivity.onMoveToActivity(intent, 0);
                                }
                            }, null);
                    break;
                case 4:
                case 404:
                    mLocationCount++;
                    if (mLocationCount == 3) {
                        mLocationManager.removeUpdates(this);
                        notifyItemChanged(false, CityInfo.TYPE_LOCATION_OFF, "自动定位", 0);
                        //  定位失败
                        Toast.makeText(mActivity, "定位失败", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
            }
        }
        mIsLocationing = false;
    }
    
    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        
    }
    
    @Override
    public void onCreatePersenter(@Nullable Bundle savedState) {
        super.onCreatePersenter(savedState);
        if (savedState != null) {
            mCityCache = savedState.getParcelable(ON_SAVE_INSTANCE_STATE);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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