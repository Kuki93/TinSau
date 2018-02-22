package com.example.helpme.mvpandroid.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.entity.image.HotEvents;

import java.util.List;

/**
 * @Created by helpme on 2018/2/18.
 * @Description
 */
public class HotEventRecyclerAdapter extends BaseQuickAdapter<HotEvents, BaseViewHolder> {
    
    public HotEventRecyclerAdapter(int layoutResId, @Nullable List<HotEvents> data) {
        super(layoutResId, data);
    }
    
    @Override
    protected void convert(BaseViewHolder helper, HotEvents item) {
        helper.setText(R.id.title, "「" + item.getTitle() + "」");
        helper.setText(R.id.desc, Html.fromHtml("<strong><font color='black'>" + item.getImage_count() + "件作品·"+"</font></strong>"+
                "\uD83D\uDC51" + item.getPrize_desc()));
        ImageView img = helper.getView(R.id.imageview);
        Glide.with(mContext).load(item.getImages().get(0)).into(img);
        helper.setText(R.id.title_tag, "距截稿" + item.getDueDays() + "天");
    }
}
