package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by Administrator on 2016/10/24.
 */
public class NoVisibleFullScreenScrollView extends ScrollView {
    private OnScrollerListener listener;
    private int downX = 0;
    private int downY = 0;

    private int mTouchSlop;

    public void setListener(OnScrollerListener listener) {
        this.listener = listener;
    }

    public NoVisibleFullScreenScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public NoVisibleFullScreenScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public NoVisibleFullScreenScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    public interface OnScrollerListener{
        void onScrolled(int position);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScrolled(t);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN){
            downX = (int) ev.getX();
            downY = (int) ev.getY();
        } else if (action == MotionEvent.ACTION_MOVE){
            int moveX = (int) ev.getX();
            int moveY = (int) ev.getY();
            int disX = Math.abs(moveX - downX);
            int disY = Math.abs(moveY - downY);
            if ((disX > mTouchSlop || disY > mTouchSlop) && disX < disY) {
                LogUtil.d("zptest","scrollViews true disX: " + disX + " disY: " + disY);
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
