package com.example.helpme.mvpandroid.widget.loading;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

public class BallPulseIndicator extends Indicator {
    
    private static final float SCALE = 1.0f;
    
    //scale x ,y
    private float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE};

//    public void draw(Canvas canvas) {
//        Log.d("sfsafasfas", "drawsss" + 111);
//        Log.d("sfsafasfas", "draw" + 111);
//        float circleSpacing = 4;
//        float radius = (Math.min(getWidth(), getHeight()) - circleSpacing * 2) / 6;
//        float x = getWidth() / 2 - (radius * 2 + circleSpacing);
//        float y = getHeight() / 2;
//        for (int i = 0; i < 3; i++) {
//            canvas.save();
//            float translateX = x + (radius * 2) * i + circleSpacing * i;
//            canvas.translate(translateX, y);
//            canvas.scale(scaleFloats[i], scaleFloats[i]);
//            canvas.drawCircle(0, 0, radius, paint);
//            canvas.restore();
//        }
//    }
    
    @Override
    public void draw(@NonNull Canvas canvas) {
        float circleSpacing = 4;
        float radius = (Math.min(getWidth(), getHeight()) - circleSpacing * 2) / 6;
        float x = getWidth() / 2 - (radius * 2 + circleSpacing);
        float y = getHeight() / 2;
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + circleSpacing * i;
            canvas.translate(translateX, y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, getPaint());
            canvas.restore();
        }
    }
    
    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        int[] delays = new int[]{120, 240, 360};
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.3f, 1);
            
            scaleAnim.setDuration(750);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
        }
        return animators;
    }
    
    
}
