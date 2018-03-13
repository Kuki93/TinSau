package com.example.helpme.mvpandroid.module.video;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.DVideoAdapter;
import com.example.helpme.mvpandroid.contract.VideoContract;
import com.example.helpme.mvpandroid.entity.video.VideoBean;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.widget.IjkVideoPlayer;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
public class DVideoPresenter extends BaseMvpPresenter<MainActivity, VideoContract.DVideoView> implements VideoContract
        .VideoOkHttpCallBcak {
    
    private VideoModelImpl impl;
    private StringBuilder url;
    private long min_time;
    
    private String[] type = {"-101", "-104", "-103", "-102"};
    private int position;
    
    public DVideoPresenter() {
        min_time = 0;
        impl = new VideoModelImpl();
        url = new StringBuilder(GlobalConfig.NEI_HAN_DUAN_ZI_VIDEO_BASE_URL);
        url.append(GlobalConfig.NEI_HAN_DUAN_ZI_URL);
    }
    
    public void getHttpImageInfo(boolean refresh) throws Exception {
        long am_loc_time = System.currentTimeMillis();
        if (min_time == 0)
            min_time = am_loc_time - 10 * 60 * 1000;
        url.append("&content_type=").append(type[position]);
        url.append("&am_loc_time").append(am_loc_time).append("&min_time").append(min_time);
        impl.okhttp_get(refresh, 0, VideoBean.class, this, url.toString());
        min_time = am_loc_time;
    }
    
    @Override
    public void onSuccess(Object data, final boolean refresh, int position) {
        final VideoBean bean = (VideoBean) data;
        if (bean != null && bean.getMessage().equals("success")) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getMvpView().onRefreshView(bean.getData().getData(), refresh, bean.getData().isHas_more());
                    if (refresh)
                        getMvpView().onFinishRefresh(true, bean.getData().isHas_more(), 1);
                    else {
                        getMvpView().onFinishRefresh(true, bean.getData().isHas_more(), 2);
                    }
                }
            });
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (refresh)
                        getMvpView().onFinishRefresh(false, true, 1);
                    else {
                        getMvpView().onFinishRefresh(false, true, 2);
                    }
                }
            });
        }
    }
    
    @Override
    public void onFail(final boolean refresh, int position, String errorInfo) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (refresh)
                    getMvpView().onFinishRefresh(false, true, 1);
                else {
                    getMvpView().onFinishRefresh(false, true, 2);
                }
            }
        });
    }
    
    public void addOnScrollListener(RecyclerView recyclerView, final int position) {
        this.position = position;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (position < 2) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int n = layoutManager.findFirstVisibleItemPosition();
                    DVideoAdapter mAdapter = (DVideoAdapter) recyclerView.getAdapter();
                    if (mAdapter.getItemViewType(n) == 0) {
                        IjkVideoPlayer player = (IjkVideoPlayer) mAdapter.getViewByPosition(n - 1, R.id.videoplayer);
                        if (player != null && !player.isCurrentPlay()) {
                          //  player.
                        }
                    }
                }
            }
        });
    }
}
