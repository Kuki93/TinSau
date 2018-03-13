package com.example.helpme.mvpandroid.module.image;

import android.graphics.Rect;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.FeedList;
import com.example.helpme.mvpandroid.entity.image.ImageData;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.example.helpme.mvpandroid.entity.image.Images;
import com.example.helpme.mvpandroid.entity.image.ItemContent;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.utils.DensityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Created by helpme on 2018/2/22.
 * @Description
 */
public class CategoryPresenter extends BaseMvpPresenter<CategoryActivity, ImageContract.ImageCategoryView> implements
        ImageContract.ImageOkHttpCallBcak {
    private ImageModelImpl impl;
    private int page;
    private StringBuilder url;
    private long post_id;
    
    
    public CategoryPresenter() {
        url = new StringBuilder(GlobalConfig.TU_CHONG_RECOMMEND_BASE_URL);
        impl = new ImageModelImpl();
        page = 1;
    }
    
    public void getBaseInfo(int tagId) {
        url.append("discover/").append(tagId).append("/category?").append(GlobalConfig.TU_CHONG_URL).append
                ("&count=20&page=");
        
    }
    
    public void getHttpImageInfo(boolean refresh) throws Exception {
        Map<String, String> header = new HashMap<>();
        header.put("device", GlobalConfig.getDeviceUniqID(mActivity));
        header.put("version", "350");
        header.put("channel", "tuchong");
        header.put("platform", "android");
        header.put("Host", "api.tuchong.com");
        header.put("Connection", "Keep-Alive");
        header.put("User-Agent", "okhttp/3.8.0");
        if (refresh) {
            page = 1;
            String urls = url.toString() + page;
            impl.okhttp_header_get(true, 0, ImageData.class, this, header, urls, "recommend");
        } else {
            page++;
            String urls = url.toString() + page + "&post_id=" + post_id;
            impl.okhttp_header_get(false, 0, ImageData.class, this, header, urls, "recommend");
        }
    }
    
    @Override
    public void onSuccess(final Object data, final boolean refresh, int position) {
        if (data == null || !(data instanceof ImageData)) {
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
            final ImageData bean = (ImageData) data;
            if (bean.getResult().equals("SUCCESS")) {
                List<FeedList> feedLists = bean.getPost_list();
                post_id = feedLists.get(feedLists.size() - 1).getPost_id();
                final List<PhotoGroup> mData = new ArrayList<>();
                for (FeedList list : feedLists) {
                    if (list.getImage_count() > 0) {
                        PhotoGroup photoGroup = new PhotoGroup(list.getSite().getName(), list.getSite().getIcon(), list
                                .getUrl(), list.getSite().getUrl(), list.getPublished_at(), position, list
                                .getFavorites());
                        photoGroup.setType(list.getType());
                        List<ImageDetails> imageDetails = new ArrayList<>();
                        List<Rect> rects = new ArrayList<>();
                        for (Images image : list.getImages()) {
                            String url = "http://photo.tuchong.com/" + image.getUser_id() + "/f/" + image.getImg_id() + "" +
                                    ".jpg";
                            List<ItemContent> contents = new ArrayList<>();
                            contents.add(new ItemContent(url, image.getWidth(), image.getHeight(), 0));
                            ImageDetails details = new ImageDetails(contents, image.getImg_id());
                            imageDetails.add(details);
                            rects.add(null);
                        }
                        photoGroup.setRects(rects);
                        photoGroup.setImages(imageDetails);
                        long width = DensityUtils.getScreenWidth(mActivity) - DensityUtils.dip2px(mActivity, 16);
                        photoGroup.setPuzzHeight((int) (imageDetails.get(0).getItems().get(0).getHeight() * width / 2 /
                                imageDetails.get(0).getItems().get(0).getWidth()));
                        if (list.getTitle() != null && !list.getTitle().equals("")) {
                            photoGroup.setTitle(list.getTitle());
                            if (list.getExcerpt() != null && !list.getExcerpt().equals("")) {
                                photoGroup.setContent(list.getExcerpt());
                            } else if (list.getContent() != null && !list.getContent().equals("")) {
                                photoGroup.setContent(list.getContent());
                            } else {
                                photoGroup.setContent(null);
                            }
                        } else {
                            photoGroup.setContent(null);
                            if (list.getExcerpt() != null && !list.getExcerpt().equals("")) {
                                photoGroup.setTitle(list.getExcerpt());
                            } else if (list.getContent() != null && !list.getContent().equals("")) {
                                photoGroup.setTitle(list.getContent());
                            } else {
                                photoGroup.setTitle(null);
                            }
                        }
                        mData.add(photoGroup);
                    }
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getMvpView().onRefreshView(mData, refresh);
                        if (refresh)
                            getMvpView().onFinishRefresh(true, true, 1);
                        else {
                            getMvpView().onFinishRefresh(true, true, 2);
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
}
