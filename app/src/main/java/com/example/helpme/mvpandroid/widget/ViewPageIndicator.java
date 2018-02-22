package com.example.helpme.mvpandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.example.helpme.mvpandroid.utils.DensityUtils;


/**
 * @Created by helpme on 2018/1/23.
 * @Description viewpager 圆形指示器
 */
public class ViewPageIndicator extends View implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    
    private Paint roundPaint;
    
    private Paint selectedRoundPaint;
    
    private float roundRadius;
    
    private float roundPadding = 10.0f;
    
    float roundBaseline;
    
    private int selectedPosition = 0;
    
    private float positionOffset = 0.0f;
    
    private int count;
    
    private Path path;
    
    private boolean isShow, isOpen;
    
    public void setOpen(boolean open) {
        isOpen = open;
    }
    
    public ViewPageIndicator(Context context) {
        this(context, null);
    }
    
    public ViewPageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ViewPageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isOpen = true;
        path = new Path();
        roundRadius = 5;
        roundPaint = new Paint();
        roundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        roundPaint.setStrokeWidth(2);
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setAlpha(100);
        
        selectedRoundPaint = new Paint();
        selectedRoundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedRoundPaint.setStrokeWidth(2);
        selectedRoundPaint.setColor(Color.WHITE);
        selectedRoundPaint.setAntiAlias(true);
    }
    
    private void onDrawLocation(float startX, float offSetX) {
        path.reset();
        path.moveTo(startX + offSetX, roundBaseline);
        path.lineTo(startX - roundRadius + offSetX, roundBaseline - roundRadius * new Double
                (Math.tan(Math.PI / 18)).floatValue());
        path.lineTo(startX + roundRadius + offSetX, roundBaseline - roundRadius);
        path.lineTo(startX + roundRadius * new Double(Math.tan
                (Math.PI / 18)).floatValue() + offSetX, roundBaseline + roundRadius);
        path.lineTo(startX + offSetX, roundBaseline);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (count == 0 || !isShow)
            return;
        roundBaseline = roundRadius + 3;
        
        float roundTotalWidth = 2 * roundRadius * count + (count - 1) * roundPadding;
        float startX = getWidth() / 2 - roundTotalWidth / 2 + roundRadius;
        for (int i = 0; i < count; i++) {
            if (i == 0 && isOpen) {
                onDrawLocation(startX, 0);
                canvas.drawPath(path, roundPaint);
            } else
                canvas.drawCircle(startX + (2 * roundRadius * i) + roundPadding * i,
                        roundBaseline, roundRadius, roundPaint);
        }
        
        float offSetX = (roundPadding + 2 * roundRadius) * positionOffset;
        if (selectedPosition == 0 && isOpen) {
            onDrawLocation(startX, offSetX);
            canvas.drawPath(path, selectedRoundPaint);
        } else
            canvas.drawCircle(startX + (2 * roundRadius * selectedPosition) + roundPadding *
                            selectedPosition + offSetX,
                    roundBaseline, roundRadius, selectedRoundPaint);
        
        
    }
    
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.addOnPageChangeListener(this);
        notifyDataSetChanged(0);
    }
    
    public ViewPager getViewPager() {
        return mViewPager;
    }
    
    public void notifyDataSetChanged(int page) {
        count = mViewPager.getAdapter().getCount();
        selectedPosition = page;
        invalidate();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        int desiredWidth = DensityUtils.dip2px(getContext(), 100);
        int desiredHeight = DensityUtils.dip2px(getContext(), 48);
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int width;
        int height;
        
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }
        
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        
        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position >= selectedPosition) {
            // 1 -> 2 -> 3 滑动
            this.positionOffset = positionOffset;
        } else if (position < selectedPosition) {
            // 1 <- 2 <- 3 滑动
            this.positionOffset = -(1 - positionOffset);
        }
        invalidate();
    }
    
    @Override
    public void onPageSelected(int position) {
        selectedPosition = position;
        invalidate();
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            isShow = true;
        } else if (state == 0) {
            isShow = false;
            postInvalidateDelayed(800);
        }
    }
}

