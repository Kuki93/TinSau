package com.example.helpme.mvpandroid.contract;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.example.helpme.mvp.view.BaseMvpView;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.module.image.CategoryActivity;
import com.xiaopo.flying.puzzle.PuzzlePiece;

import java.util.List;
import java.util.Map;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public interface ImageContract {
    
    interface OnItemPieceSelectedListener {
        void onPieceSelected(PuzzlePiece piece, int position, int itemPosition, Rect rect);
    }
    
    interface OnItemChildClickListener {
        void onPhotoViewClick();
        
        void onScrolling(boolean out);
    }
    
    interface OnDragPhototListener {
        /**
         * 开始下滑时
         */
        void onStartSlide();
    
        /**
         * 滑动过程中
         * @param Alpha
         */
        void onKeepSlideing(int Alpha);
    
        /**
         * 触发退出条件并开始退出动画，并返回目标位置rect
         * @return
         */
        Rect onReleaseExitAnim();
    
        /**
         * 退出的过程中
         * @param Alpha
         */
        void onUpdateExitAnim(int Alpha);
    
        /**
         * 动画结束，执行finish
         * @param rect
         */
        void onEndExitAnim(Rect rect);
    
        /**
         * 是否开启点击退出
         * @return
         */
        boolean onTapExit();
    
        /**
         * 当获取的rect为空时的默认rect
         * @return
         */
        @NonNull
        Rect getDefaultRect();
    
    
        boolean isAllowSwipe();
    }
    
    interface ImageRecommendView extends BaseMvpView<MainActivity> {
        
        void onRefreshView(List<PhotoGroup> mPhotoGroups, boolean refresh, boolean noMore);
        
        void onMoveToActivity(Intent intent);
        
        void onFinishRefresh(boolean success, boolean noMore, int arg);
    }
    
    interface ImageFindView extends BaseMvpView<MainActivity> {
        
        void onRefreshView(Object bean, boolean success);
    
        void onMoveToActivity(Intent intent);
    }
    
    interface ImageCategoryView extends BaseMvpView<CategoryActivity> {
        
        void onRefreshView(List<PhotoGroup> mPhotoGroups, boolean refresh);
        
        void onMoveToActivity(Intent intent);
        
        void onFinishRefresh(boolean success, boolean noMore, int arg);
    }
    
    interface OnBannerClickListener {
        void onClick(int position, String url);
    }
    
    
    interface ImageModel<T> {
        
        void okhttp_get(boolean refresh, int position, Class<T> classOfT, ImageContract.ImageOkHttpCallBcak<T> callBcak, 
                            String... params) throws Exception;
        
        void okhttp_header_get(boolean refresh, int position, Class<T> classOfT, ImageContract.ImageOkHttpCallBcak<T> callBcak,
                               Map<String,String> header, String... params) throws Exception;
    }
    
    interface ImageOkHttpCallBcak<T> {
        
        void onSuccess(T data, boolean refresh, int position);
        
        void onFail(boolean refresh, int position, String errorInfo);
    }
    
}
