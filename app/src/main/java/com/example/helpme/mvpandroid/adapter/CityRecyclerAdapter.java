package com.example.helpme.mvpandroid.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.entity.weather.CityInfo;

import java.util.List;

/**
 * @Created by helpme on 2018/1/27.
 * @Description
 */
public class CityRecyclerAdapter extends BaseItemDraggableAdapter<CityInfo, BaseViewHolder> {
    
    
    private OnDataChangeListener mOnDataChangeListener;
    
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }
    
    public interface OnDataChangeListener {
        void onWeather(long cityId);
        
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
        
        void ondeleteItem(int index);
    }
    
    public CityRecyclerAdapter(@Nullable List<CityInfo> data) {
        super(data);
        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<CityInfo>() {
            @Override
            protected int getItemType(CityInfo entity) {
                //根据你的实体类来判断布局类型
                return entity.getType();
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(CityInfo.TYPE_LOCATION_ON, R.layout.item_city_other_view)
                .registerItemType(CityInfo.TYPE_LOCATION_OFF, R.layout.item_city_other_view)
                .registerItemType(CityInfo.TYPE_NORMAL, R.layout.item_city_view);
    }
    
    @Override
    protected void convert(final BaseViewHolder helper, CityInfo item) {
        switch (helper.getItemViewType()) {
            case CityInfo.TYPE_LOCATION_ON:
            case CityInfo.TYPE_LOCATION_OFF:
                if (item.getType() == CityInfo.TYPE_LOCATION_ON) {
                    if (item.getTemperature() == null) {
                        helper.setText(R.id.city, item.getCity()).setChecked(R.id.mswitch, true)
                                .setImageResource(R.id.typeImg, getDrawableId(item.getWeather()));
                        if (item.getWeather() != null) {
                            helper.setText(R.id.cityType, item.getWeather());
                            helper.getView(R.id.cityType).setVisibility(View.VISIBLE);
                        } else
                            helper.getView(R.id.cityType).setVisibility(View.GONE);
                    } else {
                        helper.getView(R.id.cityType).setVisibility(View.VISIBLE);
                        helper.setText(R.id.city, item.getCity()).setText(R.id.cityType, item
                                .getWeather() + " " + item.getTemperature() + "°").setChecked
                                (R.id.mswitch, true).setImageResource(R.id.typeImg, getDrawableId(item.getWeather()));
                    }
                } else {
                    helper.getView(R.id.cityType).setVisibility(View.GONE);
                    helper.setText(R.id.city, item.getCity()).setChecked(R.id.mswitch, false).
                            setImageResource(R.id.typeImg, getDrawableId(item.getWeather()));
                }
                ((Switch) helper.getView(R.id.mswitch)).setOnCheckedChangeListener(new CompoundButton
                        .OnCheckedChangeListener() {
                    
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (mOnDataChangeListener != null)
                            mOnDataChangeListener.onCheckedChanged(buttonView, isChecked);
                    }
                });
                break;
            case CityInfo.TYPE_NORMAL:
                if (item.getWeather() == null) {
                    helper.setText(R.id.city, item.getCity()).setText(R.id.cityType, "暂无")
                            .setImageResource(R.id.typeImg, getDrawableId(item.getWeather()));
                    if (mOnDataChangeListener != null)
                        mOnDataChangeListener.onWeather(item.getCityId());
                } else {
                    helper.setText(R.id.city, item.getCity()).setText(R.id.cityType, item
                            .getWeather() + " " + item.getTemperature() + "°")
                            .setImageResource(R.id.typeImg, getDrawableId(item.getWeather()));
                }
                helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDataChangeListener != null)
                            mOnDataChangeListener.ondeleteItem(helper.getLayoutPosition());
                    }
                });
                break;
        }
    }
    
    private int getDrawableId(String weather) {
        @DrawableRes
        int drawableId;
        if (weather == null) {
            drawableId = R.drawable.unknown;
        } else if (weather.equals("晴")) {
            drawableId = R.drawable.qing;
        } else if (weather.equals("阴")) {
            drawableId = R.drawable.yin;
        } else if (weather.equals("多云")) {
            drawableId = R.drawable.duo_yun;
        } else if (weather.equals("少云")) {
            drawableId = R.drawable.shao_yun;
        } else if (weather.contains("雨")) {
            if (weather.equals("阵雨")) {
                drawableId = R.drawable.zhen_yu;
            } else if (weather.equals("雷阵雨")) {
                drawableId = R.drawable.lei_zhen_yu;
            } else if (weather.equals("小雨")) {
                drawableId = R.drawable.xiao_yu;
            } else if (weather.equals("中雨")) {
                drawableId = R.drawable.zhong_yu;
            } else if (weather.equals("大雨")) {
                drawableId = R.drawable.da_yu;
            } else if (weather.equals("暴雨")) {
                drawableId = R.drawable.bao_yu;
            } else if (weather.equals("大暴雨")) {
                drawableId = R.drawable.da_bao_yu;
            } else if (weather.equals("特大暴雨")) {
                drawableId = R.drawable.te_da_bao_yu;
            } else if (weather.equals("冻雨")) {
                drawableId = R.drawable.dong_yu;
            } else if (weather.contains("冰雹")) {
                drawableId = R.drawable.lei_zhen_yu_ban_you_bing_bao;
            } else if (weather.equals("雨夹雪")) {
                drawableId = R.drawable.yu_jia_xue;
            } else {
                drawableId = R.drawable.da_yu;
            }
        } else if (weather.contains("雪")) {
            if (weather.equals("阵雪")) {
                drawableId = R.drawable.zhong_xue;
            } else if (weather.equals("小雪")) {
                drawableId = R.drawable.xiao_xue;
            } else if (weather.equals("中雪")) {
                drawableId = R.drawable.zhong_xue;
            } else if (weather.equals("大雪")) {
                drawableId = R.drawable.da_xue;
            } else if (weather.equals("暴雪")) {
                drawableId = R.drawable.bao_xue;
            } else {
                drawableId = R.drawable.da_xue;
            }
        } else if (weather.equals("浮尘")) {
            drawableId = R.drawable.fu_chen;
        } else if (weather.equals("扬沙")) {
            drawableId = R.drawable.yang_sha;
        } else if (weather.equals("沙尘暴")) {
            drawableId = R.drawable.sha_chen_bao;
        } else if (weather.contains("霾")) {
            drawableId = R.drawable.wu_mai;
        } else if (weather.equals("雾")) {
            drawableId = R.drawable.wu;
        } else if (weather.contains("风")) {
            if (weather.equals("大风")) {
                drawableId = R.drawable.da_feng;
            } else if (weather.equals("飓风")) {
                drawableId = R.drawable.ju_feng;
            } else if (weather.equals("龙卷风")) {
                drawableId = R.drawable.long_juan_feng;
            } else if (weather.equals("台风")) {
                drawableId = R.drawable.tai_feng;
            } else if (weather.equals("热带风暴")) {
                drawableId = R.drawable.re_dai_feng_bao;
            } else {
                drawableId = R.drawable.feng;
            }
        } else {
            drawableId = R.drawable.unknown;
        }
        return drawableId;
    }
}
