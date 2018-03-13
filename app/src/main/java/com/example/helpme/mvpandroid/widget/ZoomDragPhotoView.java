package com.example.helpme.mvpandroid.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.helpme.mvpandroid.contract.ImageContract;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.nineoldandroids.view.ViewHelper;

/**
 * @Created by helpme on 2018/2/19.
 * @Description
 */
public class ZoomDragPhotoView extends PhotoView {
    // downX
    private float mDownX;
    // down Y
    private float mDownY;
    
    private float mTranslateY;
    private float mTranslateX;
    private float mScaleX = 1;
    private float mScaleY = 1;
    private int mWidth;
    private int mHeight;
    private float mMinScale = 0.5f;
    private int mAlpha = 100;
    private int mTouchSlop;
    private int MAX_TRANSLATE_Y;
    private int MAX_SCALE_Y;
    
    private static long DURATION = 500;
    private boolean canFinish = false;
    
    //is event on PhotoView
    private boolean isTouchEvent = false;
    private boolean init;
    private ImageContract.OnDragPhototListener mOnDragPhototListener;
    
    public void setMAX_TRANSLATE_Y(int MAX_TRANSLATE_Y) {
        this.MAX_TRANSLATE_Y = MAX_TRANSLATE_Y;
    }
    
    public void setMAX_SCALE_Y(int MAX_SCALE_Y) {
        this.MAX_SCALE_Y = MAX_SCALE_Y;
    }
    
    public void setOnDragPhototListener(ImageContract.OnDragPhototListener onDragPhototListener) {
        mOnDragPhototListener = onDragPhototListener;
    }
    
    public ZoomDragPhotoView(Context context) {
        this(context, null);
    }
    
    public ZoomDragPhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }
    
    public ZoomDragPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init = true;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        MAX_TRANSLATE_Y = DensityUtils.dip2px(getContext(), 120);
        MAX_SCALE_Y = DensityUtils.getScreenHeight(getContext()) / 2 + mHeight / 2;
        if (mOnDragPhototListener != null && init) {
            init = false;
            AddParentObserver(mOnDragPhototListener.onReleaseExitAnim());
        }
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //only scale == 1 can drag
        if (getScale() == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onActionDown(event);
                    //change the canFinish flag
                    isTouchEvent = false;
                    canFinish = !canFinish;
                    if (event.getPointerCount() == 2) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveY = event.getRawY();
                    float moveX = event.getRawX();
                    mTranslateX = moveX - mDownX;
                    mTranslateY = moveY - mDownY;
                    //in viewpager
                    if ((mTranslateY >= mTouchSlop && Math.abs(mTranslateX) < mTouchSlop || isTouchEvent) && event
                            .getPointerCount() == 1 && mOnDragPhototListener.isAllowSwipe()) {
                        if (!isTouchEvent) {
                            isTouchEvent = true;
                            if (mOnDragPhototListener != null)
                                mOnDragPhototListener.onStartSlide();
                        }
                        float percent = mTranslateY < 0 ? 0 : mTranslateY / MAX_SCALE_Y;
                        if (mScaleX >= mMinScale && mScaleX <= 1f) {
                            mScaleX = 1 - percent;
                            
                            mAlpha = (int) (255 * (1 - percent));
                            if (mAlpha > 255) {
                                mAlpha = 255;
                            } else if (mAlpha < 0) {
                                mAlpha = 0;
                            }
                        }
                        if (mScaleX < mMinScale) {
                            mScaleX = mMinScale;
                        } else if (mScaleX > 1f) {
                            mScaleX = 1;
                        }
                        mScaleY = mScaleX + 0;
                        if (mOnDragPhototListener != null)
                            mOnDragPhototListener.onKeepSlideing(mAlpha);
                        onActionMove();
                        return true;
                    } else {
                        if (event.getPointerCount() == 2)
                            getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                
                case MotionEvent.ACTION_UP:
                    //防止下拉的时候双手缩放
                    if (event.getPointerCount() == 1) {
                        onActionUp();
                        //judge finish or not
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mTranslateX == 0 && mTranslateY == 0 && canFinish) {
                                    if (mOnDragPhototListener != null && mOnDragPhototListener.onTapExit()) {
                                        finishAnimationCallBack(mOnDragPhototListener.onReleaseExitAnim());
                                    }
                                }
                                canFinish = false;
                            }
                        }, 300);
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }
    
    
    private void onActionUp() {
        if (!isTouchEvent)
            return;
        if (mTranslateY > MAX_TRANSLATE_Y) {
            if (mOnDragPhototListener != null) {
                finishAnimationCallBack(mOnDragPhototListener.onReleaseExitAnim());
            } else {
                throw new RuntimeException("DragPhotoView: onExitLister can't be null ! call setOnExitListener() ");
            }
        } else {
            performAnimation();
        }
    }
    
    private void onActionMove() {
        setScaleX(mScaleX);
        setScaleY(mScaleY);
        ViewHelper.setTranslationY(this, mTranslateY);
        ViewHelper.setTranslationX(this, mTranslateX);
    }
    
    private void performAnimation() {
        getAlphaAnimation(255, true).start();
        getScaleXAnimation(1).start();
        getScaleYAnimation(1).start();
        getTranslateXAnimation(0).start();
        getTranslateYAnimation(0).start();
    }
    
    private ValueAnimator getAlphaAnimation(int targetAlpha) {
        return getAlphaAnimation(targetAlpha, false);
    }
    
    private ValueAnimator getAlphaAnimation(int targetAlpha, final boolean callback) {
        final ValueAnimator animator = ValueAnimator.ofInt(mAlpha, targetAlpha);
        animator.setDuration(DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mOnDragPhototListener != null && callback)
                    mOnDragPhototListener.onUpdateExitAnim((int) valueAnimator.getAnimatedValue());
            }
        });
        return animator;
    }
    
    private ValueAnimator getTranslateXAnimation(float targetX) {
        return getTranslateXAnimation(targetX, false);
    }
    
    private ValueAnimator getTranslateXAnimation(float targetX, final boolean callback) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mTranslateX, targetX);
        animator.setDuration(DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTranslateX = (float) valueAnimator.getAnimatedValue();
            }
        });
        return animator;
    }
    
    private ValueAnimator getTranslateYAnimation(float targetY) {
        return getTranslateYAnimation(targetY, false);
    }
    
    private ValueAnimator getTranslateYAnimation(float targetY, final boolean callback) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mTranslateY, targetY);
        animator.setDuration(DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTranslateY = (float) valueAnimator.getAnimatedValue();
            }
        });
        return animator;
    }
    
    private ValueAnimator getScaleXAnimation(float targetScaleX) {
        return getScaleXAnimation(targetScaleX, false, null);
    }
    
    private ValueAnimator getScaleXAnimation(float targetScaleX, final boolean callback, final Rect rect) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mScaleX, targetScaleX);
        animator.setDuration(DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScaleX = (float) valueAnimator.getAnimatedValue();
                onActionMove();
            }
        });
        
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            
            @Override
            public void onAnimationEnd(Animator animator) {
                if (mOnDragPhototListener != null && callback)
                    mOnDragPhototListener.onEndExitAnim(rect);
                animator.removeAllListeners();
            }
            
            @Override
            public void onAnimationCancel(Animator animator) {
                
            }
            
            @Override
            public void onAnimationRepeat(Animator animator) {
                
            }
        });
        return animator;
    }
    
    private ValueAnimator getScaleYAnimation(float targetScaleY) {
        return getScaleYAnimation(targetScaleY, false);
    }
    
    private ValueAnimator getScaleYAnimation(float targetScaleY, final boolean callback) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mScaleY, targetScaleY);
        animator.setDuration(DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScaleY = (float) valueAnimator.getAnimatedValue();
            }
        });
        return animator;
    }
    
    private void onActionDown(MotionEvent event) {
        mDownX = event.getRawX();
        mDownY = event.getRawY();
    }
    
    
    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }
    
    private void getNotNullRect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                long lastTime = time;
                Rect rect = mOnDragPhototListener.onReleaseExitAnim();
                while (rect == null) {
                    rect = mOnDragPhototListener.onReleaseExitAnim();
                    lastTime = System.currentTimeMillis();
                    if (lastTime - time > DURATION) {
                        rect = mOnDragPhototListener.getDefaultRect();
                        break;
                    }
                }
                DURATION = Math.max(1, DURATION - (lastTime - time));
                finishAnimationCallBack(rect);
                DURATION = 500;
            }
        }).start();
    }
    
    public void finishAnimationCallBack(final Rect rect) {
        if (rect == null) {
            getNotNullRect();
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                float scaleX = rect.width() * 1.0f / mWidth;
                float scaleY = rect.height() * 1.0f / mHeight;
                float translateX = rect.left - getLeft() + (rect.width() - mWidth) * 1.0f / 2;
                float translateY = rect.top - getTop() + (rect.height() - mHeight) * 1.0f / 2;
                getScaleXAnimation(scaleX, true, rect).start();
                getScaleYAnimation(scaleY, true).start();
                getAlphaAnimation(0, true).start();
                getTranslateXAnimation(translateX, true).start();
                getTranslateYAnimation(translateY, true).start();
            }
        });
    }
    
    private void AddParentObserver(final Rect rect) {
        final ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup == null || rect == null)
            return;
        viewGroup.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        
                        float mScaleX;
                        float mScaleY;
                        float mTranslationX;
                        float mTranslationY;
                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        
                        int[] location = new int[2];
                        getLocationOnScreen(location);
                        
                        mScaleX = (float) rect.width() * 1.0f / mWidth;
                        mScaleY = (float) rect.height() * 1.0f / mHeight;
                        
                        float targetCenterX = location[0] + mWidth / 2;
                        float targetCenterY = location[1] + mHeight / 2;
                        
                        mTranslationX = rect.centerX() - targetCenterX;
                        mTranslationY = rect.centerY() - targetCenterY;
                        setTranslationX(mTranslationX);
                        setTranslationY(mTranslationY);
                        setMinimumScale(Math.min(mScaleX, mScaleY));
                        
                        mTranslationX = (DensityUtils.getScreenWidth(getContext()) - mWidth) / 2;
                        mTranslationY = (DensityUtils.getScreenHeight(getContext()) - mHeight) / 2;
                        performEnterAnimation(mTranslationX, mTranslationY, mScaleX, mScaleY);
                    }
                });
    }
    
    public void performEnterAnimation(float mTranslationX, float mTranslationY, float mScaleX, float mScaleY) {
        
        ValueAnimator translateXAnimator = ValueAnimator.ofFloat(getX(), mTranslationX);
        translateXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setX((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateXAnimator.setDuration(DURATION);
        translateXAnimator.start();
        
        ValueAnimator translateYAnimator = ValueAnimator.ofFloat(getY(), mTranslationY);
        translateYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateYAnimator.setDuration(DURATION);
        translateYAnimator.start();
        
        ValueAnimator scaleYAnimator = ValueAnimator.ofFloat(mScaleY, 1);
        scaleYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleY((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleYAnimator.setDuration(DURATION);
        scaleYAnimator.start();
        
        ValueAnimator scaleXAnimator = ValueAnimator.ofFloat(mScaleX, 1);
        scaleXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleX((Float) valueAnimator.getAnimatedValue());
            }
        });
        scaleXAnimator.setDuration(DURATION);
        scaleXAnimator.start();
    }
}