package com.example.helpme.mvpandroid.module.video;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.base.LazyMvpFragment;
import com.example.helpme.mvpandroid.contract.VideoContract;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.SpaceItemDecoration;
import com.example.helpme.mvpandroid.widget.refresh.LoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
@CreatePresenter(DZiPresenter.class)
public class DZiFragment extends  LazyMvpFragment<VideoContract.DZiView,DZiPresenter,MainActivity> implements VideoContract.DZiView {
    
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    public DZiFragment() {
    }
    
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DZiFragment newInstance(int sectionNumber) {
        DZiFragment fragment = new DZiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.error_layout)
    View mErrorLayout;
    @BindView(R.id.error_text)
    TextView error;
    @BindView(R.id.footerview)
    LoadMoreView mfooterView;
    
    @OnClick(R.id.error_layout)
    public void reload() {
        mErrorLayout.setVisibility(View.GONE);
        mSmartRefreshLayout.autoRefresh(100);
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_recommend;
    }
    
    @Override
    protected void onInitViewAndData(View view) {
        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtils.dip2px(getContext(), 15)));
    }
    
    @Override
    protected void onAddListerers() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mErrorLayout.setVisibility(View.GONE);
                try {
                    getMvpPresenter().getHttpImageInfo(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mErrorLayout.setVisibility(View.GONE);
                try {
                    getMvpPresenter().getHttpImageInfo(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    @Override
    protected void onLoadDataOnlyOnce() {
        mSmartRefreshLayout.autoRefresh(100);
    }
    
    @Override
    protected void onVisibleToUser() {
        
    }
    
    @Override
    protected void onInVisibleToUser() {
        
    }
    
    @Override
    public MainActivity getParentHost() {
        return mActivity;
    }
}