package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zp on 2016/7/19.
 */
public class RotateTextView extends TextView{
    private static final int DEFAULT_DEGREES = 0;
    private int mDegrees = 45;

    public RotateTextView(Context context) {
        super(context);
        init(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RotateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(mDegrees, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.translate(0, -getMeasuredHeight() / 4);
        super.onDraw(canvas);
        canvas.restore();
    }

    public void setDegrees(int degrees){
        this.mDegrees = degrees;
        invalidate();
    }
}
