package com.example.helpme.mvpandroid.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvp.view.AbstractMvpAppCompatActivity;
import com.example.helpme.mvp.view.AbstractMvpFragment;
import com.example.helpme.mvp.view.BaseMvpView;

/**
 * @Created by helpme on 2018/2/19.
 * @Description 延迟加载fragment
 */
public abstract class LazyMvpFragment<V extends BaseMvpView<T>, P extends BaseMvpPresenter<T, V>, T extends
        AppCompatActivity> extends AbstractMvpFragment<V, P, T> {
    
    private boolean isInit;
    private boolean isLoaded;
    protected View rootView;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInit = false;
        isLoaded = false;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
            onInitViewAndData(rootView);
            onAddListerers();
            isInit = true;
            if (getUserVisibleHint()) {
                if (!isLoaded) {
                    onLoadDataOnlyOnce();
                    isLoaded = true;
                }
            }
        }
        return rootView;
    }
    
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInit) {
            if (isVisibleToUser) {
                if (isLoaded)
                    onVisibleToUser();
                else {
                    onLoadDataOnlyOnce();
                    isLoaded = true;
                }
            } else {
                onInVisibleToUser();
            }
        }
    }
    
    /**
     * 获取根布局resource id
     *
     * @return layoutId
     */
    protected abstract int getLayoutId();
    
    /**
     * 初始化view以及一些简单的数据
     *
     * @param view 根布局
     */
    protected abstract void onInitViewAndData(View view);
    
    
    protected void onAddListerers() {
    }
    
    ;
    
    /**
     * 第一次对用户可见时执行
     * 注：只在第一次可见执行一次，以后不再执行，除非重新创建
     */
    protected abstract void onLoadDataOnlyOnce();
    
    /**
     * 第一次对用户可见以后的每次可见执行
     * 注：第一次可见只执行onLoadDataOnlyOnce，不执行onVisibleToUser
     * 以后只执行onVisibleToUser
     */
    protected abstract void onVisibleToUser();
    
    /**
     * 对用户不可见时执行，每次都执行
     * 注：fragment实例化的时候会执行一次，不管可不可见
     */
    protected abstract void onInVisibleToUser();
}
