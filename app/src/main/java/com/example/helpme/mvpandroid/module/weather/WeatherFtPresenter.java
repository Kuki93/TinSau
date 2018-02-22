package com.example.helpme.mvpandroid.module.weather;

import android.support.annotation.DrawableRes;
import android.support.v4.widget.NestedScrollView;
import android.widget.TextView;

import com.example.helpme.mvp.presenter.BaseMvpPresenter;
import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.contract.WeatherContract;
import com.example.helpme.mvpandroid.utils.ThemeUtils;


/**
 * @Created by helpme on 2018/1/24.
 * @Description
 */
public class WeatherFtPresenter extends BaseMvpPresenter<WeatherActivity, WeatherContract
        .WeatherFtView> {
    
    public static final String TAG = WeatherFtPresenter.class.getSimpleName();
    private int  mToolbarHeight, mTempHeight, top;
    
    protected void onDoSomething(int height, int top) {
        mToolbarHeight = ThemeUtils.getCurrentActionBarHeight(getMvpView().getParentHost());
        mTempHeight = height;
        this.top = top;
    }
    
    public NestedScrollView.OnScrollChangeListener mOnScrollChangeListener = new NestedScrollView
            .OnScrollChangeListener() {
        
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                   int oldScrollY) {
            float rate = scrollY * 1.0f / mToolbarHeight;
            
            if (rate <= 0) {
                rate = 0;
            }
            if (rate >= 1) {
                rate = 1;
            }
            int titleColor = ThemeUtils.changeAlphaTo255(0xFFFFFFFF, rate);
            int cityColor = ThemeUtils.changeAlphaTo0(0xFFFFFFFF, rate);
            
            if (scrollY < mTempHeight + 30)
                getMvpView().getParentHost().setToolbarColor(titleColor, cityColor, false);
            else {
                getMvpView().getParentHost().setToolbarColor(titleColor, cityColor, true);
            }
    
            if (scrollY > top) {
                getMvpView().refreshSun();
            }
        }
    };
    
    
    public void setIcon(TextView tv, String weather) {
        @DrawableRes
        int drawableId;
        if (weather.equals("晴")) {
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
        tv.setCompoundDrawablesWithIntrinsicBounds(mActivity.getResources().getDrawable(drawableId), null
                , null, null);
    }
    
}
