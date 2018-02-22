package com.example.helpme.mvpandroid.module.weather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvp.view.AbstractMvpFragment;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.HourWeatherRecyclerAdapter;
import com.example.helpme.mvpandroid.contract.WeatherContract;
import com.example.helpme.mvpandroid.entity.weather.Indexes;
import com.example.helpme.mvpandroid.entity.weather.Realtime;
import com.example.helpme.mvpandroid.entity.weather.Value;
import com.example.helpme.mvpandroid.entity.weather.Weathers;
import com.example.helpme.mvpandroid.utils.TimeUtils;
import com.example.helpme.mvpandroid.widget.refresh.SunriseView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * @Created by helpme on 2018/1/24.
 * @Description City天气详情
 */
@CreatePresenter(WeatherFtPresenter.class)
public class WeatherFragment extends AbstractMvpFragment<WeatherContract.WeatherFtView,
        WeatherFtPresenter, WeatherActivity> implements WeatherContract.WeatherFtView {
    
    @BindView(R.id.scrollview)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.stub_city)
    ViewStub mViewStub;
    
    public static final String TAG = WeatherFragment.class.getSimpleName();
    private static final String EXTRA_POSITION_WEATHER = "extra_position_weather";
    private static final String EXTRA_CITYID_WEATHER = "extra_cityid_weather";
    private MyStubView mStubView = null;
    private View mRootView;
    private int position;
    
    public static WeatherFragment newInstance(int position, long cityId) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_POSITION_WEATHER, position);
        args.putLong(EXTRA_CITYID_WEATHER, cityId);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    public WeatherFragment() {
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(EXTRA_POSITION_WEATHER);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_weather, container, false);
            ButterKnife.bind(this, mRootView);
            mNestedScrollView.setOnScrollChangeListener(getMvpPresenter().mOnScrollChangeListener);
            mActivity.getWeatherValue(position);
        }
        return mRootView;
    }
    
    
    @Override
    public void onSetWeatherView(Value weather) {
        try {
            if (weather == null) return;
            if (mStubView == null) {
                mViewStub.setLayoutResource(R.layout.content_weather_detail);
                mStubView = new MyStubView(mViewStub.inflate());
            }
            Realtime realtime = weather.getRealtime();
            mStubView.textviews.get(0).setText(realtime.getWeather());
            mStubView.textviews.get(1).setText(realtime.getTemp());
            mStubView.textviews.get(2).setText("°");
            mStubView.textviews.get(3).setText(realtime.getwD() + realtime.getsD());
            mStubView.textviews.get(4).setText(realtime.getsD() + "%");
            mStubView.textviews.get(5).setText(weather.getPm25().getAqi() + " " + weather.getPm25().getQuality());
            mStubView.textviews.get(5).setBackgroundResource(R.drawable.shape_corner);
            List<Weathers> weathers = weather.getWeathers();
            mStubView.textviews.get(6).setText("昨天   " + TimeUtils.timeString2Format(weathers.get(6).getDate(), 
                    TimeUtils.DATE2_NO_YEAR_SDF));
            mStubView.textviews.get(7).setText(weathers.get(6).getWeather());
            getMvpPresenter().setIcon(mStubView.textviews.get(7), weathers.get(6).getWeather());
            mStubView.textviews.get(8).setText(weathers.get(6).getTemp_day_c() + "° " + "~" + " " + weathers
                    .get(6).getTemp_night_c() + "°");
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
                    Log.d(TAG, "onSetWeatherView" + weathers.get(i).getDate());
                    mStubView.textviews.get(9 + 3 * i).setText("今天   " + TimeUtils.timeString2Format(weathers.get(i)
                            .getDate(), TimeUtils.DATE2_NO_YEAR_SDF));
                } else if (i == 1)
                    mStubView.textviews.get(9 + 3 * i).setText("明天   " + TimeUtils.timeString2Format(weathers.get(i)
                            .getDate(), TimeUtils.DATE2_NO_YEAR_SDF));
                else
                    mStubView.textviews.get(9 + 3 * i).setText("周" + weathers.get(i).getWeek().charAt(2) + "   " + 
                            TimeUtils
                            .timeString2Format(weathers.get(i).getDate(), TimeUtils.DATE2_NO_YEAR_SDF));
                mStubView.textviews.get(10 + 3 * i).setText(weathers.get(i).getWeather());
                getMvpPresenter().setIcon(mStubView.textviews.get(10 + 3 * i), weathers.get(i).getWeather());
                mStubView.textviews.get(11 + 3 * i).setText(weathers.get(i).getTemp_day_c() + "° " + "~" + " " + 
                        weathers
                        .get(i).getTemp_night_c() + "°");
            }
        
            mStubView.textviews.get(27).setText("全国空气质量排名" + weather.getPm25().getCityrank());
            mStubView.textviews.get(28).setText(weather.getPm25().getSo2());
            mStubView.textviews.get(29).setText(weather.getPm25().getCo());
            mStubView.textviews.get(30).setText(weather.getPm25().getPm10());
            mStubView.textviews.get(31).setText(weather.getPm25().getNo2());
            mStubView.textviews.get(32).setText(weather.getPm25().getO3());
            mStubView.textviews.get(33).setText(weather.getPm25().getPm25());
            mStubView.mSunriseView.setSunTime(weather.getWeathers().get(1).getSun_rise_time(),weather.getWeathers().get
                    (1).getSun_down_time());
        
            List<Indexes> indexes = weather.getIndexes();
            for (int i = 0; i < 6; i++) {
                mStubView.textIndexs.get(3 * i).setText(indexes.get(i).getName());
                mStubView.textIndexs.get(1 + 3 * i).setText("—— " + indexes.get(i).getLevel());
                mStubView.textIndexs.get(2 + 3 * i).setText(indexes.get(i).getContent());
            }
        
            if (mStubView.mRecyclerView.getAdapter() == null) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mStubView.mRecyclerView.setLayoutManager(linearLayoutManager);
                HourWeatherRecyclerAdapter mAdapter = new HourWeatherRecyclerAdapter(R.layout.item_hour_view, weather
                        .getWeatherDetailsInfo().getWeather3HoursDetailsInfos());
                mStubView.mRecyclerView.setAdapter(mAdapter);
            } else {
                HourWeatherRecyclerAdapter mAdapter = (HourWeatherRecyclerAdapter) mStubView.mRecyclerView.getAdapter();
                mAdapter.setNewData(weather.getWeatherDetailsInfo()
                        .getWeather3HoursDetailsInfos());
            }
            int top = mStubView.mRecyclerView.getTop();
            getMvpPresenter().onDoSomething(mStubView.textviews.get(1).getTop() + mStubView.textviews.get(1).getHeight(), top);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void refreshSun() {
        mStubView.mSunriseView.invalidateSunview();
    }
    
    public class MyStubView {
        
        @BindView(R.id.recyclerview)
        RecyclerView mRecyclerView;
        @BindView(R.id.sunview)
        SunriseView mSunriseView;

        
        @BindViews({
                R.id.weatherType, R.id.temperature, R.id.symbol, R.id.wind, R.id.sd, R.id.aqi,
                R.id.date1, R.id.type1, R.id.tempe1, R.id.date2, R.id.type2, R.id.tempe2,
                R.id.date3, R.id.type3, R.id.tempe3, R.id.date4, R.id.type4, R.id.tempe4,
                R.id.date5, R.id.type5, R.id.tempe5, R.id.date6, R.id.type6, R.id.tempe6,
                R.id.date7, R.id.type7, R.id.tempe7, R.id.rank, R.id.so2_value, R.id.co_value, R.id.pm10_value,
                R.id.no2_value, R.id.o3_value, R.id.pm2_5_value})
        List<TextView> textviews;
        
        @BindViews({
                R.id.suggest1, R.id.index1, R.id.tip1,
                R.id.suggest2, R.id.index2, R.id.tip2,
                R.id.suggest3, R.id.index3, R.id.tip3,
                R.id.suggest4, R.id.index4, R.id.tip4,
                R.id.suggest5, R.id.index5, R.id.tip5,
                R.id.suggest6, R.id.index6, R.id.tip6,
        })
        List<TextView> textIndexs;
        
        @BindViews({
                R.id.icon1,
                R.id.icon2,
                R.id.icon3,
                R.id.icon4,
                R.id.icon5,
                R.id.icon6
        })
        List<ImageView> icons;
        
        public MyStubView(View view) {
            ButterKnife.bind(this, view);
        }
    }
    
    public void scrollTo(int scrollY) {
        if (mNestedScrollView != null)
            mNestedScrollView.scrollTo(0, scrollY);
    }
    
    @Override
    public WeatherActivity getParentHost() {
        return mActivity;
    }
    
    
}
