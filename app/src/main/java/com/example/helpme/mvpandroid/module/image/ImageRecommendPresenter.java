package com.example.helpme.mvpandroid.module.image;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.ImageRecommendAdapter;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.FeedList;
import com.example.helpme.mvpandroid.entity.image.ImageBean;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.example.helpme.mvpandroid.entity.image.Images;
import com.example.helpme.mvpandroid.entity.image.ItemContent;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.module.home.MainActivity;
import com.example.helpme.mvpandroid.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public class ImageRecommendPresenter extends BaseMvpPresenter<MainActivity, ImageContract.ImageRecommendView>
        implements ImageContract.ImageOkHttpCallBcak {
    
    
    private ImageModelImpl impl;
    private int page;
    private StringBuilder url;
    private long post_id;
    
    public ImageRecommendPresenter() {
        url = new StringBuilder();
        impl = new ImageModelImpl<>();
        page = 1;
    }
    
    public void getBaseInfo() {
        url.append(GlobalConfig.TU_CHONG_RECOMMEND_URL).append(GlobalConfig.TU_CHONG_URL).append("&page=");
    }
    
    public void getHttpImageInfo(boolean refresh) throws Exception {
        if (refresh) {
            page = 1;
            impl.okhttp_get(true, 0, ImageBean.class, this, url.toString() + page + "&type=refresh", "recommend");
        } else {
            page++;
            impl.okhttp_get(false, 0, ImageBean.class, this, url.toString() + page + "&post_id=" + post_id +
                    "&type=loadmore", "recommend");
        }
    }
    
    @Override
    public void onSuccess(final Object data, final boolean refresh, int position) {
        if (data == null || !(data instanceof ImageBean)) {
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
        } else {
            final ImageBean bean = (ImageBean) data;
            final List<PhotoGroup> photoGroups = new ArrayList<>();
            if (bean.getResult().equals("SUCCESS")) {
                List<FeedList> feedLists = bean.getFeedList();
                post_id = feedLists.get(feedLists.size() - 1).getPost_id();
                for (FeedList feedList : feedLists) {
                    PhotoGroup group = new PhotoGroup(feedList.getSite().getName(), feedList.getSite().getIcon(), feedList
                            .getUrl(), feedList.getSite().getUrl(), feedList.getPublished_at(), position, feedList
                            .getFavorites());
                    group.setType(feedList.getType());
                    List<ImageDetails> imageDetails = new ArrayList<>();
                    int count = feedList.getImage_count();
                    count = count > 4 ? 4 : count;
                    List<Rect> rects = new ArrayList<>();
                    if (count > 0)
                        for (Images image : feedList.getImages()) {
                            String url = "http://photo.tuchong.com/" + image.getUser_id() + "/f/" + image.getImg_id() + "" +
                                    ".jpg";
                            List<ItemContent> contents = new ArrayList<>();
                            contents.add(new ItemContent(url, image.getWidth(), image.getHeight(), 0));
                            ImageDetails details = new ImageDetails(contents, image.getImg_id());
                            imageDetails.add(details);
                            rects.add(null);
                        }
                    else {
                        if (feedList.getTitle_image()!=null) {
                            String url = feedList.getTitle_image().get(0).getUrl();
                            List<ItemContent> contents = new ArrayList<>();
                            contents.add(new ItemContent(url, feedList.getTitle_image().get(0).getWidth(), feedList
                                    .getTitle_image().get(0).getHeight(), 0));
                            ImageDetails details = new ImageDetails(contents, 0);
                            imageDetails.add(details);
                            rects.add(null);
                        } else {
                            String url = "";
                            List<ItemContent> contents = new ArrayList<>();
                            contents.add(new ItemContent(url, 300, 200, 0));
                            ImageDetails details = new ImageDetails(contents, 0);
                            imageDetails.add(details);
                            rects.add(null);
                        }
                    }
                    group.setRects(rects);
                    group.setImages(imageDetails);
                    long width = DensityUtils.getScreenWidth(mActivity);
                    if (count <= 1) {
                        if (count == 1)
                            group.setPuzzHeight((int) (imageDetails.get(0).getItems().get(0).getHeight() * width /
                                    imageDetails.get(0).getItems().get(0).getWidth()));
                        else
                            group.setPuzzHeight((int) (feedList.getTitle_image().get(0).getHeight() * width / feedList
                                    .getTitle_image().get(0).getWidth()));
                        group.setTypeInt(1);
                    } else {
                        float[] scales;
                        double a, b;
                        long[] w, h;
                        float m, n;
                        switch (count) {
                            case 2:
                                scales = new float[1];
                                a = imageDetails.get(0).getItems().get(0).getHeight() * imageDetails.get(1).getItems().get
                                        (0).getWidth() * 1.0000d;
                                b = imageDetails.get(0).getItems().get(0).getHeight() * imageDetails.get(1).getItems().get
                                        (0).getWidth() * 1.0000d + imageDetails.get(1).getItems().get(0).getHeight() *
                                        imageDetails.get(0).getItems().get(0).getWidth() * 1.0000d;
                                scales[0] = (float) (a / b);
                                group.setPuzzHeight((int) (imageDetails.get(0).getItems().get(0).getHeight() * width /
                                        imageDetails.get(0).getItems().get(0).getWidth() + imageDetails.get(1).getItems()
                                        .get(0).getHeight() * width / imageDetails.get(1).getItems().get(0).getWidth()));
                                group.setModeAndScales(0, scales);
                                break;
                            case 3:
                                w = new long[3];
                                h = new long[3];
                                for (int i = 0; i < 3; i++) {
                                    w[i] = imageDetails.get(i).getItems().get(0).getWidth();
                                    h[i] = imageDetails.get(i).getItems().get(0).getHeight();
                                }
                                scales = new float[2];
                                m = sqrt(getVariance(w));
                                n = sqrt(getVariance(h));
                                if ((m < 222) && (n < 222)) {
                                    a = h[0] + h[1] + h[2];
                                    b = h[1] + h[2];
                                    scales[0] = (float) (h[0] * 1.0000d / a);
                                    scales[1] = (float) (h[1] * 1.0000d / b);
                                    group.setPuzzHeight((int) (width * h[0] / w[0] + width * h[1] / w[1] + width * h[2] /
                                            w[2]));
                                    group.setModeAndScales(1, scales);
                                } else {
                                    if (m >= n) {
                                        a = h[0] * width / w[0];
                                        b = 1.0000d * h[1] * h[2] * width / (1.0000d * w[1] * h[2] + 1.0000d * w[2] *
                                                h[1]);
                                        scales[0] = (float) (a / (a + b));
                                        group.setPuzzHeight((int) (a + b));
                                        a = w[1] * h[2];
                                        b = w[1] * h[2] + w[2] * h[1];
                                        scales[1] = (float) (a * 1.0000d / b);
                                        group.setModeAndScales(2, scales);
                                    } else {
                                        a = 0.0001d * w[0] * h[1] * w[2] + 0.0001d * w[0] * w[1] * h[2];
                                        b = 0.0001d * h[0] * w[1] * w[2] + 0.0001d * w[0] * h[1] * w[2] + 0.0001d *
                                                w[0] *
                                                w[1] * h[2];
                                        scales[0] = (float) (a / b);
                                        group.setPuzzHeight((int) (scales[0] * width / w[0] * h[0]));
                                        a = w[2] * h[1];
                                        b = w[2] * h[1] + w[1] * h[2];
                                        scales[1] = (float) (a * 1.0000d / b);
                                        group.setModeAndScales(3, scales);
                                    }
                                }
                                break;
                            case 4:
                                w = new long[4];
                                h = new long[4];
                                for (int i = 0; i < 4; i++) {
                                    w[i] = imageDetails.get(i).getItems().get(0).getWidth();
                                    h[i] = imageDetails.get(i).getItems().get(0).getHeight();
                                }
                                if (w[0] == w[2] && w[1] == w[3] && h[0] == h[1] && h[2] == h[3]) {
                                    scales = new float[2];
                                    a = w[0] + w[1];
                                    b = h[0] + h[2];
                                    scales[0] = h[0] * 1.0f / (h[0] + h[2]);
                                    scales[1] = w[0] * 1.0f / (w[0] + w[1]);
                                    group.setPuzzHeight((int) (width * b / a));
                                    group.setModeAndScales(4, scales);
                                } else {
                                    scales = new float[3];
                                    m = getVariance(w);
                                    n = getVariance(h);
                                    if (m >= n) {
                                        a = h[0] * h[1] * width * 0.0001d / (w[0] * h[1] * 0.0001d + w[1] * h[0] *
                                                0.0001d);
                                        b = h[2] * h[3] * width * 0.0001d / (w[2] * h[3] * 0.0001d + w[3] * h[2] *
                                                0.0001d);
                                        scales[0] = (float) (a / (a + b));
                                        group.setPuzzHeight((int) (a + b));
                                        a = w[0] * h[1];
                                        b = w[1] * h[0] + w[0] * h[1];
                                        scales[1] = (float) (a * 1.0000d / b);
                                        a = w[2] * h[3];
                                        b = w[3] * h[2] + w[2] * h[3];
                                        scales[2] = (float) (a * 1.0000d / b);
                                        group.setModeAndScales(5, scales);
                                    } else {
                                        a = w[0] * h[1] * w[2] * w[3] * 0.000001d + w[0] * w[1] * w[2] * h[3] *
                                                0.000001d;
                                        b = h[0] * w[1] * w[2] * w[3] * 0.000001d + w[0] * h[1] * w[2] * w[3] *
                                                0.000001d +
                                                w[0] * w[1] * h[2] * w[3] * 0.000001d + w[0] * w[1] * w[2] * h[3] *
                                                0.000001d;
                                        scales[0] = (float) (a / b);
                                        group.setPuzzHeight((int) (scales[0] * width / (w[0] * w[2]) * (w[2] * h[0] +
                                                w[0] *
                                                        h[2])));
                                        a = w[2] * h[0];
                                        b = w[0] * h[2] + w[2] * h[0];
                                        scales[1] = (float) (a * 1.0000d / b);
                                        a = w[3] * h[1];
                                        b = w[1] * h[3] + w[3] * h[1];
                                        scales[2] = (float) (a * 1.0000d / b);
                                        group.setModeAndScales(6, scales);
                                    }
                                }
                                break;
                        }
                    }
                    if (feedList.getTitle() != null && !feedList.getTitle().equals("")) {
                        group.setTitle(feedList.getTitle());
                        if (feedList.getExcerpt() != null && !feedList.getExcerpt().equals("")) {
                            group.setContent(feedList.getExcerpt());
                        } else if (feedList.getContent() != null && !feedList.getContent().equals("")) {
                            group.setContent(feedList.getContent());
                        } else {
                            group.setContent(null);
                        }
                    } else {
                        group.setContent(null);
                        if (feedList.getExcerpt() != null && !feedList.getExcerpt().equals("")) {
                            group.setTitle(feedList.getExcerpt());
                        } else if (feedList.getContent() != null && !feedList.getContent().equals("")) {
                            group.setTitle(feedList.getContent());
                        } else {
                            group.setTitle(null);
                        }
                    }
                    photoGroups.add(group);
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMvpView().onRefreshView(photoGroups, refresh,bean.getMore());
                        if (refresh)
                            getMvpView().onFinishRefresh(true, bean.getMore(), 1);
                        else {
                            getMvpView().onFinishRefresh(true, bean.getMore(), 2);
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
    }
    
    
    @Override
    public void onFail(final boolean refresh, int posititon, String errorInfo) {
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
    
    
    //方差s^2=[(x1-x)^2 +...(xn-x)^2]/n  
    private float getVariance(long[] x) {
        int m = x.length;
        int sum = 0;
        for (long aX : x) {//求和  
            sum += aX;
        }
        float dAve = sum * 1.0f / m;//求平均值  
        float dVar = 0;
        for (long aX : x) {//求方差  
            dVar += (aX - dAve) * (aX - dAve);
        }
        return dVar / m;
    }
    
    
    /**
     * 开方
     *
     * @param m
     * @return
     */
    private float sqrt(float m) {
        if (m == 0) {
            return 0;
        }
        
        float i = 0;
        float x1, x2 = 0;
        while ((i * i) <= m) {
            i += 0.1;
        }
        x1 = i;
        for (int j = 0; j < 10; j++) {
            x2 = m;
            x2 /= x1;
            x2 += x1;
            x2 /= 2;
            x1 = x2;
        }
        return x2;
    }
    
    public void readyAction(ImageRecommendAdapter mAdapter, int indexByGroup, int groupIndex, Rect rect) {
        int indexByAll = 0;
        for (int i = 0; i < groupIndex; i++) {
            indexByAll += mAdapter.getData().get(i).getRects().size();
        }
        indexByAll += indexByGroup;
        PhotoGroup photoGroup = mAdapter.getData().get(groupIndex);
        photoGroup.getRects().set(indexByGroup, rect);
        Intent intent = new Intent();
        intent.putExtra("indexByAll", indexByAll);
        intent.putExtra("indexByGroup", indexByGroup);
        intent.putExtra("groupIndex", groupIndex);
        intent.putParcelableArrayListExtra(GlobalConfig.IMAGE_DETAIL, (ArrayList<? extends Parcelable>) mAdapter.getData());
        intent.setClass(mActivity, ImageDetailsActivity.class);
        getMvpView().onMoveToActivity(intent);
        mActivity.overridePendingTransition(0, 0);
    }
    
    public void showBottomLayout() {
        Dialog bottomDialog = new Dialog(mActivity, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_bottom_dialog, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        int margin = DensityUtils.dip2px(mActivity, 8);
        params.width = DensityUtils.getScreenWidth(mActivity) - 2 * margin;
        params.bottomMargin = margin;
        params.leftMargin = margin;
        params.rightMargin = margin;
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.show();
    }
    
    @Override
    public void onDestroyPersenter() {
        super.onDestroyPersenter();
        impl.cancelAllRequest();
    }
    
}
