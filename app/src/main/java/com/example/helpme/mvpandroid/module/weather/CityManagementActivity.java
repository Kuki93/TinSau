package com.example.helpme.mvpandroid.module.weather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvp.view.AbstractMvpAppCompatActivity;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.CityRecyclerAdapter;
import com.example.helpme.mvpandroid.contract.CityContract;
import com.example.helpme.mvpandroid.entity.weather.CityInfo;
import com.example.helpme.mvpandroid.entity.weather.WeatherCityCache;
import com.example.helpme.mvpandroid.widget.DividerItemDecoration;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@CreatePresenter(CityManagePresenter.class)
@RuntimePermissions
public class CityManagementActivity extends AbstractMvpAppCompatActivity<CityContract.CityView,
        CityManagePresenter> implements CityContract.CityView {
    
    public static final String TAG = CityManagementActivity.class.getSimpleName();
    
    public static final String RESULT_QUEST = "RESULT_QUEST";
    
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    
    public static final int RequestCode = 102;
    
    @OnClick(R.id.fb_add)
    public void onAddCity(View view) {
        if (getMvpPresenter().getCityCache().getCityValues() != null && getMvpPresenter().getCityCache().getCityValues
                ().size() >= 11) {
            onShowSnackbar("抱歉，最多只能添加11个城市", Snackbar.LENGTH_LONG, null, null, null);
        } else {
            getMvpPresenter().onMoveToActivity();
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_management);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getMvpPresenter().initRecycleView((WeatherCityCache) getIntent().getParcelableExtra(GlobalConfig
                    .CITY_MANAGE_KEY));
        } else {
            getMvpPresenter().initRecycleView(getMvpPresenter().getCityCache());
        }
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case android.R.id.home:
                WeatherCityCache mCityCache = getMvpPresenter().getCityCache();
                if (mCityCache.getCityValues() == null || mCityCache.getCityValues().size() == 0) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("至少选择一个城市")
//                            //点击对话框以外的区域是否让对话框消失
//                            .setCancelable(true)
//                            //设置正面按钮
//                            .setPositiveButton("确定", null).show();
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                }
                Intent intent = new Intent();
                intent.putExtra(RESULT_QUEST, getMvpPresenter().getCityCache());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        WeatherCityCache mCityCache = getMvpPresenter().getCityCache();
        if (mCityCache.getCityValues() == null || mCityCache.getCityValues().size() == 0) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("至少选择一个城市")
//                    //点击对话框以外的区域是否让对话框消失
//                    .setCancelable(true)
//                    //设置正面按钮
//                    .setPositiveButton("确定", null).show();
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(RESULT_QUEST, mCityCache);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestCode == requestCode && resultCode == RESULT_OK) {
            getMvpPresenter().addCity(new CityInfo(data.getStringExtra("cityName"), data.getExtras().getLong
                    ("cityId")));
        }
    }
    
    
    @Override
    public void onNotificationList(CityRecyclerAdapter mAdapter, ItemTouchHelper itemTouchHelper) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, 2.5f));
        mRecyclerView.setAdapter(mAdapter);
    }
    
    @Override
    public void onMoveToActivity(Intent intent, int requestCode) {
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, RequestCode);
    }
    
    @Override
    public void onShowSnackbar(String msg, int time, String action, View.OnClickListener onClickListener, Snackbar
            .Callback callback) {
        if (onClickListener == null)
            Snackbar.make(toolbar, msg, time).addCallback(callback).show();
        else
            Snackbar.make(toolbar, msg, time).setAction(action, onClickListener).addCallback(callback).show();
    }
    
    @Override
    public CityManagementActivity getParentHost() {
        return this;
    }
    
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorWhite));
    }
    
    @Override
    public void onSwipeBackLayoutExecuted() {
        if (getMvpPresenter().getCityCache().getCityValues() == null || getMvpPresenter().getCityCache().getCityValues()
                .size() == 0) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage("至少选择一个城市")
//                            //点击对话框以外的区域是否让对话框消失
//                            .setCancelable(true)
//                            //设置正面按钮
//                            .setPositiveButton("确定", null).show();
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra(RESULT_QUEST, getMvpPresenter().getCityCache());
            setResult(RESULT_OK, intent);
        }
        super.onSwipeBackLayoutExecuted();
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
                //点击对话框以外的区域是否让对话框消失
                .setCancelable(true)
                //设置正面按钮
                .setPositiveButton("知道啦", null).show();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CityManagementActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
