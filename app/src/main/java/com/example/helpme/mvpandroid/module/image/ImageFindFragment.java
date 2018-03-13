package com.example.helpme.mvpandroid.module.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.HotEventRecyclerAdapter;
import com.example.helpme.mvpandroid.base.LazyMvpFragment;
import com.example.helpme.mvpandroid.base.WebActivity;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.Events;
import com.example.helpme.mvpandroid.entity.image.FindBean;
import com.example.helpme.mvpandroid.entity.image.HotEvents;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.SpaceItemDecoration;
import com.example.helpme.mvpandroid.widget.loading.LoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created by helpme on 2018/2/11.
 * @Description
 */
@CreatePresenter(ImageFindPresenter.class)
public class ImageFindFragment extends LazyMvpFragment<ImageContract.ImageFindView, ImageFindPresenter, MainActivity>
        implements ImageContract.ImageFindView {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    public ImageFindFragment() {
    }
    
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ImageFindFragment newInstance(int sectionNumber) {
        ImageFindFragment fragment = new ImageFindFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading)
    LoadingIndicatorView mLoadingIndicatorView;
    @BindView(R.id.error_layout)
    View mErrorLayout;
    @BindView(R.id.error_text)
    TextView error;
    
    List<HotEvents> hotEvents;
    
    @OnClick(R.id.error_layout)
    public void reload() {
        mErrorLayout.setVisibility(View.GONE);
        onLoadDataOnlyOnce();
    }
    
    private HotEventRecyclerAdapter mAdapter;
    
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_find;
    }
    
    @Override
    protected void onInitViewAndData(View view) {
        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dip2px(getContext(), 15)));
        mLoadingIndicatorView.setIndicatorColor(getResources().getColor(R.color.colorYellow));
        getMvpPresenter().getBaseInfo();
    }
    
    @Override
    protected void onLoadDataOnlyOnce() {
        mLoadingIndicatorView.show();
        try {
            hotEvents = new ArrayList<>();
            getMvpPresenter().getHttpImageInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onVisibleToUser() {
        if (getMvpPresenter().getBannerScaleHelper() != null)
            getMvpPresenter().getBannerScaleHelper().onStartBannerLoop();
    }
    
    @Override
    protected void onInVisibleToUser() {
        if (getMvpPresenter().getBannerScaleHelper() != null)
            getMvpPresenter().getBannerScaleHelper().onCancelBannerLoop();
    }
    
    @Override
    public MainActivity getParentHost() {
        return mActivity;
    }
    
    @Override
    public void onRefreshView(Object data, boolean success) {
        if (success) {
            if (data instanceof FindBean) {
                final FindBean bean = (FindBean) data;
                if (mAdapter == null) {
                    mLoadingIndicatorView.hide();
                    if (hotEvents.size() > 0)
                        mAdapter = new HotEventRecyclerAdapter(R.layout.item_find_event, hotEvents);
                    else
                        mAdapter = new HotEventRecyclerAdapter(R.layout.item_find_event, bean.getHotEvents());
                    mAdapter.addHeaderView(getMvpPresenter().updateBannerView(bean.getBanners(), this));
                    mAdapter.addHeaderView(getMvpPresenter().updateTagView(bean.getCategories()));
                    mAdapter.addHeaderView(getMvpPresenter().getHotEventTagView());
                    mAdapter.bindToRecyclerView(mRecyclerView);
                    getMvpPresenter().getBannerScaleHelper().onStartBannerLoop();
                    mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent();
                            intent.setClass(mActivity, WebActivity.class);
                            intent.putExtra("url", mAdapter.getItem(position).getUrl());
                            onMoveToActivity(intent);
                        }
                    });
                }
                if (mAdapter.getItemCount() == 0) {
                    error.setText(R.string.load_empty);
                    mErrorLayout.setVisibility(View.VISIBLE);
                }
            } else if (data instanceof Events) {
                if (hotEvents.size() > 0)
                    hotEvents.clear();
                hotEvents = ((Events) data).getEventList();
                if (mAdapter != null) {
                    mAdapter.setNewData(hotEvents);
                }
            }
        } else {
            if (mAdapter == null) {
                mLoadingIndicatorView.hide();
                error.setText(R.string.load_failed);
                mErrorLayout.setVisibility(View.VISIBLE);
            }
        }
    }
    
    @Override
    public void onMoveToActivity(Intent intent) {
        startActivity(intent);
    }
    
    
    @Override
    public void onStop() {
        super.onStop();
        if (getMvpPresenter().getBannerScaleHelper() != null)
            getMvpPresenter().getBannerScaleHelper().onCancelBannerLoop();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (getMvpPresenter().getBannerScaleHelper() != null)
            getMvpPresenter().getBannerScaleHelper().onStartBannerLoop();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getMvpPresenter().getBannerScaleHelper() != null)
            getMvpPresenter().getBannerScaleHelper().onCancelBannerLoop();
    }
}
