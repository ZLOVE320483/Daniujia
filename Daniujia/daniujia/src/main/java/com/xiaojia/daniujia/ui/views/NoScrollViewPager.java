package com.xiaojia.daniujia.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ZLOVE on 2016/4/26.
 */
public class NoScrollViewPager extends ViewPager {
    private boolean enableScroll;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isEnableScroll()) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isEnableScroll()) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    /**
     * @return
     */
    public boolean isEnableScroll() {
        return enableScroll;
    }

    /**
     * @param
     */
    public void setEnableScroll(boolean enableScroll) {
        this.enableScroll = enableScroll;
    }

}
