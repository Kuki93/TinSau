package com.example.helpme.mvpandroid;

import android.app.Application;

import com.sumitanantwar.eventbus_poc.MyEventBusIndex;

import org.greenrobot.eventbus.EventBus;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @Created by helpme on 2018/1/23.
 * @Description
 */
public class HelpmeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BGASwipeBackHelper.init(this, null);
        EventBus.builder()
                .addIndex(new MyEventBusIndex())
                .installDefaultEventBus();
    }
}

