package com.example.helpme.mvp.view;


import android.support.v4.app.FragmentActivity;

/**
 * @author  helpme
 * @date  2018/1/23
 * @description  所有View层接口的基类
 */
public interface BaseMvpView<T extends FragmentActivity> {
    T getParentHost();
}
