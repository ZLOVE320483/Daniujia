package com.xiaojia.daniujia.ui.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/24.
 */
public class MoreLineTextView extends TextView {
    private Context context;

    public MoreLineTextView(Context context) {
        super(context);
        init(context);
    }

    public MoreLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoreLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Layout layout = getLayout();
        if (layout != null) {
            int height = (int) FloatMath.ceil(getMaxLineHeight(this.getText().toString()))
                    + getCompoundPaddingTop() + getCompoundPaddingBottom();
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);
        }
    }

    private float getMaxLineHeight(String str) {
        Activity activity;
        if (context instanceof Activity){
            activity = ((Activity) context);
        } else {
            return 0;
        }

        float screenW = activity.getWindowManager().getDefaultDisplay().getWidth();
        // 注意该TextView的父控件是否为LinearLayout
        float paddingLeft = ((LinearLayout) this.getParent()).getPaddingLeft();
        float paddingRight = ((LinearLayout) this.getParent()).getPaddingRight();
        Rect rect = new Rect();
        getPaint().getTextBounds(str,0,str.length(),rect);
        int line = (int) Math.ceil((rect.width() / (screenW - paddingLeft - paddingRight - getPaddingLeft() - getPaddingRight())));
        return line * getLineHeight();

    }

    //    private float getMaxLineHeight(String str) {
//        float height;
//        float screenW = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
//        float paddingLeft = ((LinearLayout) this.getParent()).getPaddingLeft();
//        float paddingReft = ((LinearLayout) this.getParent()).getPaddingRight();
//        //这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
//        int line = (int) Math.ceil((this.getPaint().measureText(str) / (screenW - paddingLeft - paddingReft))) + ScreenUtils.dip2px(8);
//        height = (this.getPaint().getFontMetrics().descent - this.getPaint().getFontMetrics().ascent) * line;
//        return height;
//    }
}
