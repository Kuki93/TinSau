package com.example.helpme.mvpandroid.module.image;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.helpme.mvp.factory.CreatePresenter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.TagCategoryAdapter;
import com.example.helpme.mvpandroid.base.LazyMvpFragment;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.MessageEvent;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
        EventBus.getDefault().register(this);
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
    public void onRefreshView(final List<PhotoGroup> mPhotoGroups, boolean refresh) {
        if (refresh) {
            if (mAdapter == null) {
                mAdapter = new TagCategoryAdapter(R.layout.item_image_grid, mPhotoGroups);
                mAdapter.bindToRecyclerView(mRecyclerView);
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        int indexByAll = 0;
                        for (int i = 0; i < position; i++) {
                            indexByAll += mAdapter.getData().get(i).getRects().size();
                        }
                        Intent intent = new Intent(mActivity, ImageDetailsActivity.class);
                        Rect rect = new Rect();
                        adapter.getViewByPosition(position, R.id.imageview).getGlobalVisibleRect(rect);
                        PhotoGroup photoGroup = mAdapter.getData().get(position);
                        photoGroup.getRects().set(0, rect);
                        intent.putExtra("flag", 1);
                        intent.putExtra("indexByAll", indexByAll);
                        intent.putExtra("indexByGroup", 0);
                        intent.putExtra("groupIndex", position);
                        intent.putParcelableArrayListExtra(GlobalConfig.IMAGE_DETAIL, (ArrayList<? extends Parcelable>)
                                mAdapter.getData());
                        onMoveToActivity(intent);
                        mActivity.overridePendingTransition(0, 0);
                    }
                });
            } else {
                mAdapter.setNewData(mPhotoGroups);
            }
        } else {
            EventBus.getDefault().post(new MessageEvent.AddNewDataEvent((ArrayList<PhotoGroup>) mPhotoGroups, true));
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
        if (event.flag != 1 || !getUserVisibleHint())
            return;
        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
        int[] f = manager.findFirstVisibleItemPositions(null);
        int[] l = manager.findLastVisibleItemPositions(null);
        if (event.position < Math.min(f[0], f[1]) || event.position > Math.max(l[0], l[1])) {
            mRecyclerView.scrollToPosition(event.position);
        }
        getItemView(event);
        
    }
    
    private void getItemView(final MessageEvent.SlideBackEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                View view = mAdapter.getViewByPosition(event.position, R.id.imageview);
                while (view == null) {
                    view = mAdapter.getViewByPosition(event.position, R.id.imageview);
                }
                getNewRect(event, view);
            }
        }).start();
    }
    
    private void getNewRect(final MessageEvent.SlideBackEvent event, final View view) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Rect rect;
                ImageView imageView = (ImageView) view;
                int[] location = new int[2];
                imageView.getLocationOnScreen(location);
                rect = new Rect();
                rect.left = location[0];
                rect.top = location[1];
                rect.right = rect.left + imageView.getWidth();
                rect.bottom = rect.top + imageView.getHeight();
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
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestEvent(MessageEvent.Empty empty) {
        if (empty.flag != 1 || !getUserVisibleHint())
            return;
        try {
            Log.d("sdas", "onRequestEvent" + 23213);
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
