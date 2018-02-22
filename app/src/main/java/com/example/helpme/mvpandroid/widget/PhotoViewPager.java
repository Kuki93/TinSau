package com.example.helpme.mvpandroid.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Created by helpme on 2018/2/12.
 * @Description
 */
public class PhotoViewPager extends ViewPager {
    
    public PhotoViewPager(Context context) {
        super(context);
    }
    
    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}