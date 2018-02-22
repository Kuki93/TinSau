package com.example.helpme.mvpandroid.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.utils.DensityUtils;
import com.example.helpme.mvpandroid.utils.TimeUtils;

/**
 * @Created by helpme on 2018/2/3.
 * @Description
 */
public class SunriseView extends View {
    
    private Paint mDottedLinePaint;
    private Paint mLinePaint;
    private Paint mBottomLinePaint;
    private Paint mSunRaysPaint;
    private Paint mSunPaint;
    private Paint mCoverPaint;
    private Paint mReplenishPaint;
    private Paint mShadowPaint;
    private TextPaint mTextPaint;
    
    private int mWidth;
    private int mHeight;
    private float mRadius;
    private float mSunPercentage;
    private float mSunRadius;
    private String sunrise;
    private String sunset;
    private int px;
    private float textHight;
    private boolean isfinish;
    
    public SunriseView(Context context) {
        this(context, null);
    }
    
    public SunriseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public SunriseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SunriseView);
        mSunPercentage = array.getInteger(R.styleable.SunriseView_sunPercentage, 0);
        mSunRadius = array.getInteger(R.styleable.SunriseView_sunRadius, 30);
        mSunPercentage = Math.min(100, mSunPercentage);
        array.recycle();
        px = DensityUtils.dip2px(context, 14);
        init(context);
    }
    
    public void invalidateSunview() {
        if (sunrise == null || sunset == null)
            return;
        mSunPercentage = TimeUtils.getTimeDiffPercent(sunrise, sunset) * 100;
        if (isfinish && mSunPercentage >= 0 && mSunPercentage <= 100) {
            isfinish = false;
            mCurrentPercentage = -10;
            if (mSunPercentage < 0)
                mSunPercentage = 0;
            if (mSunPercentage > 100)
                mSunPercentage = 100;
            invalidate();
        }
    }
    
    public void setSunTime(String sunrise, String sunset) {
        this.sunrise = sunrise;
        this.sunset = sunset;
        if (sunrise == null || sunset == null)
            return;
        mSunPercentage = TimeUtils.getTimeDiffPercent(sunrise, sunset) * 100;
        if (isfinish && mSunPercentage >= 0 && mSunPercentage <= 100) {
            isfinish = false;
            mCurrentPercentage = -10;
            if (mSunPercentage < 0)
                mSunPercentage = 0;
            if (mSunPercentage > 100)
                mSunPercentage = 100;
            invalidate();
        }
    }
    
    public void setSunPercentage(int sunPercentage) {
        mSunPercentage = sunPercentage;
        mSunPercentage = Math.min(100, mSunPercentage);
    }
    
    public void setSunRadius(float sunRadius) {
        mSunRadius = sunRadius;
    }
    
    private void init(Context context) {
        isfinish = true;
        mDottedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDottedLinePaint.setARGB(255, 230, 230, 230);
        mDottedLinePaint.setStrokeWidth(8);
        mDottedLinePaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{15, 15}, 0);
        mDottedLinePaint.setPathEffect(effects);
        
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setARGB(255, 255, 203, 107);
        mLinePaint.setStrokeWidth(8);
        mLinePaint.setStyle(Paint.Style.STROKE);
        
        mBottomLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomLinePaint.setARGB(255, 254, 210, 125);
        mBottomLinePaint.setStrokeWidth(10);
        
        mSunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSunPaint.setStyle(Paint.Style.FILL);
        mSunPaint.setARGB(255, 233, 134, 104);
        
        mSunRaysPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSunRaysPaint.setARGB(255, 254, 209, 120);
        mSunRaysPaint.setStyle(Paint.Style.STROKE);
        mSunRaysPaint.setStrokeWidth(mSunRadius / 2.5f);
        PathEffect sunRaysEffects = new DashPathEffect(new float[]{mSunRadius / 7.5f, mSunRadius / 1.5f}, 0);
        mSunRaysPaint.setPathEffect(sunRaysEffects);
        
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setARGB(255, 255, 238, 205);
        mShadowPaint.setStyle(Paint.Style.FILL);
        
        mCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoverPaint.setStyle(Paint.Style.FILL);
        mCoverPaint.setARGB(0, 0, 0, 0);
        mCoverPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        
        mReplenishPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReplenishPaint.setARGB(255, 255, 238, 205);
        mReplenishPaint.setStyle(Paint.Style.FILL);
        
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextSize(DensityUtils.sp2px(context, 14));
        mTextPaint.setColor(0xff666666);
        textHight = mTextPaint.descent() + Math.abs(mTextPaint.ascent());
    }
    
    private float mCurrentPercentage = -10;
    
    private void drawSunView(Canvas canvas) {
        
        mCurrentPercentage += 0.5;
        if (mCurrentPercentage > 100) {
            isfinish = true;
        }
        
        float left = (mWidth - 2 * mRadius) / 2;
        float top = getPaddingTop() + mSunRadius / 2;
        float right = mWidth / 2 + mRadius;
        float bottom = top + 2 * mRadius;
        
        //矩形轮廓
        RectF dottedLineRectF = new RectF(left, top, right, bottom);
        float margin = mWidth / 16;
        dottedLineRectF.inset(margin, margin);
        
        //太阳扇形阴影
        float startAngle = 180;
        float sweepAngle = 180;
        //计算当前角度太阳的坐标
        Path path = new Path();
        path.addArc(dottedLineRectF, startAngle, sweepAngle);
        PathMeasure measure = new PathMeasure(path, false);
        sweepAngle = mCurrentPercentage * 1.8f;
        float[] sunXY = new float[2];
        measure.getPosTan(sweepAngle * measure.getLength() / 180, sunXY, null);
        int layerId = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        
        if (!isfinish)
            canvas.drawArc(dottedLineRectF, startAngle, sweepAngle, true, mShadowPaint);
        
        //太阳扇形阴影多余部分遮盖
        if (!isfinish)
            if (sweepAngle <= 90) {
                RectF rectF = new RectF(sunXY[0], sunXY[1], right, bottom - mRadius);
                canvas.drawRect(rectF, mCoverPaint);
            } else {
                Path trianglePath = new Path();
                trianglePath.moveTo(left + mRadius, bottom - mRadius);
                trianglePath.lineTo(sunXY[0], sunXY[1]);
                trianglePath.lineTo(sunXY[0], bottom - mRadius);
                canvas.drawPath(trianglePath, mReplenishPaint);
            }
        
        //太阳轨迹的虚线
        canvas.drawArc(dottedLineRectF, startAngle, 180, false, mDottedLinePaint);
        if (!isfinish)
            canvas.drawArc(dottedLineRectF, startAngle, sweepAngle, false, mLinePaint);
        
        //太阳
        if (mSunRadius < 30) mSunRadius = 30;
        if (!isfinish) {
            canvas.drawCircle(sunXY[0], sunXY[1], mSunRadius, mSunRaysPaint);
            canvas.drawCircle(sunXY[0], sunXY[1], mSunRadius - 12, mSunPaint);
        }
        //地平线
        canvas.drawLine(0, bottom - mRadius, mWidth, bottom - mRadius, mBottomLinePaint);
        
        //日出或日落半个太阳的遮罩层
        canvas.drawRect(0, bottom - mRadius + 5, mWidth, mHeight, mCoverPaint);
        if (sunrise != null)
            canvas.drawText(sunrise, left + margin, bottom - mRadius + textHight, mTextPaint);
        if (sunset != null)
            canvas.drawText(sunset, right - mTextPaint.measureText(sunset) - margin, bottom - mRadius + textHight,
                    mTextPaint);
        canvas.restoreToCount(layerId);
        if (mCurrentPercentage <= mSunPercentage) {
            invalidate();
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSunView(canvas);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = measure(widthMeasureSpec, true);
        mHeight = measure(heightMeasureSpec, false);
        if (mWidth == 0) {
            mRadius = mHeight - getPaddingTop() - getPaddingBottom() - textHight - mSunRadius - 20;
            mWidth = (int) (mRadius * 2 + px + getPaddingLeft() + getPaddingRight());
        } else if (mHeight == 0) {
            mRadius = (mWidth - getPaddingLeft() - getPaddingRight() - px) / 2;
            mHeight = (int) (mRadius + textHight + mSunRadius + getPaddingTop() + getPaddingBottom());
        } else {
            mRadius = Math.min(mHeight - getPaddingTop() - getPaddingBottom() - textHight - mSunRadius, (mWidth -
                    getPaddingLeft() - getPaddingRight() - px) / 2);
        }
        setMeasuredDimension(mWidth, mHeight);
    }
    
    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }
}