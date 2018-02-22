package com.example.helpme.mvpandroid.widget.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.loading.LineScaleIndicator;
import com.example.helpme.mvpandroid.widget.loading.LoadingIndicatorView;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * @Created by helpme on 2018/2/11.
 * @Description
 */
public class RefreshHeadView extends LinearLayout implements RefreshHeader {
    
    private LoadingIndicatorView mLoadingIndicatorView;
    
    public RefreshHeadView(Context context) {
        this(context, null);
    }
    
    public RefreshHeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public RefreshHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
        setGravity(Gravity.CENTER);
        mLoadingIndicatorView = new LoadingIndicatorView(context);
        mLoadingIndicatorView.setIndicator(new LineScaleIndicator());
        addView(mLoadingIndicatorView);
        setPadding(0, DensityUtils.dip2px(context, 16), 0, DensityUtils.dip2px(context, 16));
    }
    
    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        if (offset > headerHeight * 0.9f) {
            mLoadingIndicatorView.startAnimation();
        } else {
            mLoadingIndicatorView.stopAnimation();
        }
    }
    
    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
        if (offset < headerHeight * 0.9f) {
            mLoadingIndicatorView.stopAnimation();
        }
    }
    
    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
    }
    
    @NonNull
    @Override
    public View getView() {
        return this;
    }
    
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }
    
    @Override
    public void setPrimaryColors(int... colors) {
        
    }
    
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        mLoadingIndicatorView.stopAnimation();
    }
    
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        
    }
    
    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {
        
    }
    
    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        return 500;
    }
    
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
    
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
    }
}
