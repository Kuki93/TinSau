package com.example.helpme.mvpandroid.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.helpme.mvpandroid.R;

import java.util.LinkedHashMap;

import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * @Created by helpme on 2018/2/25.
 * @Description
 */
public class IjkVideoPlayer extends JZVideoPlayerStandard {
    
    
    public IjkVideoPlayer(Context context) {
        this(context, null);
    }
    
    public IjkVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    
    @Override
    public void init(Context context) {
        super.init(context);
        clarity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.jz_layout_clarity, null);
                
                OnClickListener mQualityListener = new OnClickListener() {
                    public void onClick(View v) {
                        int index = (int) v.getTag();
                        onStatePreparingChangingUrl(index, getCurrentPositionWhenPlaying());
                        clarity.setText(JZUtils.getKeyFromDataSource(dataSourceObjects, currentUrlMapIndex));
                        for (int j = 0; j < layout.getChildCount(); j++) {//设置点击之后的颜色
                            if (j == currentUrlMapIndex) {
                                ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#fff85959"));
                            } else {
                                ((TextView) layout.getChildAt(j)).setTextColor(Color.parseColor("#ffffff"));
                            }
                        }
                        if (clarityPopWindow != null) {
                            clarityPopWindow.dismiss();
                        }
                    }
                };
                
                for (int j = 0; j < ((LinkedHashMap) dataSourceObjects[0]).size(); j++) {
                    String key = JZUtils.getKeyFromDataSource(dataSourceObjects, j);
                    TextView clarityItem = (TextView) View.inflate(getContext(), R.layout.jz_layout_clarity_item, null);
                    clarityItem.setText(key);
                    clarityItem.setTag(j);
                    layout.addView(clarityItem, j);
                    clarityItem.setOnClickListener(mQualityListener);
                    if (j == currentUrlMapIndex) {
                        clarityItem.setTextColor(Color.parseColor("#fff85959"));
                    }
                }
                
                clarityPopWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
                clarityPopWindow.setContentView(layout);
                int location[] = new int[2];
                clarity.getLocationOnScreen(location);
                clarityPopWindow.showAtLocation(clarity, Gravity.NO_GRAVITY, location[0], location[1]);
                layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int width = Math.round(layout.getMeasuredWidth() * 1.5f);
                int height = layout.getMeasuredHeight();
                int dy = -height - clarity.getHeight() - 5;
                clarityPopWindow.update(clarity, 0, dy, width, height);
                
            }
        });
    }
    
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == cn.jzvd.R.id.fullscreen) {
            if (currentScreen == JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN) {
            } else {
            } 
        }else if (i == cn.jzvd.R.id.back) {
            if (currentScreen == JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN) {
            } else {
            }
        } 
    }
    

    
    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        if (currentScreen == JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN) {
            
        } else {
            
        }
    }
    
    
}
