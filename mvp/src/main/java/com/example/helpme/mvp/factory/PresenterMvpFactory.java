package com.example.helpme.mvp.factory;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvp.view.BaseMvpView;

/**
 * @author helpme
 * @date 2018/1/23
 * @description Persenter工厂接口
 */
public interface PresenterMvpFactory<V extends BaseMvpView, P extends BaseMvpPresenter<?, V>> {
    
    /**
     * 创建Presenter的接口方法
     *
     * @return 需要创建的Presenter
     */
    P createMvpPresenter();
}
