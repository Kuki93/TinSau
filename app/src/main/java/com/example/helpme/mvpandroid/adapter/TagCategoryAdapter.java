package com.example.helpme.mvpandroid.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;

import java.util.List;

/**
 * @Created by helpme on 2018/2/22.
 * @Description
 */
public class TagCategoryAdapter extends BaseQuickAdapter<ImageDetails, BaseViewHolder> {
    
    public TagCategoryAdapter(int layoutResId, @Nullable List<ImageDetails> data) {
        super(layoutResId, data);
    }
    
    @Override
    protected void convert(BaseViewHolder helper, ImageDetails item) {
        ImageView imageView = helper.getView(R.id.imageview);
        Glide.with(mContext).load(item.getPhotoUrl()).into(imageView);
    }
}
