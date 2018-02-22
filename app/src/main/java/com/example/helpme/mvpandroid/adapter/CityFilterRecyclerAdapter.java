package com.example.helpme.mvpandroid.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.entity.weather.AllCityCode;

import java.util.List;

/**
 * @Created by helpme on 2018/2/1.
 * @Description
 */
public class CityFilterRecyclerAdapter  extends BaseQuickAdapter<AllCityCode, BaseViewHolder> {
    
    public CityFilterRecyclerAdapter(int layoutResId, @Nullable List<AllCityCode> data) {
        super(layoutResId, data);
    }
    
    @Override
    protected void convert(BaseViewHolder helper, AllCityCode item) {
        helper.setText(R.id.tv_suggestion, item.getCountyname());
    }
}
