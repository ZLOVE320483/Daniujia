/*
 * Copyright 2014 HuHoo. All rights reserved.
 * HuHoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @IndexerBar.java - 2014年5月21日
 */

package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.xiaojia.daniujia.R;

/**
 * IndexerBar
 * 
 * @author OCEAN
 */
public class AlphaBetIndexerBar extends View {

    public static final String SEARCH_INDEXER = "*";
    
    private static final int TEXT_SIZE = 15;

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private static String[] INDEXER_SEARCH = { SEARCH_INDEXER, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"};
    
    private static String[] INDEXER_NORMAL = {"热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"};
    
    private String[] mIndexer;
    
    private int mChoose = -1;

    private Paint mPaint = new Paint();

    private Paint imgPaint = new Paint();

    private Bitmap searchImgBitMap;
    
    public AlphaBetIndexerBar(Context context) {
        super(context);
        init(null);
    }
    
    public AlphaBetIndexerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    
    public AlphaBetIndexerBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }
    
    private void init(AttributeSet attrs) {
        searchImgBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_index_search);
        dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        if (attrs == null) {
            mIndexer = INDEXER_NORMAL;
        } else {
            // Styleables from XML
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AlphaBetIndexerBar);
            if (a.hasValue(R.styleable.AlphaBetIndexerBar_index_search) && a.getBoolean(R.styleable.AlphaBetIndexerBar_index_search, false)) {
                mIndexer = INDEXER_SEARCH;
            } else {
                mIndexer = INDEXER_NORMAL;
            }
            a.recycle();
        }
    }

    private DisplayMetrics dm = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndexer.length == 0) {
            return;
        }

        float density = dm.density;
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int singleHeight = height / mIndexer.length;
        for (int i = 0; i < mIndexer.length; i++) {
            if (isInEditMode()) {
                mPaint.setColor(Color.BLACK);
            } else {
                mPaint.setColor(getContext().getResources().getColor(R.color.second_text_color));
            }
            mPaint.setTextSize(density * TEXT_SIZE);
            mPaint.setAntiAlias(true);
            if (i == mChoose) {
                mPaint.setFakeBoldText(true);
                mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                mPaint.setFakeBoldText(false);
                mPaint.setTypeface(Typeface.DEFAULT);
            }

            if (SEARCH_INDEXER.equals(mIndexer[i])) {
                imgPaint.setAntiAlias(true);
                float xPos = width / 2 - searchImgBitMap.getWidth() / 2;
                float yPos = singleHeight * i + searchImgBitMap.getHeight() / 2;
                canvas.drawBitmap(searchImgBitMap, xPos, yPos, imgPaint);
            } else {
                float xPos = width / 2 - mPaint.measureText(mIndexer[i]) / 2;
                float yPos = singleHeight * i + singleHeight;
                canvas.drawText(mIndexer[i], xPos, yPos, mPaint);
            }
            mPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mIndexer.length == 0) {
            return false;
        }

        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int characterIndex = (int) (y / getHeight() * mIndexer.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (oldChoose != characterIndex) {
                    if (characterIndex >= 0 && characterIndex < mIndexer.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(mIndexer[characterIndex]);
                        }
                        mChoose = characterIndex;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != characterIndex) {
                    if (characterIndex >= 0 && characterIndex < mIndexer.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(mIndexer[characterIndex]);
                        }
                        mChoose = characterIndex;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mChoose = -1;
                invalidate();
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }
    
}
