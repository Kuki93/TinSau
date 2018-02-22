package com.example.helpme.mvpandroid.widget.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.utils.DensityUtils;

/**
 * @Created by helpme on 2018/1/25.
 * @Description
 */
public class SunView extends View {
    
    public static final String TAG = SunView.class.getSimpleName();
    
    private Paint mPaint, mAlphaPaint;
    //画笔的宽度dp
    private int strokeWidth = 5;
    //半径dp
    private int raduis = 13;
    //间距px
    private final int space = 8;
    
    private int count;
    private boolean isAnim;
    private Animation rotateAnim;
    
    public void setAnim(boolean anim) {
        isAnim = anim;
    }
    
    public SunView(Context context) {
        this(context, null);
    }
    
    public SunView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public SunView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        strokeWidth = DensityUtils.dip2px(context, strokeWidth);
        raduis = DensityUtils.dip2px(context, raduis);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mAlphaPaint = new Paint();
        mAlphaPaint.setColor(Color.WHITE);
        mAlphaPaint.setStrokeWidth(strokeWidth);
        mAlphaPaint.setStyle(Paint.Style.STROKE);
        mAlphaPaint.setAntiAlias(true);
        mAlphaPaint.setAlpha(100);
        count = 7;
        rotateAnim = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnim.setInterpolator(lin);
    }
    
    void startRotateAnim() {
        startAnimation(rotateAnim);
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int width = raduis * 2 + strokeWidth * 3 + space * 2 + 20;
        int height = raduis * 2 + strokeWidth * 3 + space * 2 + 4;
        
        
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(width, widthSize);
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
    }
    
    private void drawLine(Canvas canvas, Paint paint, int n) {
        int l = raduis + strokeWidth / 2 + space;
        int x = getWidth() / 2 + (int) (l * Math.sin(-(n + 4) * Math.PI / 4));
        int y = getHeight() / 2 + (int) (l * Math.cos(-(n + 4) * Math.PI / 4));
        int ll = raduis + strokeWidth * 3 / 2 + space;
        int xl = getWidth() / 2 + (int) (ll * Math.sin(-(n + 4) * Math.PI / 4));
        int yl = getHeight() / 2 + (int) (ll * Math.cos(-(n + 4) * Math.PI / 4));
        canvas.drawLine(x, y, xl, yl, paint);
    }
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, raduis,
                mPaint);
        for (int i = 0; i < count; i++) {
            if (i >= 8)
                break;
            if (i == count - 1 && i != 0 && !isAnim)
                drawLine(canvas, mAlphaPaint, i);
            else
                drawLine(canvas, mPaint, i);
        }
    }
    
}
