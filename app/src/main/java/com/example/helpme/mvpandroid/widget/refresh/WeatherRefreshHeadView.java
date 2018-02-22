package com.example.helpme.mvpandroid.widget.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * @Created by helpme on 2018/1/25.
 * @Description
 */
public class WeatherRefreshHeadView extends LinearLayout implements RefreshHeader {
    
    public static final String TAG = WeatherRefreshHeadView.class.getSimpleName();
    
    // 文字画笔
    private TextPaint mTextPaint;
    //间距px
    private final int space = 8;
    // 上边距
    private int minPaddingTop = 20;
    // 下边距
    private int minPaddingBottom = 20;
    private String msg = "";
    
    private int delay, dy;
    
    SunView sunView;
    
    public WeatherRefreshHeadView(Context context) {
        this(context, null);
    }
    
    public WeatherRefreshHeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public WeatherRefreshHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setGravity(Gravity.CENTER_HORIZONTAL);
        minPaddingTop = DensityUtils.dip2px(context, minPaddingTop);
        minPaddingBottom = DensityUtils.dip2px(context, minPaddingBottom);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DensityUtils.sp2px(context, 14));
        mTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mTextPaint.setColor(Color.WHITE);
        sunView = new SunView(context);
        addView(sunView);
        setPadding(0, minPaddingTop, 0, minPaddingBottom);
    }
    
    private void setAlphaPaint(int Alpha) {
        mTextPaint.setAlpha(Alpha);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        minPaddingTop = Math.max(minPaddingTop, getPaddingTop());
        minPaddingBottom = Math.max(minPaddingBottom, getPaddingBottom());
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        measureChild(sunView, widthMeasureSpec, heightMeasureSpec);
        int width = sunView.getMeasuredWidth() + 20;
        int height = (int) (sunView.getMeasuredHeight() + space + mTextPaint.descent() +
                Math.abs(mTextPaint.ascent()) + minPaddingTop + minPaddingBottom);
        
        
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.max(width, widthSize);
        }
        
        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(height, heightSize);
        }
        //MUST CALL THIS
        setMeasuredDimension(width, height);
        delay = (int) ((height - (sunView.getMeasuredHeight() + space + mTextPaint.descent() +
                Math.abs(mTextPaint.ascent()))) / 2);
        dy = height - delay - space * 2;
    }
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 计算Baseline绘制的起点X轴坐标 ，计算方式：画布宽度的一半 - 文字宽度的一半
        float baseX = canvas.getWidth() / 2 - mTextPaint.measureText(msg) / 2;
        // 计算Baseline绘制的Y坐标 ，计算方式：画布高度的一半 - 文字总高度的一半
        float baseY = minPaddingTop + sunView.getHeight() + space +
                mTextPaint.descent() + Math.abs(mTextPaint.ascent());
        
        canvas.drawText(msg, baseX, baseY, mTextPaint);
    }
    
    
    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        int m = (getHeight() - dy) / 8;
        if (offset > dy) {
            sunView.setCount(Math.min((offset - dy) / m, 9));
        } else {
            sunView.setCount(0);
        }
        float p = 175.0f / (getHeight() - delay);
        if (offset >= delay && offset <= getHeight()) {
            setAlphaPaint((int) (80 + (offset - delay) * p));
        }
        sunView.invalidate();
        invalidate();
    }
    
    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
    }
    
    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
        invalidate();
    }
    
    @NonNull
    @Override
    public View getView() {
        return this;
    }
    
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }
    
    @Override
    public void setPrimaryColors(int... colors) {
        
    }
    
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        
    }
    
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
        
    }
    
    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {
        sunView.setAnim(true);
        if (sunView.getCount() < 8)
            sunView.setCount(9);
        sunView.startRotateAnim();
    }
    
    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        sunView.setAnim(false);
        sunView.setCount(0);
        sunView.clearAnimation();
        if (success) {
            msg = "更新成功";
        } else {
            msg = "更新失败";
        }
        postInvalidate();
        return 500;
    }
    
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
    
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState
            newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                msg = "下拉刷新";
                break;
            case Refreshing:
                msg = "加载中";
                break;
            case ReleaseToRefresh:
                msg = "释放更新";
                break;
        }
    }
}
