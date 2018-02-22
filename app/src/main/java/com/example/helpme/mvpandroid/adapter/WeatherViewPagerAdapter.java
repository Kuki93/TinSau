package com.example.helpme.mvpandroid.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.helpme.mvpandroid.module.weather.WeatherFragment;

import java.util.List;


/**
 * @Created by helpme on 2018/1/24.
 * @Description 天气Viewpager Adapter
 */
public class WeatherViewPagerAdapter extends FragmentStatePagerAdapter {
    
    
    private List<WeatherFragment> myFragments;
    
    public WeatherViewPagerAdapter(FragmentManager fm, List<WeatherFragment> myFrags) {
        super(fm);
        myFragments = myFrags;
    }
    
    @Override
    public WeatherFragment getItem(int position) {
        return myFragments.get(position);
    }
    
    @Override
    public int getCount() {
        if (myFragments == null)
            return 0;
        else
            return myFragments.size();
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
    
}
