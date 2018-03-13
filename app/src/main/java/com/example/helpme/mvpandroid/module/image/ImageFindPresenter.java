package com.example.helpme.mvpandroid.module.image;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.BannerRecyclerAdapter;
import com.example.helpme.mvpandroid.adapter.CategoryRecyclerAdapter;
import com.example.helpme.mvpandroid.adapter.HotEventRecyclerAdapter;
import com.example.helpme.mvpandroid.base.WebActivity;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.Banners;
import com.example.helpme.mvpandroid.entity.image.Categories;
import com.example.helpme.mvpandroid.entity.image.Events;
import com.example.helpme.mvpandroid.entity.image.FindBean;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.banner.BannerRecyclerView;
import com.example.helpme.mvpandroid.widget.banner.BannerScaleHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public class ImageFindPresenter extends BaseMvpPresenter<MainActivity, ImageContract.ImageFindView>
        implements ImageContract.ImageOkHttpCallBcak {
    
    
    private ImageModelImpl impl;
    private StringBuilder findUrl, eventUrl;
    private BannerScaleHelper mBannerScaleHelper;
    
    public BannerScaleHelper getBannerScaleHelper() {
        return mBannerScaleHelper;
    }
    
    public ImageFindPresenter() {
        findUrl = new StringBuilder(GlobalConfig.TU_CHONG_FIND_URL);
        eventUrl = new StringBuilder(GlobalConfig.TU_CHONG_EVENTS_URL);
        impl = new ImageModelImpl();
    }
    
    
    public void getHttpImageInfo() throws Exception {
        impl.okhttp_get(true, 0, FindBean.class, this, findUrl.toString());
        impl.okhttp_get(true, 0, Events.class, this, eventUrl.toString());
    }
    
    
    @Override
    public void onSuccess(final Object data, final boolean refresh, int posititon) {
        if (data == null) {
            getMvpView().onRefreshView(null, false);
        } else {
            if (data instanceof FindBean) {
                final FindBean bean = (FindBean) data;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bean.getResult().equals("SUCCESS")) {
                            getMvpView().onRefreshView(data, true);
                        } else {
                            getMvpView().onRefreshView(null, false);
                        }
                    }
                });
            } else if (data instanceof Events) {
                final Events bean = (Events) data;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bean.getResult().equals("SUCCESS")) {
                            getMvpView().onRefreshView(data, true);
                        } else {
                            getMvpView().onRefreshView(null, false);
                        }
                    }
                });
            }
        }
    }
    
    @Override
    public void onFail(final boolean refresh, int position, String errorInfo) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getMvpView().onRefreshView(null, false);
            }
        });
    }
    
    public void getBaseInfo() {
        DisplayMetrics metric = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        findUrl.append(GlobalConfig.TU_CHONG_URL);
        eventUrl.append(GlobalConfig.TU_CHONG_URL).append("&page=1");
        
        
    }
    
    public void initSkelelton(HotEventRecyclerAdapter mHotEventAdapter, Fragment fragment) {
        BannerRecyclerView mBanner = new BannerRecyclerView(mActivity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        layoutParams.width = DensityUtils.getScreenWidth(mActivity);
        layoutParams.height = DensityUtils.getScreenWidth(mActivity) * 3 / 5;
        mBanner.setLayoutParams(layoutParams);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager
                .HORIZONTAL, false);
        mBanner.setLayoutManager(linearLayoutManager);
        
        final BannerRecyclerAdapter mAdapter = new BannerRecyclerAdapter(new ArrayList<Banners>(), fragment, new
                ImageContract
                        .OnBannerClickListener() {
                    @Override
                    public void onClick(int position, String url) {
                        Intent intent = new Intent();
                        intent.setClass(mActivity, WebActivity.class);
                        intent.putExtra("url", url);
                        getMvpView().onMoveToActivity(intent);
                    }
                });
        mBanner.setAdapter(mAdapter);
        // mRecyclerView绑定scale效果
        mBannerScaleHelper = new BannerScaleHelper();
        mBannerScaleHelper.setFirstItemPos(1200);
        mBannerScaleHelper.attachToRecyclerView(mBanner);

        
        RecyclerView mRecyclerView = new RecyclerView(mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager
                .HORIZONTAL, false);
        mRecyclerView.setLayoutParams(params);
        mRecyclerView.setLayoutManager(layoutManager);
        
        final ArrayList<Categories> categories = new ArrayList<>();
        CategoryRecyclerAdapter mCategoryAdapter = new CategoryRecyclerAdapter(R.layout.item_find_category, categories);
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, CategoryActivity.class);
                intent.putExtra("position", position);
                intent.putParcelableArrayListExtra(GlobalConfig.IMAGE_CATEGORIES, categories);
                getMvpView().onMoveToActivity(intent);
            }
        });
   
        
        mHotEventAdapter.addHeaderView(mBanner);
        mHotEventAdapter.addHeaderView(getHotEventTagView());
        mHotEventAdapter.addHeaderView(mRecyclerView);
    }
    
    
    public View updateBannerView(List<Banners> banners, Fragment fragment) {
    
        BannerRecyclerView mBanner = new BannerRecyclerView(mActivity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        layoutParams.width = DensityUtils.getScreenWidth(mActivity);
        layoutParams.height = DensityUtils.getScreenWidth(mActivity) * 3 / 5;
        mBanner.setLayoutParams(layoutParams);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager
                .HORIZONTAL, false);
        mBanner.setLayoutManager(linearLayoutManager);
        
        final BannerRecyclerAdapter mBannerAdapter = new BannerRecyclerAdapter(banners, fragment, new ImageContract
                .OnBannerClickListener() {
            @Override
            public void onClick(int position, String url) {
                Intent intent = new Intent();
                intent.setClass(mActivity, WebActivity.class);
                intent.putExtra("url", url);
                getMvpView().onMoveToActivity(intent);
            }
        });
        mBanner.setAdapter(mBannerAdapter);
        // mRecyclerView绑定scale效果
        mBannerScaleHelper = new BannerScaleHelper();
        mBannerScaleHelper.setFirstItemPos(banners.size() * 300);
        mBannerScaleHelper.attachToRecyclerView(mBanner);
        return mBanner;
    }
    
    
    public View getHotEventTagView() {
        return LayoutInflater.from(mActivity).inflate(R.layout.layout_find_tag, null);
    }
    
    public View updateTagView(final ArrayList<Categories> categories) {
        RecyclerView mRecyclerView = new RecyclerView(mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager
                .HORIZONTAL, false);
        mRecyclerView.setLayoutParams(params);
        mRecyclerView.setLayoutManager(layoutManager);
        
        CategoryRecyclerAdapter mCategoryAdapter = new CategoryRecyclerAdapter(R.layout.item_find_category, categories);
        mCategoryAdapter.bindToRecyclerView(mRecyclerView);
        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, CategoryActivity.class);
                intent.putExtra("position", position);
                intent.putParcelableArrayListExtra(GlobalConfig.IMAGE_CATEGORIES, categories);
                getMvpView().onMoveToActivity(intent);
            }
        });
        return mRecyclerView;
    }
    
    @Override
    public void onDestroyPersenter() {
        super.onDestroyPersenter();
        impl.cancelAllRequest();
    }
}
