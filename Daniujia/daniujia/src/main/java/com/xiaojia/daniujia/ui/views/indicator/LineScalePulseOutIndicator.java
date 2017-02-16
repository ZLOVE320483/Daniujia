package com.xiaojia.daniujia.ui.views.indicator;


import android.animation.Animator;
import android.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2015/10/19.
 */
public class LineScalePulseOutIndicator extends LineScaleIndicator {

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            final int index = i;
            ValueAnimator scaleAnim;
            if (i == 0) {
                scaleAnim = ValueAnimator.ofFloat(1f, 1.8f, 1f);
            } else if (i == 1) {
                scaleAnim = ValueAnimator.ofFloat(1f, 2f, 1f);
            } else if (i == 2) {
                scaleAnim = ValueAnimator.ofFloat(1f, 1.3f, 1f);
            } else if (i == 3) {
                scaleAnim = ValueAnimator.ofFloat(1f, 6f, 1f);
            } else if (i == 4) {
                scaleAnim = ValueAnimator.ofFloat(1f, 8f, 1f);
            } else if (i == 5) {
                scaleAnim = ValueAnimator.ofFloat(1f, 5f, 1f);
            } else if (i == 6) {
                scaleAnim = ValueAnimator.ofFloat(1f, 2f, 1f);
            } else if (i == 7) {
                scaleAnim = ValueAnimator.ofFloat(1f, 4f, 1f);
            } else {
                scaleAnim = ValueAnimator.ofFloat(1f, 1.8f, 1f);
            }
            scaleAnim.setDuration(400);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(0);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleYFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();
            animators.add(scaleAnim);
        }
        return animators;
    }

}
