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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.example.helpme.mvpandroid.entity.image.MessageEvent;
import com.example.helpme.mvpandroid.entity.image.PhotoGroup;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.widget.PhotoViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<PhotoGroup> mPhotoGroups;
    private List<Rect> mRects;
    private List<Integer> nums;
    private boolean nomore;
    private boolean isloading;
    
    private boolean show;
    private int count;
    private int indexByAll;  // 在所有组中的第几个
    private int indexByGroup;  //在第某组的第几个
    private int groupIndex; //在第几组
    private int lastIndex = -1;
    private int flag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mPhotoViewPager.addOnPageChangeListener(this);
        more.setOnClickListener(this);
        close.setOnClickListener(this);
        icon.setOnClickListener(this);
        flag = getIntent().getIntExtra("flag", 0);
        indexByAll = getIntent().getIntExtra("indexByAll", 0);
        indexByGroup = getIntent().getIntExtra("indexByGroup", 0);
        groupIndex = getIntent().getIntExtra("groupIndex", 0);
        mPhotoGroups = getIntent().getParcelableArrayListExtra(GlobalConfig.IMAGE_DETAIL);
        mRects = new ArrayList<>();
        nums = new ArrayList<>();
        for (PhotoGroup mPhotoGroup : mPhotoGroups) {
            count += mPhotoGroup.getImages().size();
            nums.add(count);
            mRects.addAll(mPhotoGroup.getRects());
        }
        mAdapter = new PhotoViewPagerAdapter(mPhotoGroups, this, nums, count);
        mAdapter.setOnDragPhototListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mPhotoViewPager.setAdapter(mAdapter);
        mPhotoViewPager.setCurrentItem(indexByAll);
        mPhotoViewPager.setOffscreenPageLimit(2);
        show = true;
        nomore = false;
        if (indexByAll == 0)
            changeTitle();
    }
    
    private void changeTitle() {
        String content;
        int size = getGroupByPosition(indexByAll).getImages().size();
        if (getGroupByPosition(indexByAll).getTitle() != null) {
            if (getGroupByPosition(indexByAll).getContent() != null)
                content = getGroupByPosition(indexByAll).getTitle() + "·" + getGroupByPosition(indexByAll).getContent();
            else
                content = getGroupByPosition(indexByAll).getTitle();
        } else {
            content = "";
        }
        name.setText(getGroupByPosition(indexByAll).getName());
        RequestOptions options = new RequestOptions();
        Glide.with(this).load(getGroupByPosition(indexByAll).getIconUrl()).apply(options.error(R.drawable.ic_default_icon).
                placeholder(R.drawable.ic_default_icon).transform(new CircleCrop())).into(icon);
        desc.setText((indexByGroup + 1) + "/" + size + "  " + content);
    }
    
    private PhotoGroup getGroupByPosition(int position) {
        if (lastIndex == position)
            return mPhotoGroups.get(groupIndex);
        int count = nums.size();
        for (int i = 0; i < count; i++) {
            if (position < nums.get(i)) {
                if (i == 0)
                    indexByGroup = position;
                else
                    indexByGroup = position - nums.get(i - 1);
                groupIndex = i;
                lastIndex = position;
                return mPhotoGroups.get(i);
            }
        }
        return null;
    }
    
    @Override
    public void onClick(View v) {
        if (v == close) {
            onBackNewRectEvent(null);
            EventBus.getDefault().post(new MessageEvent.SlideBackEvent(groupIndex, indexByGroup, flag));
            mAdapter.getPhotoView(indexByAll).finishAnimationCallBack(onReleaseExitAnim());
        } else if (v == more) {
            
        } else {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("url", getGroupByPosition(indexByAll).getSiteUrl());
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
    public void onPageSelected(int index) {
        indexByAll = index;
        changeTitle();
        if (nomore || isloading)
            return;
        int n = mAdapter.getCount() - indexByAll;
        if (n < 12) {
            isloading = true;
            EventBus.getDefault().post(new MessageEvent.Empty(flag));
        }
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
    public Rect getDefaultRect() {
        if (mPhotoGroups.get(groupIndex).getRect() == null) {
            Rect rect = mPhotoGroups.get(0).getRect();
            if (rect == null) {
                rect = new Rect();
                rect.left = 0;
                rect.top = 0;
                rect.right = DensityUtils.getScreenWidth(this);
            }
            rect.bottom = rect.top + 200;
            return rect;
        } else
            return mPhotoGroups.get(groupIndex).getRect();
    }
    
    @Override
    public boolean isAllowSwipe() {
        RecyclerView mRecyclerView = mAdapter.getPhotoRecyclerView(indexByAll);
        if (mRecyclerView != null)
            return getScollYDistance(mRecyclerView) == 0;
        else
            return false;
    }
    
    public int getScollYDistance(RecyclerView mRecyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
    
    @Override
    public void onStartSlide() {
        EventBus.getDefault().post(new MessageEvent.SlideBackEvent(groupIndex, indexByGroup, flag));
    }
    
    @Override
    public void onKeepSlideing(int alpha) {
        int color = getResources().getColor(R.color.colorBlack);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        layout.setBackgroundColor(Color.argb(alpha, red, green, blue));
    }
    
    @Override
    public Rect onReleaseExitAnim() {
        int size;
        if (indexByGroup >= 4)
            size = indexByAll - indexByGroup + 3;
        else
            size = indexByAll;
        return mRects.get(size);
    }
    
    @Override
    public void onUpdateExitAnim(int Alpha) {
        onKeepSlideing(Alpha);
    }
    
    @Override
    public void onEndExitAnim(final Rect rect) {
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
        onBackNewRectEvent(null);
        EventBus.getDefault().post(new MessageEvent.SlideBackEvent(groupIndex, indexByGroup, flag));
        mAdapter.getPhotoView(indexByAll).finishAnimationCallBack(onReleaseExitAnim());
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDataEvent(MessageEvent.AddNewDataEvent event) {
        isloading = false;
        nomore = !event.more;
        for (PhotoGroup mPhotoGroup : event.newData) {
            count += mPhotoGroup.getImages().size();
            nums.add(count);
            mRects.addAll(mPhotoGroup.getRects());
        }
        mPhotoGroups.addAll(event.newData);
        mAdapter.addNewData(mPhotoGroups, count);
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackNewRectEvent(Rect rect) {
        int size;
        if (indexByGroup >= 4)
            size = indexByAll - indexByGroup + 3;
        else
            size = indexByAll;
        mRects.set(size, rect);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.onCloseHandler();
        EventBus.getDefault().unregister(this);
    }
}
