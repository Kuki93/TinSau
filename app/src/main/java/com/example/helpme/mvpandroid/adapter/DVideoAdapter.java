package com.example.helpme.mvpandroid.adapter;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.entity.video.Data;
import com.example.helpme.mvpandroid.entity.video.Group;
import com.example.helpme.mvpandroid.entity.video.Url_list;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.IjkVideoPlayer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
public class DVideoAdapter extends BaseQuickAdapter<Data, BaseViewHolder> {
    
    public DVideoAdapter(@Nullable List<Data> data) {
        super(data);
        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<Data>() {
            @Override
            protected int getItemType(Data entity) {
                //根据你的实体类来判断布局类型
                if (entity.getGroup().getIs_video() == 1)
                    return 0;
                else
                    return 1;
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(0, R.layout.item_type_video)
                .registerItemType(1, R.layout.item_type_gif);
    }
    
    @Override
    protected void convert(BaseViewHolder helper, Data item) {
        Group group = item.getGroup();
        switch (helper.getItemViewType()) {
            case 0:
                IjkVideoPlayer jzVideoPlayer = helper.getView(R.id.videoplayer);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) jzVideoPlayer.getLayoutParams();
                layoutParams.height = Math.max(group.getVideo_height() + 50, DensityUtils.getScreenWidth(mContext) * 3 / 5);
                jzVideoPlayer.setLayoutParams(layoutParams);
                jzVideoPlayer.titleTextView.setVisibility(View.GONE);
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                if (group.getVideo_360p() != null && group.getVideo_360p().getUrl_list() != null) {
                    List<Url_list> url_list = group.getVideo_360p().getUrl_list();
                    for (Url_list urls : url_list) {
                        if (urls.getUrl() != null && !urls.getUrl().trim().isEmpty()) {
                            map.put("360p", urls.getUrl());
                            break;
                        }
                    }
                }
                if (group.getVideo_480p() != null && group.getVideo_480p().getUrl_list() != null) {
                    List<Url_list> url_list = group.getVideo_480p().getUrl_list();
                    for (Url_list urls : url_list) {
                        if (urls.getUrl() != null && !urls.getUrl().trim().isEmpty()) {
                            map.put("480p", urls.getUrl());
                            break;
                        }
                    }
                }
                if (group.getVideo_720p() != null && group.getVideo_720p().getUrl_list() != null) {
                    List<Url_list> url_list = group.getVideo_720p().getUrl_list();
                    for (Url_list urls : url_list) {
                        if (urls.getUrl() != null && !urls.getUrl().trim().isEmpty()) {
                            map.put("720p", urls.getUrl());
                            break;
                        }
                    }
                }
                if (map.size() == 0) {
                    jzVideoPlayer.setUp(group.getMp4_url(), JZVideoPlayer.SCREEN_WINDOW_LIST, "");
                } else {
                    Object[] objects = new Object[3];
                    objects[0] = map;
                    objects[1] = false;//looping
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("key", "value");
                    objects[2] = hashMap;
                    int size = 0;
                    if (GlobalConfig.priority720P)
                        size = map.size() - 1;
                    jzVideoPlayer.setUp(objects, size, JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
                    
                }
                if (group.getLarge_cover().getUrl_list() != null)
                    Glide.with(mContext).load(group.getLarge_cover().getUrl_list().get(0).getUrl()).into(jzVideoPlayer
                            .thumbImageView);
                else if (group.getMedium_cover().getUrl_list() != null)
                    Glide.with(mContext).load(group.getMedium_cover().getUrl_list().get(0).getUrl()).into(jzVideoPlayer
                            .thumbImageView);
                break;
            case 1:
                ImageView gif = helper.getView(R.id.giflayer);
                layoutParams = (ConstraintLayout.LayoutParams) gif.getLayoutParams();
                String url = null;
                if (group.getLarge_image() != null && group.getLarge_image().getUrl_list() != null) {
                    List<Url_list> url_list = group.getLarge_image().getUrl_list();
                    for (Url_list urls : url_list) {
                        if (urls.getUrl() != null && !urls.getUrl().trim().isEmpty()) {
                            url = urls.getUrl();
                            layoutParams.height = group.getLarge_image().getHeight() + 20;
                            break;
                        }
                    }
                } else if (group.getMiddle_image() != null && group.getMiddle_image().getUrl_list() != null) {
                    List<Url_list> url_list = group.getMiddle_image().getUrl_list();
                    for (Url_list urls : url_list) {
                        if (urls.getUrl() != null && !urls.getUrl().trim().isEmpty()) {
                            url = urls.getUrl();
                            layoutParams.height = group.getMiddle_image().getHeight() + 20;
                            break;
                        }
                    }
                }
                if (group.getIs_gif() == 1) {
                    if (layoutParams.height == 0 && group.getVideo_height() != 0) {
                        layoutParams.height = group.getVideo_height() + 20;
                        gif.setLayoutParams(layoutParams);
                    }
                    gif.setVisibility(View.VISIBLE);
                    if (url != null) {
                        helper.setGone(R.id.gif, false);
                        Glide.with(mContext).asGif().load(url).apply(new RequestOptions().placeholder(R.drawable
                                .ic_place_img).error(R.drawable.ic_error_img).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                .into(gif);
                    } else {
                        helper.setGone(R.id.gif, false);
                        gif.setImageResource(R.drawable.ic_error_img);
                    }
                } else {
                    helper.setGone(R.id.gif, false);
                    if (group.getLarge_image_list() != null && group.getLarge_image_list().get(0).getUrl() != null && !group
                            .getLarge_image_list().get(0).getUrl().trim().isEmpty()) {
                        url = group.getLarge_image_list().get(0).getUrl();
                    } else if (group.getLarge_image_list() != null && group.getLarge_image_list().get(0).getUrl_list() !=
                            null) {
                        List<Url_list> url_list = group.getLarge_image_list().get(0).getUrl_list();
                        for (Url_list urls : url_list) {
                            if (urls.getUrl() != null && !urls.getUrl().trim().isEmpty()) {
                                url = urls.getUrl();
                                break;
                            }
                        }
                    }
                    if (url != null) {
                        gif.setVisibility(View.VISIBLE);
                        if (group.getLarge_image_list() != null)
                            layoutParams.height = group.getLarge_image_list().get(0).getHeight() + 20;
                        else if (layoutParams.height == 0 && group.getVideo_height() != 0) {
                            layoutParams.height = group.getVideo_height() + 20;
                        }
                        gif.setLayoutParams(layoutParams);
                        gif.setLayoutParams(layoutParams);
                        Glide.with(mContext).load(url).apply(new RequestOptions().placeholder(R.drawable
                                .ic_place_img).error(R.drawable.ic_error_img).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                .into(gif);
                    } else {
                        gif.setVisibility(View.GONE);
                    }
                }
                break;
        }
        
        ImageView icon = helper.getView(R.id.siteIcon);
        Glide.with(mContext).load(group.getUser().getAvatar_url()).apply(new RequestOptions().circleCrop().error
                (R.drawable.ic_default_icon).placeholder(R.drawable.ic_default_icon)).into(icon);
        helper.setText(R.id.siteName, group.getUser().getName());
        if (group.getContent() != null && !group.getContent().isEmpty())
            helper.setText(R.id.content, Html.fromHtml("<font color='#FF4081'>" + "#" + group.getCategory_name() + "#&nbsp;&nbsp;" +
                    "</font>" + group.getContent()));
        else if (group.getText() != null && !group.getText().isEmpty())
            helper.setText(R.id.content, Html.fromHtml("<font color='#FF4081'>" + "#" + group.getCategory_name() + "#&nbsp;&nbsp;" +
                    "</font>" + group.getText()));
        else
            helper.setText(R.id.content, Html.fromHtml("<font color='#FF4081'>" + "#" + group.getCategory_name() + "#&nbsp;&nbsp;" +
                    "</font>"));
        helper.setText(R.id.zan, group.getDigg_count() + "");
        helper.setText(R.id.cai, group.getBury_count() + "");
        helper.setText(R.id.share, group.getShare_count() + "");
    }
}
