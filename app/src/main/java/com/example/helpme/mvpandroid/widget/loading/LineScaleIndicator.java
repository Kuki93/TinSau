package com.example.helpme.mvpandroid.widget.loading;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class LineScaleIndicator extends Indicator {
    
    private static final float SCALE = 1.0f;
    private static final float INITSCALE = 0.1f;
    
    private float[] scaleYFloats = new float[]{INITSCALE,
            INITSCALE,
            INITSCALE,
            INITSCALE};
    
    public void setScaleYFloats() {
        for (int i = 0; i < scaleYFloats.length; i++) {
            scaleYFloats[i] = INITSCALE;
        }
        invalidateSelf();
    }
    
    @Override
    public void draw(@NonNull Canvas canvas) {
        float translateX = getWidth() / 11;
        float translateY = getHeight() / 2;
        for (int i = 0; i < 4; i++) {
            canvas.save();
            canvas.translate(translateY, (3 + i * 3) * translateX - translateX / 2);
            canvas.scale(scaleYFloats[i], SCALE);
            RectF rectF = new RectF(-getWidth() / 2.5f, -translateX / 2, getWidth() / 2.5f, translateX / 2);
            canvas.drawRoundRect(rectF, 5, 5, getPaint());
            canvas.restore();
        }
    }
    
    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        long[] delays = new long[]{100, 200, 200, 400};
        for (int i = 0; i < 4; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat( 0.1f, 1,  0.1f);
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleYFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
        }
        return animators;
    }
    
}
