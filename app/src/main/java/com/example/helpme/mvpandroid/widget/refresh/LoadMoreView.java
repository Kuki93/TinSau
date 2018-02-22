package com.example.helpme.mvpandroid.widget.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.loading.LoadingIndicatorView;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public class LoadMoreView extends RelativeLayout implements RefreshFooter {
    
    private LoadingIndicatorView mLoadingIndicatorView;
    private TextView mTextView;
    protected boolean mNoMoreData = false;
    
    public LoadMoreView(Context context) {
        this(context, null);
    }
    
    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public LoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
        setGravity(Gravity.CENTER);
        mTextView = new TextView(context);
        mLoadingIndicatorView = new LoadingIndicatorView(context);
        mLoadingIndicatorView.setIndicatorColor(getResources().getColor(R.color.colorYellow));
        addView(mTextView);
        addView(mLoadingIndicatorView);
        setPadding(0, DensityUtils.dip2px(context, 8), 0, DensityUtils.dip2px(context, 8));
    }
    
    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {
        if (!mNoMoreData && offset > footerHeight)
            mLoadingIndicatorView.show();
    }
    
    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {
        if (!mNoMoreData && offset < footerHeight)
            mLoadingIndicatorView.stopAnimation();
    }
    
    @Override
    public void onLoadmoreReleased(RefreshLayout layout, int footerHeight, int extendHeight) {
        
    }
    
    @Override
    public boolean setLoadmoreFinished(boolean finished) {
        return false;
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
        
    }
    
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        mLoadingIndicatorView.stopAnimation();
    }
    
    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {
        if (!mNoMoreData)
            mLoadingIndicatorView.show();
}
    
    @Override
    public int onFinish(@NonNull final RefreshLayout layout, boolean success) {
        if (!mNoMoreData) {
            mLoadingIndicatorView.stopAnimation();
            return 300;
        }
        return 0;
    }
    
    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    public void setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            if (noMoreData) {
                mTextView.setText("没有更多了");
                mTextView.setVisibility(VISIBLE);
                mLoadingIndicatorView.setVisibility(GONE);
            } else {
                mTextView.setText("");
                mTextView.setVisibility(GONE);
                mLoadingIndicatorView.setVisibility(VISIBLE);
            }
        }
    }
    
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
    
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        
    }
}
