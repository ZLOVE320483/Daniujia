package com.xiaojia.daniujia.ui.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Interpolator;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

import com.xiaojia.daniujia.utils.ScreenUtils;

public class FlexibleScrollView extends HorizontalScrollView{

    private static final int MAX_BEAD_LENGTH = 50;
    private static final int REPLACE_ILLEGAL_LENGTH = 10;
    private float k = 1.0f;
    private float MAX_OFFSET;

    public FlexibleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlexibleScrollView(Context context) {
        super(context);
        init();
    }

    private void init() {
        MAX_OFFSET = ScreenUtils.getDisplayMetrics((Activity)getContext()).widthPixels * 2 /5;
    }

    private View leftView;
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            leftView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private Rect markRect = new Rect();
    private float x;
    private void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preX = x;
                float nowX = ev.getX();
                k = (MAX_OFFSET - Math.abs(leftView.getLeft()))/ MAX_OFFSET;
                int distanceX = (int) ((int) (preX - nowX) * k * k) * 3 / 4;
                if (Math.abs(distanceX)> MAX_BEAD_LENGTH) {
                    distanceX = REPLACE_ILLEGAL_LENGTH;
                }
                scrollBy(distanceX, 0);
                x = nowX;
                if (isNeedMove()) {
                    if (markRect.isEmpty()) {
                        markRect.set(leftView.getLeft(), leftView.getTop(), leftView.getRight(), leftView.getBottom());
                    }
                    leftView.layout(leftView.getLeft() - distanceX, leftView.getTop(), leftView.getRight() - distanceX, leftView.getBottom());
                }
                break;
            default:
                break;
        }
    }

    private void animation() {
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(leftView.getLeft(), 0, markRect.left, 0);
        mTranslateAnimation.setDuration(450);
        mTranslateAnimation.setInterpolator(new DecelerateInterpolator());
        leftView.setAnimation(mTranslateAnimation);
        leftView.layout(markRect.left, markRect.top, markRect.right, markRect.bottom);
        markRect.setEmpty();
        x = 0;
    }

    private boolean isNeedAnimation() {
        return !markRect.isEmpty();
    }

    private boolean isNeedMove() {
        int offset = leftView.getMeasuredWidth() - getWidth();
        int scrollX = getScrollX();
        if (scrollX == 0 || offset == scrollX)
            return true;
        return false;
    }
}
