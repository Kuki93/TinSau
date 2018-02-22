package com.example.helpme.mvpandroid.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by helpme on 2018/1/25.
 * @Description
 */
public class FallingView extends SurfaceView implements SurfaceHolder.Callback {
    
    
    private List<FallObject> fallObjects;
    
    private int viewWidth;
    private int viewHeight;
    
    private static final int defaultWidth = 600;//默认宽度
    private static final int defaultHeight = 1000;//默认高度
    private static final int intervalTime = 12;//重绘间隔时间
    
    private DrawThread mDrawThread;
    private SurfaceHolder mHolder;
    
    public FallingView(Context context) {
        this(context, null);
    }
    
    public FallingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public FallingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);//使surfaceview放到最顶层  
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度  
    }
    
    private void init() {
        fallObjects = new ArrayList<>();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultHeight, heightMeasureSpec);
        int width = measureSize(defaultWidth, widthMeasureSpec);
        setMeasuredDimension(width, height);
        
        viewWidth = width;
        viewHeight = height;
    }
    
    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }
    
    protected void drawView(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (fallObjects.size() > 0) {
            for (int i = 0; i < fallObjects.size(); i++) {
                //然后进行绘制
                fallObjects.get(i).drawObject(canvas);
            }
        }
    }
    
    
    /**
     * 向View添加下落物体对象
     *
     * @param builder
     * @param num
     */
    public void addFallObject(final FallObject.Builder builder, final int num) {
        fallObjects.clear();
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < num; i++) {
                    FallObject newFallObject = new FallObject(builder, viewWidth,
                            viewHeight);
                    fallObjects.add(newFallObject);
                }
                return true;
            }
        });
    }
    
    public void start() {
        if (mDrawThread != null && mDrawThread.isRunning)
            mDrawThread.setRunning(false);
        mDrawThread = new DrawThread();
        mDrawThread.setRunning(true);
        mDrawThread.start();
        invalidate();
    }
    
    public void stop() {
        if (mDrawThread != null && mDrawThread.isRunning)
            mDrawThread.setRunning(false);
    }
    
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }
    
    /**
     * 绘制线程
     */
    private class DrawThread extends Thread {
        
        // 用来停止线程的标记
        private boolean isRunning = false;
        
        public void setRunning(boolean running) {
            isRunning = running;
        }
        
        public boolean isRunning() {
            return isRunning;
        }
        
        @Override
        public void run() {
            Canvas canvas;
            // 无限循环绘制
            while (isRunning) {
                if (viewWidth != 0 && viewHeight != 0) {
                    canvas = mHolder.lockCanvas();
                    if (canvas != null) {
                        drawView(canvas);
                        mHolder.unlockCanvasAndPost(canvas);
                        // sleep
                        SystemClock.sleep(intervalTime);
                    }
                }
            }
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
