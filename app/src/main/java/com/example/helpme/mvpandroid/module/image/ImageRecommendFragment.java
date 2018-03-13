package com.example.helpme.mvpandroid.module.image;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.ImageRecommendAdapter;
import com.example.helpme.mvpandroid.base.LazyMvpFragment;
import com.example.helpme.mvpandroid.base.WebActivity;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.MessageEvent;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.SpaceItemDecoration;
import com.example.helpme.mvpandroid.widget.refresh.LoadMoreView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);
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
    public void onPieceSelected(PuzzlePiece piece, int position, int itemPosition, Rect rect) {
        PhotoGroup photoGroup = mAdapter.getData().get(itemPosition);
        if (!photoGroup.getType().equals("multi-photo")) {
            Intent intent = new Intent(mActivity, WebActivity.class);
            intent.putExtra("url", photoGroup.getOpusUrl());
            startActivity(intent);
        } else {
            getMvpPresenter().readyAction(mAdapter, position, itemPosition, rect);
        }
    }
    
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.more) {
            getMvpPresenter().showBottomLayout();
        } else if (view.getId() == R.id.share) {
            
        } else {
            Intent intent = new Intent(mActivity, WebActivity.class);
            PhotoGroup photoGroup = mAdapter.getData().get(position);
            if (view.getId() == R.id.siteIcon) {
                intent.putExtra("url", photoGroup.getSiteUrl());
            } else {
                intent.putExtra("url", photoGroup.getOpusUrl());
            }
            startActivity(intent);
        }
    }
    
    @Override
    public MainActivity getParentHost() {
        return mActivity;
    }
    
    @Override
    public void onRefreshView(List<PhotoGroup> mPhotoGroups, boolean refresh, boolean noMore) {
        if (refresh) {
            if (mAdapter == null) {
                mAdapter = new ImageRecommendAdapter(mPhotoGroups);
                mAdapter.bindToRecyclerView(mRecyclerView);
                mAdapter.setOnItemPieceSelectedListener(ImageRecommendFragment.this);
                mAdapter.setOnItemChildClickListener(this);
            } else {
                mAdapter.setNewData(mPhotoGroups);
            }
        } else {
            EventBus.getDefault().post(new MessageEvent.AddNewDataEvent((ArrayList<PhotoGroup>) mPhotoGroups, noMore));
            mAdapter.addData(mPhotoGroups);
        }
        if (mAdapter.getItemCount() == 0) {
            error.setText(R.string.load_empty);
            mErrorLayout.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void onMoveToActivity(Intent intent) {
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
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackEvent(MessageEvent.SlideBackEvent event) {
        if (event.flag != 0)
            return;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int f = linearLayoutManager.findFirstVisibleItemPosition();
        int l = linearLayoutManager.findLastVisibleItemPosition();
        if (event.position < f || event.position > l) {
            mRecyclerView.scrollToPosition(event.position);
        }
        int size = mAdapter.getItem(event.position).getImages().size();
        if (size > 1) {
            if (event.itemIndex >= 4)
                event.itemIndex = 3;
            getItemView(event, R.id.puzzle);
        } else {
            getItemView(event, R.id.imageview);
        }
    }
    
    private void getNewRect(final MessageEvent.SlideBackEvent event, final View view) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Rect rect;
                if (view instanceof PuzzleView) {
                    PuzzleView mPuzzleView = (PuzzleView) view;
                    rect = mPuzzleView.getRectByPosition(event.itemIndex);
                } else {
                    ImageView imageView = (ImageView) view;
                    int[] location = new int[2];
                    imageView.getLocationOnScreen(location);
                    rect = new Rect();
                    rect.left = location[0];
                    rect.top = location[1];
                    rect.right = rect.left + imageView.getWidth();
                    rect.bottom = rect.top + imageView.getHeight();
                }
                int location[] = new int[2];
                mRecyclerView.getLocationOnScreen(location);
                if (rect.top < location[1]) {
                    int dy = rect.top - location[1];
                    mRecyclerView.scrollBy(0, dy);
                    rect.top = location[1];
                    rect.bottom = rect.bottom - dy;
                    EventBus.getDefault().post(rect);
                } else if (rect.bottom > location[1] + mRecyclerView.getHeight()) {
                    int dy = rect.bottom - location[1] - mRecyclerView.getHeight();
                    mRecyclerView.scrollBy(0, dy);
                    rect.top = rect.top - dy;
                    rect.bottom = location[1] + mRecyclerView.getHeight();
                    EventBus.getDefault().post(rect);
                } else {
                    EventBus.getDefault().post(rect);
                }
            }
        });
    }
    
    private void getItemView(final MessageEvent.SlideBackEvent event, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                View view = mAdapter.getViewByPosition(event.position, id);
                while (view == null) {
                    view = mAdapter.getViewByPosition(event.position, id);
                }
                getNewRect(event, view);
            }
        }).start();
    }
    
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestEvent(MessageEvent.Empty empty) {
        if (empty.flag != 0)
            return;
        try {
            getMvpPresenter().getHttpImageInfo(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
