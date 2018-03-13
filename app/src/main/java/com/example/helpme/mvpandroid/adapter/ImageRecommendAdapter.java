package com.example.helpme.mvpandroid.adapter;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.ImageDetails;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.widget.ImagePuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.util.List;

/**
 * @Created by helpme on 2018/2/11.
 * @Description
 */
public class ImageRecommendAdapter extends BaseQuickAdapter<PhotoGroup, BaseViewHolder> {
    
    private ImageContract.OnItemPieceSelectedListener mOnItemPieceSelectedListener;
    
    
    public void setOnItemPieceSelectedListener(ImageContract.OnItemPieceSelectedListener onItemPieceSelectedListener) {
        mOnItemPieceSelectedListener = onItemPieceSelectedListener;
    }
    
    public ImageRecommendAdapter(@Nullable List<PhotoGroup> datas) {
        super(datas);
        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<PhotoGroup>() {
            @Override
            protected int getItemType(PhotoGroup entity) {
                //根据你的实体类来判断布局类型
                return entity.getTypeInt();
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(0, R.layout.item_image_recommend)
                .registerItemType(1, R.layout.item_image_recommend_other);
    }
    
    @Override
    protected void convert(final BaseViewHolder helper, PhotoGroup item) {
        final List<ImageDetails> images = item.getImages();
        Glide.with(mContext).load(item.getIconUrl()).apply(new RequestOptions().placeholder(R.drawable.ic_default_icon)
                .error(R.drawable.ic_default_icon).transform(new CircleCrop())).into((ImageView) helper.getView(R.id
                .siteIcon));
        helper.setText(R.id.siteName, item.getName());
        helper.setText(R.id.publish, item.getPublishDate());
        if (item.getTitle() != null) {
            helper.setGone(R.id.excerpt, true);
            if (item.getContent() != null) {
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                                "<strong><font color='black'>" + item.getTitle() + "·</font></strong>" + item.getContent(),
                        mImageGetter, null));
            } else {
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                                "<strong><font color='black'>" + item.getTitle() + "</font></strong>",
                        mImageGetter, null));
            }
        } else {
            if (item.getContent() != null) {
                helper.setGone(R.id.excerpt, true);
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                        "<strong><font color='black'>" + item.getContent() + "</font></strong>", mImageGetter, null));
            } else {
                helper.setGone(R.id.excerpt, false);
            }
        }
        helper.setText(R.id.like, item.getFavorite() + "");
        int count = images.size();
        if (!item.getType().equals("multi-photo"))
            helper.setGone(R.id.imageSize, false);
        else
            helper.setGone(R.id.imageSize, true);
        helper.setText(R.id.imageSize, "共有" + count + "张图片");
        count = count > 4 ? 4 : count;
        helper.addOnClickListener(R.id.siteName);
        helper.addOnClickListener(R.id.publish);
        helper.addOnClickListener(R.id.siteIcon);
        helper.addOnClickListener(R.id.share);
        helper.addOnClickListener(R.id.more);
        switch (helper.getItemViewType()) {
            case 0:
                final PuzzleView mPuzzleView = helper.getView(R.id.puzzle);
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mPuzzleView.getLayoutParams();
                layoutParams.height = item.getPuzzHeight();
                mPuzzleView.setLayoutParams(layoutParams);
                mPuzzleView.setTouchEnable(false);
                mPuzzleView.setPuzzleLayout(new ImagePuzzleLayout(item.getMode(), item.getScales()));
                for (int i = 0; i < count; i++) {
                    loadImage(mPuzzleView, i, images.get(i).getItems().get(0).getPhotoUrl());
                }
                mPuzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
                    @Override
                    public void onPieceSelected(PuzzlePiece piece, int position) {
                        if (mOnItemPieceSelectedListener != null) {
                            mOnItemPieceSelectedListener.onPieceSelected(piece, position, helper.getLayoutPosition(),
                                    mPuzzleView.getRectByPosition(position));
                        }
                    }
                });
                if (item.getRect() ==null) {
                    Rect rect = new Rect();
                    mPuzzleView.getGlobalVisibleRect(rect);
                    item.setRect(rect);
                }
                break;
            case 1:
                final ImageView imageView = helper.getView(R.id.imageview);
                layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.height = item.getPuzzHeight();
                imageView.setLayoutParams(layoutParams);
                Glide.with(mContext).load(images.get(0).getItems().get(0).getPhotoUrl()).apply(new RequestOptions()
                        .placeholder(R.drawable.ic_place_img).error(R
                        .drawable.ic_error_img)).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemPieceSelectedListener != null) {
                            Rect rect = new Rect();
                            int[] location = new int[2];
                            imageView.getLocationOnScreen(location);
                            rect.left = location[0];
                            rect.top = location[1];
                            rect.right = rect.left + imageView.getWidth();
                            rect.bottom = rect.top + imageView.getHeight();
                            mOnItemPieceSelectedListener.onPieceSelected(null, 0, helper.getLayoutPosition(), rect);
                        }
                    }
                });
                if (item.getRect() ==null) {
                    Rect rect = new Rect();
                    imageView.getGlobalVisibleRect(rect);
                    item.setRect(rect);
                }
                break;
        }
    }
    
    private Html.ImageGetter mImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            int id = Integer.parseInt(source);
            Drawable drawable = mContext.getResources().getDrawable(id);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            return drawable;
        }
    };
    
    private void loadImage(final PuzzleView mPuzzleView, final int position, final String url) {
        Glide.with(mContext).asDrawable().load(url).apply(new RequestOptions().fitCenter()
                .placeholder(R.drawable.ic_place_img).error(R.drawable.ic_error_img)).into(new SimpleTarget<Drawable>() {
            
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                mPuzzleView.addPiece(placeholder);
            }
            
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                mPuzzleView.setPiece(position, resource);
            }
            
            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                mPuzzleView.setPiece(position, errorDrawable);
            }
            
            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                super.onLoadCleared(placeholder);
            }
        });
    }
}