package com.xiaojia.daniujia.ui.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

/**
 * Created by ZLOVE on 2016/7/19.
 */
public class OrderRelativeWindow extends PopupWindow implements View.OnClickListener {

    private Activity mContext;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;

    private RelativeLayout contentView;

    private Handler mHandler = new Handler();

    private boolean needHideChatRecord = false;
    private OrderRelativeListener listener;

    public void setListener(OrderRelativeListener listener) {
        this.listener = listener;
    }

    public OrderRelativeWindow(Activity context, boolean needHideChatRecord) {
        this.mContext = context;
        this.needHideChatRecord = needHideChatRecord;
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
        setWidth(mWidth);
        setHeight(mHeight);
    }

    private Bitmap blur() {
        if (null != overlay) {
            return overlay;
        }
        View view = mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        mBitmap = view.getDrawingCache();

        float scaleFactor = 8;//图片缩放比例；
        float radius = 5f;//模糊程度
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        return overlay;
    }

    public void showWindow(View anchor, boolean isExpert) {
        contentView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_view_order_relative, null);
        contentView.findViewById(R.id.id_relative).setOnClickListener(this);
        if (isExpert) {
            ((TextView) contentView.findViewById(R.id.id_my_ask)).setText("用户提问");
            ((TextView) contentView.findViewById(R.id.id_person_intro)).setText("用户简介");
        } else {
            ((TextView) contentView.findViewById(R.id.id_my_ask)).setText("我的提问");
            ((TextView) contentView.findViewById(R.id.id_person_intro)).setText("我的简介");
        }
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnimation();
            }
        });
        setContentView(contentView);
        for (int i = 0; i < contentView.getChildCount() - 1; i++) {
            final View child = contentView.getChildAt(i);
            child.setOnClickListener(this);
            child.setVisibility(View.INVISIBLE);
            final ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 300 - i * 100, 0);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (needHideChatRecord && child.getId() == R.id.id_chat_record) {
                        child.setVisibility(View.INVISIBLE);
                    } else {
                        child.setVisibility(View.VISIBLE);
                    }
                    fadeAnim.setDuration(300);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(150);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }
        Drawable colorDrawable = new ColorDrawable(Color.WHITE);
        colorDrawable.setAlpha(180);
        setBackgroundDrawable(colorDrawable);
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
    }

    public void closeAnimation() {
        for (int i = 0; i < contentView.getChildCount() - 1; i++) {
            final View child = contentView.getChildAt(i);
            final ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 400 - i * 100);
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (needHideChatRecord && child.getId() == R.id.id_chat_record) {
                        child.setVisibility(View.INVISIBLE);
                    } else {
                        child.setVisibility(View.VISIBLE);
                    }
                    fadeAnim.setDuration(200);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(100);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                    fadeAnim.addListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }
                    });
                }
            }, (contentView.getChildCount() - i - 1) * 30);

            if (child.getId() == R.id.id_chat_record) {
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dismiss();
                    }
                }, (contentView.getChildCount() - i) * 30 + 80);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_chat_record) {
            closeAnimation();
            if (listener != null) {
                listener.chatRecordClick();
            }
        } else if (v.getId() == R.id.id_my_ask) {
            closeAnimation();
            if (listener != null) {
                listener.myAskClick();
            }
        } else if (v.getId() == R.id.id_person_intro) {
            closeAnimation();
            if (listener != null) {
                listener.personProfileClick();
            }
        } else if (v.getId() == R.id.id_relative) {
            closeAnimation();
        }
    }

    public interface OrderRelativeListener {
        void chatRecordClick();

        void myAskClick();

        void personProfileClick();
    }
}
