package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.xiaojia.daniujia.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class CustomGifView extends View {
    private Movie gif;
    private long gifStart;
    public CustomGifView(Context context) {
        super(context);
        gif = Movie.decodeStream(getResources().openRawResource(R.raw.progrss2));
    }

    public CustomGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gif = Movie.decodeStream(getResources().openRawResource(R.raw.progrss2));
    }

    public CustomGifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gif = Movie.decodeStream(getResources().openRawResource(R.raw.progrss2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = android.os.SystemClock.uptimeMillis();

        if (gifStart == 0) {
            gifStart = now;
        }
        if (gif != null) {
            int dur = gif.duration();
            if (dur == 0) {
                dur = 1000;
            }
            int relTime = (int) ((now - gifStart) % dur);
            gif.setTime(relTime);
            gif.draw(canvas,160, 300);
            invalidate();
        }
    }
}
