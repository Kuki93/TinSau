package com.example.helpme.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.helpme.mvp.R;
import com.example.helpme.mvp.factory.PresenterMvpFactory;
import com.example.helpme.mvp.factory.PresenterMvpFactoryImpl;
import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvp.proxy.BaseMvpProxy;
import com.example.helpme.mvp.proxy.PresenterProxyInterface;
import com.jaeger.library.StatusBarUtil;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 * @author helpme
 * @date 2018/1/23
 * @description 继承自AppCompatActivity的基类MvpAppCompatActivity
 * 使用代理模式来代理Presenter的创建、销毁、绑定、解绑以及Presenter的状态保存,其实就是管理Presenter的生命周期
 */
public class AbstractMvpAppCompatActivity<V extends BaseMvpView, P extends BaseMvpPresenter<?,V>> 
        extends AppCompatActivity implements PresenterProxyInterface<V, P>, BGASwipeBackHelper.Delegate {
    
    private static final String PRESENTER_SAVE_KEY = "presenter_save_key";
    /**
     * 创建被代理对象,传入默认Presenter的工厂
     */
    private BaseMvpProxy<V, P> mProxy = new BaseMvpProxy<>(PresenterMvpFactoryImpl.<V, P>createFactory(getClass()));
    
    protected BGASwipeBackHelper mSwipeBackHelper;
    
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
         setStatusBar();
    }
    
    protected int getStatusBarColor() {
        return R.color.colorWhite;
    }
    
    protected int getStatusBarAlpha() {
        return 0;
    }
    
    protected void setStatusBar() {
        StatusBarUtil.setColorForSwipeBack(this, getResources().getColor(getStatusBarColor()), getStatusBarAlpha());
        //StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorWhite));
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        Log.e("perfect-mvp", "V onCreate");
        Log.e("perfect-mvp", "V onCreate mProxy = " + mProxy);
        Log.e("perfect-mvp", "V onCreate this = " + this.hashCode());
        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_SAVE_KEY));
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("perfect-mvp", "V onResume");
        mProxy.onResume((V) this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("perfect-mvp", "V onStop");
        mProxy.onStop();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("perfect-mvp", "V onDestroy = " + isChangingConfigurations());
        mProxy.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("perfect-mvp", "V onSaveInstanceState");
        outState.putBundle(PRESENTER_SAVE_KEY, mProxy.onSaveInstanceState());
    }
    
    @Override
    public void setPresenterFactory(PresenterMvpFactory<V, P> presenterFactory) {
        Log.e("perfect-mvp", "V setPresenterFactory");
        mProxy.setPresenterFactory(presenterFactory);
    }
    
    @Override
    public PresenterMvpFactory<V, P> getPresenterFactory() {
        Log.e("perfect-mvp", "V getPresenterFactory");
        return mProxy.getPresenterFactory();
    }
    
    @Override
    public P getMvpPresenter() {
        Log.e("perfect-mvp", "V getMvpPresenter");
        P mPresenter = mProxy.getMvpPresenter();
        mProxy.onAttachView((V) this);
        return mPresenter;
    }
    
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }
    
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }
    
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
        
    }
    
    @Override
    public void onSwipeBackLayoutCancel() {
        
    }
    
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }
    
    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }
}
