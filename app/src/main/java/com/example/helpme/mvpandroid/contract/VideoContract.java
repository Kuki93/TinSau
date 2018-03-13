package com.example.helpme.mvpandroid.contract;

import android.content.Intent;

import com.example.helpme.mvp.view.BaseMvpView;
import com.example.helpme.mvpandroid.entity.video.Data;
import com.example.helpme.mvpandroid.module.home.MainActivity;

import java.util.List;
import java.util.Map;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
public interface VideoContract {
    
    interface DVideoView extends BaseMvpView<MainActivity> {
        
        void onRefreshView(List<Data> mDatas, boolean refresh, boolean more);
    
        void onMoveToActivity(Intent intent);
    
        void onFinishRefresh(boolean success, boolean more, int arg);
        
    }
    
    interface DZiView extends BaseMvpView<MainActivity> {
        
    }
    
    interface VideoModel<T> {
        
        void okhttp_get(boolean refresh, int position, Class<T> classOfT, VideoContract.VideoOkHttpCallBcak<T> callBcak,
                        String... params) throws Exception;
        
        void okhttp_header_get(boolean refresh, int position, Class<T> classOfT, VideoContract.VideoOkHttpCallBcak<T> callBcak,
                               Map<String,String> header, String... params) throws Exception;
    }
    
    interface VideoOkHttpCallBcak<T> {
        
        void onSuccess(T data, boolean refresh, int position);
        
        void onFail(boolean refresh, int position, String errorInfo);
    }
    
}
