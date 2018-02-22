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
import com.example.helpme.mvpandroid.entity.image.FeedList;
import com.example.helpme.mvpandroid.entity.image.Images;
import com.example.helpme.mvpandroid.entity.image.Site;
import com.example.helpme.mvpandroid.widget.ImagePuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/2/11.
 * @Description
 */
public class ImageRecommendAdapter extends BaseQuickAdapter<FeedList, BaseViewHolder> {
    
    private ImageContract.OnItemPieceSelectedListener mOnItemPieceSelectedListener;
    
    
    public void setOnItemPieceSelectedListener(ImageContract.OnItemPieceSelectedListener onItemPieceSelectedListener) {
        mOnItemPieceSelectedListener = onItemPieceSelectedListener;
    }
    
    public ImageRecommendAdapter(@Nullable List<FeedList> data) {
        super(data);
        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<FeedList>() {
            @Override
            protected int getItemType(FeedList entity) {
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
    protected void convert(final BaseViewHolder helper, FeedList item) {
        final List<Images> images = item.getImages();
        Site site = item.getSite();
        Glide.with(mContext).load(site.getIcon()).apply(new RequestOptions().placeholder(R.drawable.ic_default_icon)
                .error(R.drawable.ic_default_icon).transform(new CircleCrop())).into((ImageView) helper.getView(R.id
                .siteIcon));
        helper.setText(R.id.siteName, site.getName());
        helper.setText(R.id.publish, item.getPublished_at());
        if (item.getTitle() != null && !item.getTitle().trim().equals("")) {
            helper.setGone(R.id.excerpt, true);
            if (item.getExcerpt() != null && !item.getExcerpt().trim().equals("")) {
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                                "<strong><font color='black'>" + item.getTitle() + "·</font></strong>" + item
                                .getExcerpt(),
                        mImageGetter, null));
            } else if (item.getContent() != null && !item.getContent().trim().equals("")) {
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                                "<strong><font color='black'>" + item.getTitle() + "·</font></strong>" + item
                                .getContent(),
                        mImageGetter, null));
            } else {
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                                "<strong><font color='black'>" + item.getTitle() + "</font></strong>",
                        mImageGetter, null));
            }
        } else {
            if (item.getExcerpt() != null && !item.getExcerpt().trim().equals("")) {
                helper.setGone(R.id.excerpt, true);
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                        "<strong><font color='black'>" + item.getExcerpt() + "</font></strong>", mImageGetter, null));
            } else if (item.getContent() != null && !item.getContent().trim().equals("")) {
                helper.setGone(R.id.excerpt, true);
                helper.setText(R.id.excerpt, Html.fromHtml("<img src='" + R.drawable.ic_quot_mark + "'> " +
                        "<strong><font color='black'>" + item.getContent() + "</font></strong>", mImageGetter, null));
            } else {
                helper.setGone(R.id.excerpt, false);
            }
        }
        helper.setText(R.id.like, item.getFavorites() + "");
        int count = item.getImage_count();
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
                layoutParams.height = item.getHeight();
                mPuzzleView.setLayoutParams(layoutParams);
                mPuzzleView.setTouchEnable(false);
                mPuzzleView.setPuzzleLayout(new ImagePuzzleLayout(item.getMode(), item.getScales()));
                for (int i = 0; i < count; i++) {
                    loadImage(mPuzzleView, i, "http://photo.tuchong.com/" + images.get(i)
                            .getUser_id() + "/f/" + images.get(i).getImg_id() + ".jpg");
                }
                mPuzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
                    @Override
                    public void onPieceSelected(PuzzlePiece piece, int position) {
                        if (mOnItemPieceSelectedListener != null) {
                            mOnItemPieceSelectedListener.onPieceSelected(piece, position, helper.getLayoutPosition(),
                                    mPuzzleView.getAllImageRects());
                        }
                    }
                });
                break;
            case 1:
                final ImageView imageView = helper.getView(R.id.imageview);
                layoutParams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                layoutParams.height = item.getHeight();
                imageView.setLayoutParams(layoutParams);
                String url;
                if (!item.getType().equals("multi-photo"))
                    url = item.getTitle_image().get(0).getUrl();
                else
                    url = "http://photo.tuchong.com/" + images.get(0).getUser_id() + "/f/" + images.get(0)
                            .getImg_id() + ".jpg";
                Glide.with(mContext).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_place_img).error(R
                        .drawable.ic_error_img)).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemPieceSelectedListener != null) {
                            ArrayList<Rect> rects = new ArrayList<>();
                            Rect rect = new Rect();
                            imageView.getGlobalVisibleRect(rect);
                            rects.add(rect);
                            mOnItemPieceSelectedListener.onPieceSelected(null, 0, helper.getLayoutPosition(), rects);
                        }
                    }
                });
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