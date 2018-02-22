package com.example.helpme.mvpandroid.module.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvp.view.AbstractMvpAppCompatActivity;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.contract.MainContract;
import com.example.helpme.mvpandroid.module.image.ImageFragment;
import com.example.helpme.mvpandroid.module.news.NewsFragment;
import com.example.helpme.mvpandroid.module.weather.WeatherActivity;
import com.example.helpme.mvpandroid.utils.BottomNavigationViewHelper;
import com.jaeger.library.StatusBarUtil;

@CreatePresenter(MainPresenter.class)
public class MainActivity extends AbstractMvpAppCompatActivity<MainContract.MainView, MainPresenter> implements
        MainContract.MainView {
    
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_new:
                    fragment = fm.findFragmentByTag(FRAGMENT_TAG_NEWS);
                    if (fragment != null) {
                        if (fragment != mCurrentFragment) {
                            if (mCurrentFragment != null)
                                ft.hide(mCurrentFragment);
                            ft.show(fragment);
                            mCurrentFragment = fragment;
                        }
                    } else {
                        if (mCurrentFragment != null)
                            ft.hide(mCurrentFragment);
                        mCurrentFragment = NewsFragment.newInstance(0);
                        ft.add(R.id.content, mCurrentFragment, FRAGMENT_TAG_NEWS);
                    }
                    ft.commit();
                    return true;
                case R.id.navigation_image:
                    fragment = fm.findFragmentByTag(FRAGMENT_TAG_IMAGE);
                    if (fragment != null) {
                        if (fragment != mCurrentFragment) {
                            if (mCurrentFragment != null)
                                ft.hide(mCurrentFragment);
                            ft.show(fragment);
                            mCurrentFragment = fragment;
                        }
                    } else {
                        if (mCurrentFragment != null)
                            ft.hide(mCurrentFragment);
                        mCurrentFragment = ImageFragment.newInstance(1);
                        ft.add(R.id.content, mCurrentFragment, FRAGMENT_TAG_IMAGE);
                    }
                    ft.commit();
                    return true;
                case R.id.navigation_live:
                    startActivity(new Intent(MainActivity.this, WeatherActivity.class));
//                    fragment = fm.findFragmentByTag(FRAGMENT_TAG_LIVE);
//                    if (fragment != null) {
//                if (fragment != mCurrentFragment) {
//                    if (mCurrentFragment != null)
//                        ft.hide(mCurrentFragment);
//                    ft.show(fragment);
//                    mCurrentFragment = fragment;
//                }
//            } else {
//                if (mCurrentFragment != null)
//                    ft.hide(mCurrentFragment);
//                        mCurrentFragment = NewsFragment.newInstance(2);
//                        ft.add(R.id.content, mCurrentFragment, FRAGMENT_TAG_LIVE);
//                    }
//                    ft.commit();
                    return true;
//                case R.id.navigation_video:
//                    fragment = fm.findFragmentByTag(FRAGMENT_TAG_VIDEO);
//                    if (fragment != null) {
//                if (fragment != mCurrentFragment) {
//                    if (mCurrentFragment != null)
//                        ft.hide(mCurrentFragment);
//                    ft.show(fragment);
//                    mCurrentFragment = fragment;
//                }
//            } else {
//                if (mCurrentFragment != null)
//                    ft.hide(mCurrentFragment);
//                        mCurrentFragment = NewsFragment.newInstance(3);
//                        ft.add(R.id.content, mCurrentFragment, FRAGMENT_TAG_VIDEO);
//                    }
//                    ft.commit();
//                    return true;
//                case R.id.navigation_person:
//                    fragment = fm.findFragmentByTag(FRAGMENT_TAG_PERSON);
//                    if (fragment != null) {
//                if (fragment != mCurrentFragment) {
//                    if (mCurrentFragment != null)
//                        ft.hide(mCurrentFragment);
//                    ft.show(fragment);
//                    mCurrentFragment = fragment;
//                }
//            } else {
//                if (mCurrentFragment != null)
//                    ft.hide(mCurrentFragment);
//                        mCurrentFragment = NewsFragment.newInstance(4);
//                        ft.add(R.id.content, mCurrentFragment, FRAGMENT_TAG_PERSON);
//                    }
//                    ft.commit();
//                    return true;
            }
            return false;
        }
    };
    
    private FragmentManager fm;
    private Fragment mCurrentFragment;
    
    protected final static String FRAGMENT_TAG_NEWS = "news";
    protected final static String FRAGMENT_TAG_IMAGE = "image";
    protected final static String FRAGMENT_TAG_LIVE = "live";
    protected final static String FRAGMENT_TAG_VIDEO = "video";
    protected final static String FRAGMENT_TAG_PERSON = "person";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup rootView = this.findViewById(android.R.id.content);
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            rootView.setPadding(0, statusBarHeight, 0, 0);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_main);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.hide();
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mCurrentFragment = NewsFragment.newInstance(0);
        ft.add(R.id.content, mCurrentFragment, FRAGMENT_TAG_NEWS);
        ft.commit();
    }
    
    @Override
    public MainActivity getParentHost() {
        return this;
    }
    
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
    
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorWhite));
    }
    
}
