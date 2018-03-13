package com.example.helpme.mvpandroid.module.weather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvp.view.AbstractMvpAppCompatActivity;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.WeatherViewPagerAdapter;
import com.example.helpme.mvpandroid.contract.WeatherContract;
import com.example.helpme.mvpandroid.entity.weather.CityInfo;
import com.example.helpme.mvpandroid.entity.weather.Value;
import com.example.helpme.mvpandroid.entity.weather.WeatherCityCache;
import com.example.helpme.mvpandroid.utils.TimeUtils;
import com.example.helpme.mvpandroid.utils.WeatherPageTransformer;
import com.example.helpme.mvpandroid.widget.FallObject;
import com.example.helpme.mvpandroid.widget.FallingView;
import com.example.helpme.mvpandroid.widget.ViewPageIndicator;
import com.example.helpme.mvpandroid.widget.refresh.WeatherRefreshHeadView;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@CreatePresenter(WeatherAyPresenter.class)
@RuntimePermissions
public class WeatherActivity extends AbstractMvpAppCompatActivity<WeatherContract.WeatherAyView,
        WeatherAyPresenter> implements WeatherContract.WeatherAyView {
    
    @BindView(R.id.city)
    TextView mCity;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    ViewPageIndicator mIndicator;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.weatherBg)
    FallingView mFallingView;
    @BindView(R.id.layout)
    LinearLayout mLayout;
    
    public static final String TAG = WeatherActivity.class.getSimpleName();
    public static final int ManageRequestCode = 100;
    public static final int PickerRequestCode = 101;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle(null);
        selectWapper();
        mViewPager.setPageTransformer(true, new WeatherPageTransformer());
        mViewPager.addOnPageChangeListener(getMvpPresenter().mOnPageChangeListener);
        mSmartRefreshLayout.setRefreshHeader(new WeatherRefreshHeadView(this));
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getMvpPresenter().weather(true);
            }
        });
        if (savedInstanceState == null) {
            getMvpPresenter().firstLoadWeather();
        } else {
            getMvpPresenter().restoreStatus();
        }
    }
    
    @Override
    public void onInitViewpager(List<WeatherFragment> mWeatherFragments, int page) {
        if (mWeatherFragments == null) {
            mViewPager.getAdapter().notifyDataSetChanged();
            mIndicator.notifyDataSetChanged(page);
            mViewPager.setCurrentItem(page);
        } else {
            WeatherViewPagerAdapter viewPagerAdapter = new WeatherViewPagerAdapter(getSupportFragmentManager(),
                    mWeatherFragments);
            mViewPager.setAdapter(viewPagerAdapter);
            mViewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
            if (mIndicator.getViewPager() == null)
                mIndicator.setViewPager(mViewPager);
            else
                mIndicator.notifyDataSetChanged(page);
            mIndicator.setOpen(getMvpPresenter().isOpenLocation());
            mViewPager.setCurrentItem(page);
            mCity.setText(getMvpPresenter().getCurrentValue(page).getCity());
            mToolbar.setTitleTextColor(getResources().getColor(R.color.colorToolbarDefault));
        }
    }
    
    
    @Override
    public void onSuccess() {
        if (mSmartRefreshLayout.isRefreshing())
            mSmartRefreshLayout.finishRefresh(true);
        selectWapper();
    }
    
    @Override
    public void onFail() {
        if (mSmartRefreshLayout.isRefreshing())
            mSmartRefreshLayout.finishRefresh(false);
        selectWapper();
    }
    
    @Override
    public void onUpdateCityTitle(String city, String extra) {
        mCity.setText(city);
        if (extra != null)
            mToolbar.setTitle(city + " " + extra);
        selectWapper();
    }
    
    @Override
    public void onMoveToActivity(Intent intent, int requestCode) {
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }
    
    @Override
    public void onShowSnackbar(String msg, int time, String action, View.OnClickListener onClickListener, Snackbar
            .Callback callback) {
        if (onClickListener == null)
            Snackbar.make(mViewPager, msg, time).addCallback(callback).show();
        else
            Snackbar.make(mViewPager, msg, time).setAction(action, onClickListener).addCallback(callback).show();
    }
    
    @Override
    public void changeWeatherView(int type) {
        switch (type) {
            case 1:
                FallObject.Builder builder1 = new FallObject.Builder(getResources().getDrawable(R.drawable.snowflake));
                builder1.setSpeed(3, true)
                        .setSize(true)
                        .setWind(3, true, true);
                mFallingView.addFallObject(builder1, 66);
                mFallingView.start();
                break;
            case 2:
                FallObject.Builder builder2 = new FallObject.Builder(getResources().getDrawable(R.drawable.snowflake));
                builder2.setSpeed(30, true)
                        .setSize(15, 60, false)
                        .setWind(0, false, false);
                mFallingView.addFallObject(builder2, 80);
                mFallingView.start();
                break;
            default:
                mFallingView.stop();
                break;
        }
        
    }
    
    public void getWeatherValue(int position) {
        getMvpPresenter().getWeatherValue(position);
    }
    
    public void setToolbarColor(int titleColor, int cityColor, boolean isShowTemp) {
        mToolbar.setTitleTextColor(titleColor);
        mCity.setTextColor(cityColor);
        Value value = getMvpPresenter().getCurrentValue(-1);
        if (!isShowTemp) {
            mToolbar.setTitle(value.getCity());
        } else {
            mToolbar.setTitle(value.getCity() + " " + value.getRealtime().getTemp() + "°");
        }
    }
    
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_city_manager:
                getMvpPresenter().moveToCityManager();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectWapper();
        if (requestCode == ManageRequestCode) {
            if (resultCode == RESULT_OK) {
                getMvpPresenter().refreshWeather((WeatherCityCache) data.getParcelableExtra(CityManagementActivity
                        .RESULT_QUEST));
            }else {
                finish();
            }
        } else {
            if (resultCode == RESULT_OK) {
                getMvpPresenter().addCity(new CityInfo(data.getStringExtra("cityName"), data.getExtras().getLong
                        ("cityId")));
            }
        }
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
        selectWapper();
    }
    
    private void selectWapper() {
        if (TimeUtils.isNowBetween("08:00", "14:00")) {
            mLayout.setBackgroundResource(R.drawable.weather_bgb);
        } else if (TimeUtils.isNowBetween("14:00", "20:00")) {
            mLayout.setBackgroundResource(R.drawable.weather_bgc);
        } else if (TimeUtils.isNowBetween("20:00", "24:00") || TimeUtils.isNowBetween("00:00", "02:00")) {
            mLayout.setBackgroundResource(R.drawable.weather_bga);
        } else {
            int random = new Random().nextInt(4);
            if (random == 0)
                mLayout.setBackgroundResource(R.drawable.weather_bgb);
            else if (random == 1)
                mLayout.setBackgroundResource(R.drawable.weather_bgc);
            else
                mLayout.setBackgroundResource(R.drawable.weather_bga);
        }
    }
    
    @Override
    public WeatherActivity getParentHost() {
        return this;
    }
    
    @NeedsPermission(Manifest.permission_group.LOCATION)
    void getLocationInfo() {
        getMvpPresenter().getLocationInfo();
    }
    
    @OnShowRationale(Manifest.permission_group.LOCATION)
    void showUsers(final PermissionRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("无法获取位置信息");
        builder.setMessage("因未被授予定位权限，不能给您带来当地的最新天气，是否去设置中去授予相关权限？");
        builder.setIcon(R.mipmap.ic_launcher_round);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("去授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 如果用户同意去设置：
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                onMoveToActivity(intent, 0);
            }
        }).setNegativeButton("88", null).show();
    }
    
    @OnPermissionDenied(Manifest.permission_group.LOCATION)
    void showDeniedForLocation() {
        onShowSnackbar("未授予定位权限无法获取位置，是否去授予？", Snackbar.LENGTH_LONG,
                "授予", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 如果用户同意去设置：
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        onMoveToActivity(intent, 0);
                        
                    }
                }, null);
    }
    
    @OnNeverAskAgain(Manifest.permission_group.LOCATION)
    void showNeverAskForLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("因未被授予定位权限，很抱歉无法为您提供当地最新天气。如您再次有需要，可以去【设置-权限管理】中授予我们相关权限，谢谢！")
                .setIcon(R.mipmap.ic_launcher_round)
                //点击对话框以外的区域是否让对话框消失
                .setCancelable(true)
                //设置正面按钮
                .setPositiveButton("知道啦", null).show();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WeatherActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}