package com.example.helpme.mvpandroid.contract;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.helpme.mvp.view.BaseMvpView;
import com.example.helpme.mvpandroid.adapter.CityRecyclerAdapter;
import com.example.helpme.mvpandroid.module.weather.CityManagementActivity;

/**
 * @Created by helpme on 2018/1/27.
 * @Description
 */
public interface CityContract {
    
     interface CityView extends BaseMvpView<CityManagementActivity> {
        
        void onNotificationList(CityRecyclerAdapter adapter,ItemTouchHelper itemTouchHelper);
        
        void onMoveToActivity(Intent intent, int requestCode);
        
        void onShowSnackbar(String msg, int time, String action, View.OnClickListener
                onClickListener, Snackbar.Callback callback);
    }
}
