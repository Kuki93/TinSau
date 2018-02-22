package com.example.helpme.mvpandroid.utils;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

/**
 * @Created by helpme on 2018/1/24.
 * @Description
 */
public class WeatherPageTransformer implements PageTransformer{
    
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            page.setAlpha(0);
        } else if (position <= 0) {
            page.setAlpha(1 + position);
        } else if (position <= 1) {
            page.setAlpha(1 - position);
        } else {
            page.setAlpha(0);
        }
        page.setTranslationX(position);
    }

}
