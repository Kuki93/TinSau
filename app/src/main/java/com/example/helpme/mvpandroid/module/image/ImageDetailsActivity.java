package com.example.helpme.mvpandroid.module.image;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.helpme.mvpandroid.GlobalConfig;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.adapter.PhotoViewPagerAdapter;
import com.example.helpme.mvpandroid.base.BaseActivity;
import com.example.helpme.mvpandroid.base.WebActivity;
import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.widget.PhotoViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailsActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener
        , ImageContract.OnItemChildClickListener, ImageContract.OnDragPhototListener {
    
    @BindView(R.id.viewpager)
    PhotoViewPager mPhotoViewPager;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.description)
    TextView desc;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.layout)
    FrameLayout layout;
    
    private PhotoViewPagerAdapter mAdapter;
    private PhotoGroup mPhotoGroup;
    private String content;
    private ArrayList<Rect> mRects;
    
    private boolean show;
    private int index = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        ButterKnife.bind(this);
        mPhotoViewPager.addOnPageChangeListener(this);
        more.setOnClickListener(this);
        close.setOnClickListener(this);
        icon.setOnClickListener(this);
        mPhotoGroup = getIntent().getParcelableExtra(GlobalConfig.IMAGE_DETAIL);
        mRects = getIntent().getParcelableArrayListExtra("rects");
        if (mPhotoGroup.getTitle() != null) {
            if (mPhotoGroup.getContent() != null)
                content = mPhotoGroup.getTitle() + "·" + mPhotoGroup.getContent();
            else
                content = mPhotoGroup.getTitle();
        } else {
            content = "";
        }
        name.setText(mPhotoGroup.getName());
        RequestOptions options = new RequestOptions();
        Glide.with(this).load(mPhotoGroup.getIconUrl()).apply(options.error(R.drawable.ic_default_icon).
                placeholder(R.drawable.ic_default_icon).transform(new CircleCrop())).into(icon);
        
        final int position = mPhotoGroup.getIndex();
        mAdapter = new PhotoViewPagerAdapter(this, mPhotoGroup);
        mAdapter.setOnDragPhototListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mPhotoViewPager.setAdapter(mAdapter);
        int size = mPhotoGroup.getUrls().size();
        mPhotoViewPager.setOffscreenPageLimit(size > 2 ? 2 : 1);
        show = true;
        mPhotoViewPager.setCurrentItem(position);
        index = position;
        if (position == 0)
            desc.setText(1 + "/" + size + "  " + content);
    }
    
    
    @Override
    public void onClick(View v) {
        if (v == close)
            finish();
        else if (v == more) {
            
        } else {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("url", mPhotoGroup.getHomeUrl());
            startActivity(intent);
        }
    }
    
    private void moveLayout(View target, float toY, boolean inOrOut) {
        float curTranslationY = target.getTranslationY();
        ObjectAnimator translation = ObjectAnimator.ofFloat(target, "translationY", curTranslationY, toY);
        ObjectAnimator alpha;
        if (inOrOut)
            alpha = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0f);
        else
            alpha = ObjectAnimator.ofFloat(target, "alpha", 0f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(translation).with(alpha);
        animSet.setDuration(500);
        animSet.start();
    }
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        
    }
    
    @Override
    public void onPageSelected(int position) {
        index = position;
        String text = String.valueOf(position + 1) + "/" + mPhotoGroup.getUrls().size() + "  " + content;
        desc.setText(text);
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {
        
    }
    
    @Override
    public void onPhotoViewClick() {
        if (show = !show) {
            moveLayout(toolbar, 0, false);
            moveLayout(desc, 0, false);
        } else {
            moveLayout(toolbar, -toolbar.getHeight(), true);
            moveLayout(desc, desc.getHeight(), true);
        }
    }
    
    boolean isOut = false;
    
    @Override
    public void onScrolling(boolean out) {
        if (isOut != out && !(!show && !out)) {
            isOut = out;
            if (isOut)
                moveLayout(desc, desc.getHeight(), true);
            else
                moveLayout(desc, 0, false);
        }
    }
    
    @Override
    protected void setStatusBar() {
    }
    
    @NonNull
    @Override
    public ViewGroup getParentViewGroup() {
        return mPhotoViewPager;
    }
    
    @Override
    public void onStartSlide() {
        
    }
    
    @Override
    public void onKeepSlideing(int alpha) {
        int color = getResources().getColor(R.color.colorBlack);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        layout.setBackgroundColor(Color.argb(alpha, red, green, blue));
    }
    
    @NonNull
    @Override
    public Rect onReleaseExitAnim() {
        int size = mRects.size();
        return mRects.get(index % size);
    }
    
    @Override
    public void onUpdateExitAnim(int Alpha) {
        onKeepSlideing(Alpha);
    }
    
    @Override
    public void onEndExitAnim() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
    
    @Override
    public boolean onTapExit() {
        return false;
    }
    
    @Override
    public void onBackPressed() {
        mAdapter.getPhotoAdaper(index).finishAnimationCallBack(onReleaseExitAnim());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.onCloseHandler();
    }
}
