package com.example.helpme.mvpandroid.module.video;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.contract.VideoContract;
import com.example.helpme.mvpandroid.module.home.MainActivity;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
public class DZiPresenter extends BaseMvpPresenter<MainActivity,VideoContract.DZiView> {
    
    private VideoModelImpl impl;
    
    public DZiPresenter() {
        impl = new VideoModelImpl();
        
    }
    
    public void  getHttpImageInfo(boolean refresh) {
    }
}
