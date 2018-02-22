package com.example.helpme.mvpandroid.adapter;

import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.ZoomDragPhotoView;

import java.util.List;

/**
 * @Created by helpme on 2018/2/13.
 * @Description
 */
public class ImageDetailsRecyclerAdapter extends BaseQuickAdapter<ImageDetails, BaseViewHolder> {
    
    private RequestOptions requestOptions;
    
    private ImageContract.OnDragPhototListener mOnDragPhototListener;
    
    private int position;
    
    public void setOnDragPhototListener(ImageContract.OnDragPhototListener onDragPhototListener) {
        mOnDragPhototListener = onDragPhototListener;
    }
    
    public ImageDetailsRecyclerAdapter(@Nullable List<ImageDetails> data, int position) {
        super(data);
        this.position = position;
        setMultiTypeDelegate(new MultiTypeDelegate<ImageDetails>() {
            @Override
            protected int getItemType(ImageDetails entity) {
                //根据你的实体类来判断布局类型
                return entity.getType();
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(0, R.layout.item_image_details_photo)
                .registerItemType(1, R.layout.item_image_details_title)
                .registerItemType(2, R.layout.item_image_details_text)
                .registerItemType(3, R.layout.item_image_details_param)
                .registerItemType(4, R.layout.item_image_details_empty);
        
        requestOptions = new RequestOptions();
        //在RequestOptions中使用Transformations
        requestOptions.placeholder(R.drawable.ic_place_img).error(R.drawable.ic_error_img).diskCacheStrategy
                (DiskCacheStrategy.ALL).skipMemoryCache(true);
    }
    
    @Override
    protected void convert(final BaseViewHolder helper, ImageDetails item) {
        switch (helper.getItemViewType()) {
            case 0:
                ZoomDragPhotoView mPhotoView = helper.getView(R.id.photoview);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPhotoView.getLayoutParams();
                int width = DensityUtils.getScreenWidth(mContext);
                int height = DensityUtils.getScreenHeight(mContext);
                float m = item.getHeight() * 1.0f / item.getWidth();
                float n = height * 1.0f / width;
                if (m >= n) {
                    layoutParams.width = (int) (height  * 1.0f / item.getHeight() * item.getWidth());
                    layoutParams.height = height;
                } else {
                    layoutParams.width = width;
                    layoutParams.height = (int) (width * 1.0f / item.getWidth() * item.getHeight());
                }
                mPhotoView.setLayoutParams(layoutParams);
                helper.addOnClickListener(R.id.photoview);
                mPhotoView.setMaximumScale(3.6f);
                if (mOnDragPhototListener != null) {
                    mPhotoView.setOnDragPhototListener(mOnDragPhototListener);
                    mPhotoView.setPosition(position);
                }
                Glide.with(mContext).load(item.getPhotoUrl()).apply(requestOptions).into(mPhotoView);
                break;
            case 1:
                helper.setText(R.id.title, item.getContent());
                break;
            case 2:
                helper.setText(R.id.text, item.getContent());
                break;
            case 3:
                helper.setText(R.id.paramName, item.getDesc());
                helper.setText(R.id.param, item.getContent());
                break;
        }
    }
    
    
}
