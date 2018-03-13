package com.example.helpme.mvpandroid.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/22.
 * @Description
 */
public class TagCategoryAdapter extends BaseQuickAdapter<PhotoGroup, BaseViewHolder> {
    
    
    public TagCategoryAdapter(int layoutResId, @Nullable List<PhotoGroup> data) {
        super(layoutResId, data);
    }
    
    @Override
    protected void convert(BaseViewHolder helper, PhotoGroup item) {
        ImageView imageView = helper.getView(R.id.imageview);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        ImageDetails imageDetails = item.getImages().get(0);
        layoutParams.height = item.getPuzzHeight();
        Glide.with(mContext).load(imageDetails.getItems().get(0).getPhotoUrl()).apply(new RequestOptions().placeholder(R.drawable.ic_place_img)
                .error(R.drawable.ic_error_img)).into(imageView);
        int size = item.getImages().size();
        if (size > 1) {
            helper.setText(R.id.imageSize, String.valueOf(size));
            helper.setVisible(R.id.imageSize, true);
        } else
            helper.setVisible(R.id.imageSize, false);
        if (item.getRect() ==null) {
            Rect rect = new Rect();
            imageView.getGlobalVisibleRect(rect);
            item.setRect(rect);
        }
        helper.setText(R.id.favorite, String.valueOf(item.getFavorite()));
    }
}
