package com.example.helpme.mvpandroid.module.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.DVideoAdapter;
import com.example.helpme.mvpandroid.base.LazyMvpFragment;
import com.example.helpme.mvpandroid.contract.VideoContract;
import com.example.helpme.mvpandroid.entity.video.Data;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.SpaceItemDecoration;
import com.example.helpme.mvpandroid.widget.refresh.LoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
@CreatePresenter(DVideoPresenter.class)
public class DVideoFragment extends LazyMvpFragment<VideoContract.DVideoView, DVideoPresenter, MainActivity> implements
        VideoContract.DVideoView {
    
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    public DVideoFragment() {
    }
    
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DVideoFragment newInstance(int sectionNumber) {
        DVideoFragment fragment = new DVideoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    int position;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            position = getArguments().getInt(ARG_SECTION_NUMBER);
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
    
    private DVideoAdapter mAdapter;
    
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
        getMvpPresenter().addOnScrollListener(mRecyclerView , position);
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
    
    @Override
    public void onRefreshView(List<Data> mDatas, boolean refresh, boolean more) {
        Iterator<Data> iterator = mDatas.iterator();
        while (iterator.hasNext()) {
            Data mData = iterator.next();
            if (mData.getGroup() == null) {
                iterator.remove();
            }
        }
        if (refresh) {
            if (mAdapter == null) {
                mAdapter = new DVideoAdapter(mDatas);
                mAdapter.bindToRecyclerView(mRecyclerView);
                //   mAdapter.setOnItemChildClickListener(this);
            } else {
                mAdapter.setNewData(mDatas);
            }
        } else {
            mAdapter.addData(mDatas);
        }
        if (mAdapter.getItemCount() == 0) {
            error.setText(R.string.load_empty);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onMoveToActivity(Intent intent) {
        
    }
    
    @Override
    public void onFinishRefresh(boolean success, boolean more, int arg) {
        if (!success && mAdapter == null) {
            error.setText(R.string.load_failed);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
        mfooterView.setNoMoreData(!more);
        if (more)
            mSmartRefreshLayout.resetNoMoreData();
        else
            mSmartRefreshLayout.setLoadmoreFinished(true);
        switch (arg) {
            case 1:
                mSmartRefreshLayout.finishRefresh(success);
                break;
            case 2:
                mSmartRefreshLayout.finishLoadmore(success);
                break;
        }
    }
    
    
    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.goOnPlayOnPause();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        JZVideoPlayer.releaseAllVideos();
    }
}
