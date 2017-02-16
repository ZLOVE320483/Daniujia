package com.xiaojia.daniujia.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

public class ExpandableTextView extends TextView {
    private static int MIN_LINE_NUM = 3;
    private static int MAX_LINE_NUM = 500;
    private int lineNum = MIN_LINE_NUM;
    private Bitmap bitmapDown, bitmapUp;
    private boolean mEnabled = false;

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMaxLines(lineNum);
        setTextSize(14);
        bitmapDown = BitmapFactory.decodeResource(getResources(), R.drawable.ic_expert_info_down);
        bitmapUp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_expert_info_up);
        setPadding(26, 40, 26, 100);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineNum == MIN_LINE_NUM) {
                    lineNum = MAX_LINE_NUM;
                } else {
                    lineNum = MIN_LINE_NUM;
                }
                setMaxLines(lineNum);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getLineCount() <= MIN_LINE_NUM) {
            super.onDraw(canvas);
            return;
        }
        if (lineNum == MIN_LINE_NUM) {// 收缩状态
            canvas.drawBitmap(bitmapDown, getWidth() / 2 - bitmapDown.getWidth() / 2, getHeight() - dp2px(15), null);
        } else {// 展开状态
            canvas.drawBitmap(bitmapUp, getWidth() / 2 - bitmapUp.getWidth() / 2, getHeight() - dp2px(15), null);
        }

        super.onDraw(canvas);
    }

    public void setAutoSplitEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && getWidth() > 0 && mEnabled) {
            String result = autoSplitText(this);
            if (!TextUtils.isEmpty(result)) {
                setText(result);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度
        LogUtil.d("zptest", "tv: " + tv.getWidth());
        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }

    private int dp2px(float dpValue) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        final float scale = displayMetrics.density;
        return (int) (dpValue * scale + 0.5f);
    }

}
