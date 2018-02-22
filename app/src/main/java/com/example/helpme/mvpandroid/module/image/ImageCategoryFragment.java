package com.example.helpme.mvpandroid.module.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.TagCategoryAdapter;
import com.example.helpme.mvpandroid.base.LazyMvpFragment;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.FeedList;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created by helpme on 2018/2/22.
 * @Description
 */
@CreatePresenter(CategoryPresenter.class)
public class ImageCategoryFragment extends LazyMvpFragment<ImageContract.ImageCategoryView,
        CategoryPresenter, CategoryActivity> implements ImageContract.ImageCategoryView {
    
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    public ImageCategoryFragment() {
        // Required empty public constructor
    }
    
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageCategoryFragment newInstance(int sectionNumber) {
        ImageCategoryFragment fragment = new ImageCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    
    private int tagId;
    
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.error_layout)
    View mErrorLayout;
    @BindView(R.id.error_text)
    TextView error;
    
    TagCategoryAdapter mAdapter;
    List<FeedList> mFeedLists;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tagId = bundle.getInt(ARG_SECTION_NUMBER);
        }
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_recommend;
    }
    
    @Override
    protected void onInitViewAndData(View view) {
        ButterKnife.bind(this, view);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager
                .VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getMvpPresenter().getBaseInfo(tagId);
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
    public CategoryActivity getParentHost() {
        return mActivity;
    }
    
    @Override
    public void onRefreshView(List<FeedList> feedLists, List<ImageDetails> mData, boolean refresh) {
        if (refresh) {
            if (mAdapter == null) {
                mFeedLists = feedLists;
                mAdapter = new TagCategoryAdapter(R.layout.item_image_grid, mData);
                mAdapter.bindToRecyclerView(mRecyclerView);
                //   mAdapter.setOnItemClickListener();
            } else {
                mFeedLists.clear();
                mFeedLists.addAll(feedLists);
                mAdapter.setNewData(mData);
            }
        } else {
            mFeedLists.addAll(feedLists);
            mAdapter.addData(mData);
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
