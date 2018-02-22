package com.example.helpme.mvpandroid.contract;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.example.helpme.mvp.view.BaseMvpView;
import com.example.helpme.mvpandroid.entity.image.FeedList;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.module.image.CategoryActivity;
import com.xiaopo.flying.puzzle.PuzzlePiece;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public interface ImageContract {
    
    interface OnItemPieceSelectedListener {
        void onPieceSelected(PuzzlePiece piece, int position, int itemPosition, ArrayList<Rect> rects);
    }
    
    interface OnItemChildClickListener {
        void onPhotoViewClick();
        
        void onScrolling(boolean out);
    }
    
    interface OnDragPhototListener {
        
        @NonNull
        ViewGroup getParentViewGroup();
        
        void onStartSlide();
        
        void onKeepSlideing(int Alpha);
        
        @NonNull
        Rect onReleaseExitAnim();
        
        void onUpdateExitAnim(int Alpha);
        
        void onEndExitAnim();
        
        boolean onTapExit();
    }
    
    interface ImageRecommendView extends BaseMvpView<MainActivity> {
        
        void onRefreshView(List<FeedList> feedLists, boolean refresh);
        
        void onMoveToActivity(Intent intent);
        
        void onFinishRefresh(boolean success, boolean noMore, int arg);
    }
    
    interface ImageFindView extends BaseMvpView<MainActivity> {
        
        void onRefreshView(Object bean, boolean success);
    
        void onMoveToActivity(Intent intent);
    }
    
    interface ImageCategoryView extends BaseMvpView<CategoryActivity> {
        
        void onRefreshView(List<FeedList> feedLists, List<ImageDetails> mData, boolean refresh);
        
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
