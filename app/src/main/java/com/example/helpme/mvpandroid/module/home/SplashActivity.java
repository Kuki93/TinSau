package com.example.helpme.mvpandroid.module.home;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.helpme.mvpandroid.R;
import com.example.helpme.mvpandroid.widget.AutoZoomInImageView;
import com.jaeger.library.StatusBarUtil;

public class SplashActivity extends AppCompatActivity {
    
    private Handler handler = new Handler();
    
    private AutoZoomInImageView imageview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setTransparent(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                
            }
        }, 2000);
        imageview = findViewById(R.id.imageview);
        imageview.post(new Runnable() {//iv即AutoZoomInImageView
            
            @Override
            public void run() {
                //简单方式启动放大动画
//                imageview.init()
//                  .startZoomInByScaleDeltaAndDuration(0.3f, 1000, 1000);//放大增量是0.3，放大时间是1000毫秒，放大开始时间是1000毫秒以后
                //使用较为具体的方式启动放大动画
                imageview.init()
                        .setScaleDelta(0.15f)//放大的系数是原来的（1 + 0.2）倍
                        .setDurationMillis(1500)//动画的执行时间为1500毫秒
                        .setOnZoomListener(new AutoZoomInImageView.OnZoomListener() {
                            @Override
                            public void onStart(View view) {
                                //放大动画开始时的回调
                            }
                            
                            @Override
                            public void onUpdate(View view, float progress) {
                                //放大动画进行过程中的回调 progress取值范围是[0,1]
                            }
                            
                            @Override
                            public void onEnd(View view) {
                                //放大动画结束时的回调
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        })
                        .start(500);//延迟1000毫秒启动
            }
        });
    }
    
    /**
     * 屏蔽物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    
    @Override
    protected void onDestroy() {
        if (handler != null) {
            //If token is null, all callbacks and messages will be removed.
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
