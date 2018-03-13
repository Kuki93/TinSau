package com.example.helpme.mvpandroid.widget.loading;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.helpme.mvpandroid.R;

public class LoadingIndicatorView extends View {
    
    private static final String TAG = "LoadingIndicatorView";
    
    private static final BallPulseIndicator DEFAULT_INDICATOR = new BallPulseIndicator();
    
    int mMinWidth;
    int mMaxWidth;
    int mMinHeight;
    int mMaxHeight;
    
    private Indicator mIndicator;
    private int mIndicatorColor;
    
    private boolean mShouldStartAnimationDrawable;
    
    public LoadingIndicatorView(Context context) {
        this(context, null);
    }
    
    public LoadingIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    
    public LoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    
    private void init(Context context) {
        mMinWidth = 60;
        mMaxWidth = 60;
        mMinHeight = 72;
        mMaxHeight = 72;
        
        mIndicatorColor = context.getResources().getColor(R.color.colorYellow);
        setIndicator(DEFAULT_INDICATOR);
    }
    
    public Indicator getIndicator() {
        return mIndicator;
    }
    
    public void setIndicator(Indicator d) {
        if (mIndicator != d) {
            if (mIndicator != null) {
                mIndicator.setCallback(null);
                unscheduleDrawable(mIndicator);
            }
            
            mIndicator = d;
            //need to set indicator color again if you didn't specified when you update the indicator .
            setIndicatorColor(mIndicatorColor);
            if (d != null) {
                d.setCallback(this);
            }
            postInvalidate();
        }
    }
    
    
    /**
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(Color.BLUE)
     * or
     * setIndicatorColor(Color.parseColor("#FF4081"))
     * or
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(getResources().getColor(android.R.color.black))
     *
     * @param color
     */
    public void setIndicatorColor(int color) {
        this.mIndicatorColor = color;
        mIndicator.setColor(color);
    }
    
    
    public void smoothToShow() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        setVisibility(VISIBLE);
    }
    
    public void smoothToHide() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        setVisibility(GONE);
    }
    
    public void hide() {
       setVisibility(View.GONE);
    }
    
    
    public void show() {
        // Reset the start time.
       setVisibility(VISIBLE);
       invalidate();
    }
    
    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mIndicator
                || super.verifyDrawable(who);
    }
    
    public void startAnimation() {
        if (mIndicator instanceof Animatable) {
            mShouldStartAnimationDrawable = true;
        }
        postInvalidate();
    }
    
    public void stopAnimation() {
        if (mIndicator instanceof Animatable) {
            mIndicator.stop();
            mShouldStartAnimationDrawable = false;
        }
        if (mIndicator instanceof LineScaleIndicator) {
            ((LineScaleIndicator) mIndicator).setScaleYFloats();
        }
        postInvalidate();
    }
    
    @Override
    public void setVisibility(int v) {
        if (v == GONE || v == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
        if (getVisibility() != v) {
            super.setVisibility(v);
        }
    }
    
//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//        if (visibility == GONE || visibility == INVISIBLE) {
//            stopAnimation();
//        } else {
//            startAnimation();
//        }
//    }
    
    @Override
    public void invalidateDrawable(Drawable dr) {
        if (verifyDrawable(dr)) {
            final Rect dirty = dr.getBounds();
            final int scrollX = getScrollX() + getPaddingLeft();
            final int scrollY = getScrollY() + getPaddingTop();
            
            invalidate(dirty.left + scrollX, dirty.top + scrollY,
                    dirty.right + scrollX, dirty.bottom + scrollY);
        } else {
            super.invalidateDrawable(dr);
        }
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateDrawableBounds(w, h);
    }
    
    private void updateDrawableBounds(int w, int h) {
        // onDraw will translate the canvas so we draw starting at 0,0.
        // Subtract out padding for the purposes of the calculations below.
        w -= getPaddingRight() + getPaddingLeft();
        h -= getPaddingTop() + getPaddingBottom();
        
        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;
        
        if (mIndicator != null) {
            // Maintain aspect ratio. Certain kinds of animated drawables
            // get very confused otherwise.
            final int intrinsicWidth = mIndicator.getIntrinsicWidth();
            final int intrinsicHeight = mIndicator.getIntrinsicHeight();
            final float intrinsicAspect = (float) intrinsicWidth / intrinsicHeight;
            final float boundAspect = (float) w / h;
            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    // New width is larger. Make it smaller to match height.
                    final int width = (int) (h * intrinsicAspect);
                    left = (w - width) / 2;
                    right = left + width;
                } else {
                    // New height is larger. Make it smaller to match width.
                    final int height = (int) (w * (1 / intrinsicAspect));
                    top = (h - height) / 2;
                    bottom = top + height;
                }
            }
            mIndicator.setBounds(left, top, right, bottom);
        }
    }
    
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }
    
    void drawTrack(Canvas canvas) {
        final Drawable d = mIndicator;
        if (d != null) {
            // Translate canvas so a indeterminate circular progress bar with padding
            // rotates properly in its animation
            final int saveCount = canvas.save();
            
            canvas.translate(getPaddingLeft(), getPaddingTop());
            
            d.draw(canvas);
            canvas.restoreToCount(saveCount);
            
            if (mShouldStartAnimationDrawable && d instanceof Animatable) {
                ((Animatable) d).start();
                mShouldStartAnimationDrawable = false;
            }
        }
    }
    
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;
        
        final Drawable d = mIndicator;
        if (d != null) {
            dw = Math.max(mMinWidth, Math.min(mMaxWidth, d.getIntrinsicWidth()));
            dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()));
        }
        
        updateDrawableState();
        
        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingBottom();
        
        final int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
    
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }
    
    private void updateDrawableState() {
        final int[] state = getDrawableState();
        if (mIndicator != null && mIndicator.isStateful()) {
            mIndicator.setState(state);
        }
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        
        if (mIndicator != null) {
            mIndicator.setHotspot(x, y);
        }
    }
    
}
