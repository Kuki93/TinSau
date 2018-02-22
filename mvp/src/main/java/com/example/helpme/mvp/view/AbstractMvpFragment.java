package com.example.helpme.mvp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.helpme.mvp.factory.PresenterMvpFactory;
import com.example.helpme.mvp.factory.PresenterMvpFactoryImpl;
import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvp.proxy.BaseMvpProxy;
import com.example.helpme.mvp.proxy.PresenterProxyInterface;


/**
 * @author helpme
 * @date 2018/1/23
 * @description 继承Fragment的MvpFragment基类
 */
public class AbstractMvpFragment<V extends BaseMvpView<T>, P extends BaseMvpPresenter<T, V>, T extends
        AppCompatActivity> extends Fragment implements PresenterProxyInterface<V, P> {
    
    /**
     * 调用onSaveInstanceState时存入Bundle的key
     */
    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private BaseMvpProxy<V, P> mProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V,
            P>createFactory(getClass()));
    
    protected T mActivity;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (T) getActivity();
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mProxy.onResume((V) this);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        mProxy.onStop();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mProxy.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_SAVE_KEY, mProxy.onSaveInstanceState());
    }
    
    
    /**
     * 可以实现自己PresenterMvpFactory工厂
     *
     * @param presenterFactory PresenterFactory类型
     */
    @Override
    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
        mProxy.setPresenterFactory(presenterFactory);
    }
    
    
    /**
     * 获取创建Presenter的工厂
     *
     * @return PresenterMvpFactory类型
     */
    @Override
    public PresenterMvpFactory<V, P> getPresenterFactory() {
        return mProxy.getPresenterFactory();
    }
    
    /**
     * 获取Presenter
     *
     * @return P
     */
    @Override
    public P getMvpPresenter() {
        P mPresenter = mProxy.getMvpPresenter();
        mProxy.onAttachView((V) this);
        return mPresenter;
    }
    
}