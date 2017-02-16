package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/5/3
 */
public class StillScrollEditText extends EditText implements OnTouchListener {

    public StillScrollEditText(Context context) {
        super(context);
        init();
    }

    public StillScrollEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StillScrollEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.hasFocus() && getLineCount() > 5) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }
}
