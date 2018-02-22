package com.example.helpme.mvpandroid.module.image;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.ImageRecommendAdapter;
import com.example.helpme.mvpandroid.base.LazyMvpFragment;
import com.example.helpme.mvpandroid.base.WebActivity;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.FeedList;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.SpaceItemDecoration;
import com.example.helpme.mvpandroid.widget.refresh.LoadMoreView;
import com.example.helpme.mvpandroid.widget.refresh.RefreshHeadView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiaopo.flying.puzzle.PuzzlePiece;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created by helpme on 2018/2/11.
 * @Description
 */
@CreatePresenter(ImageRecommendPresenter.class)
public class ImageRecommendFragment extends LazyMvpFragment<ImageContract.ImageRecommendView,
        ImageRecommendPresenter, MainActivity> implements ImageContract.OnItemPieceSelectedListener, ImageContract
        .ImageRecommendView, BaseQuickAdapter.OnItemChildClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    public ImageRecommendFragment() {
    }
    
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ImageRecommendFragment newInstance(int sectionNumber) {
        ImageRecommendFragment fragment = new ImageRecommendFragment();
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
    
    private ImageRecommendAdapter mAdapter;
    
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
        getMvpPresenter().getBaseInfo();
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
    public void onPieceSelected(PuzzlePiece piece, int position, int itemPosition, ArrayList<Rect> rects) {
        FeedList feedList = mAdapter.getData().get(itemPosition);
        if (!feedList.getType().equals("multi-photo")) {
            Intent intent = new Intent(mActivity, WebActivity.class);
            intent.putExtra("url", feedList.getUrl());
            startActivity(intent);
        } else {
            getMvpPresenter().readyAction(mAdapter, position, itemPosition, rects);
        }
    }
    
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.more) {
            getMvpPresenter().showBottomLayout();
        } else if (view.getId() == R.id.share) {
            
        } else {
            Intent intent = new Intent(mActivity, WebActivity.class);
            FeedList feedList = mAdapter.getData().get(position);
            if (view.getId() == R.id.siteIcon) {
                intent.putExtra("url", feedList.getSite().getUrl());
            } else {
                intent.putExtra("url", feedList.getUrl());
            }
            startActivity(intent);
        }
    }
    
    @Override
    public MainActivity getParentHost() {
        return mActivity;
    }
    
    @Override
    public void onRefreshView(List<FeedList> feedLists, boolean refresh) {
        if (refresh) {
            if (mAdapter == null) {
                mAdapter = new ImageRecommendAdapter(feedLists);
                mAdapter.bindToRecyclerView(mRecyclerView);
                mAdapter.setOnItemPieceSelectedListener(ImageRecommendFragment.this);
                mAdapter.setOnItemChildClickListener(this);
            } else {
                mAdapter.setNewData(feedLists);
            }
        } else {
            mAdapter.addData(feedLists);
        }
        if (mAdapter.getItemCount() == 0) {
            error.setText(R.string.load_empty);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onMoveToActivity(Intent intent) {
        if (intent != null)
            startActivity(intent);
    }
    
    @Override
    public void onFinishRefresh(boolean success, boolean noMore, int arg) {
        if (!success && mAdapter == null) {
            error.setText(R.string.load_failed);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
        mfooterView.setNoMoreData(!noMore);
        if (noMore)
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
}
