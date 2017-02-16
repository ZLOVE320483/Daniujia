package com.xiaojia.daniujia.ui.views.imageview.BubberImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.xiaojia.daniujia.R;

public class BubbleShader extends ShaderHelper {

    public enum ArrowPosition {
        @SuppressLint("RtlHardcoded")
        LEFT,
        RIGHT
    }

    protected final RectF imageRect = new RectF();
    protected RectF shaderRect = new RectF();
    protected RectF transparentRect = new RectF();

    private int radius = 0;
    protected int bitmapRadius;
    private final Path path = new Path();

    private ArrowPosition arrowPosition = ArrowPosition.LEFT;

    private float left;
    private float top;
    private float right;
    private float bottom;
    private float triangleHeightPx;

    private int bitmapWidth;
    private int bitmapHeight;

    @Override
    public void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        // borderWidth = 0;
        //borderPaint.setStrokeWidth(borderWidth * 2);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShaderImageView, defStyle, 0);
            int arrowPositionInt = typedArray.getInt(R.styleable.ShaderImageView_siArrowPosition, ArrowPosition.LEFT.ordinal());
            arrowPosition = ArrowPosition.values()[arrowPositionInt];
            radius = typedArray.getDimensionPixelSize(R.styleable.ShaderImageView_siRadius, radius);
            typedArray.recycle();
        }
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint) {
        canvas.save();
        canvas.concat(matrix);

        canvas.drawRoundRect(imageRect, bitmapRadius, bitmapRadius, imagePaint);
        /**
         *  draw path  just like svg animation to draw a bitmap
         */
        canvas.drawPath(path, imagePaint);
        canvas.restore();
    }


    public void drawProgress(Canvas canvas, Paint mPaint, int progress) {
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(Color.parseColor("#70000000"));//半透明
        shaderRect.set(left, top, right, bitmapHeight - bitmapHeight * progress / 100);
        if (progress <= 0) {
            canvas.drawRoundRect(shaderRect, bitmapRadius, bitmapRadius, mPaint);
        } else {
            canvas.drawRoundRect(shaderRect, 0, 0, mPaint);
        }

        mPaint.setColor(Color.parseColor("#00000000"));//全透明
        transparentRect.set(left, bitmapHeight - bitmapHeight * progress / 100, right, bottom);
        canvas.drawRoundRect(transparentRect, bitmapRadius, bitmapRadius, mPaint);

        if (progress != 100) {
            mPaint.setTextSize(30);
            mPaint.setColor(Color.parseColor("#FFFFFF"));
            mPaint.setStrokeWidth(2);
            Rect rect = new Rect();
            mPaint.getTextBounds("100%", 0, "100%".length(), rect);//确定文字的宽度
            canvas.drawText(progress + "%", bitmapWidth / 2 - rect.width() / 2, bitmapWidth / 2, mPaint);
        }

    }

    @Override
    public void calculate(int bitmapWidth, int bitmapHeight,
                          float width, float height,
                          float scale,
                          float translateX, float translateY) {
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        if (bitmapWidth > bitmapHeight) {
            triangleHeightPx = 1f / 30 * bitmapWidth;
        } else {
            triangleHeightPx = 1f / 30 * bitmapHeight;
        }

        if (arrowPosition.equals(ArrowPosition.LEFT)) {
            left = -translateX + triangleHeightPx - 1;
            top = -translateY;
            right = bitmapWidth + translateX;
            bottom = bitmapHeight + translateY;
            imageRect.set(left, top, right, bottom);
        } else {
            left = -translateX;
            top = -translateY;
            right = bitmapWidth + translateX - triangleHeightPx + 1;
            bottom = bitmapHeight + translateY;
            imageRect.set(left, top, right, bottom);
        }
        bitmapRadius = Math.round(radius / scale);

        path.reset();
        float x = -translateX;
        float y = -translateY - triangleHeightPx;
        float scaledTriangleHeight = triangleHeightPx / scale;
        float resultWidth = bitmapWidth + 2 * translateX;
        float resultHeight = bitmapHeight + 2 * translateY;

        float centerY;
        if (translateY != 0) {
            centerY = 1f / 10 * bitmapHeight - translateY;
        } else {
            centerY = 1f / 5 * bitmapHeight;
        }

        path.setFillType(Path.FillType.WINDING);
        float rectLeft;
        float rectRight;
        switch (arrowPosition) {
            case LEFT:
                rectLeft = scaledTriangleHeight + x;
                rectRight = resultWidth + rectLeft - 3 * triangleHeightPx;
                path.addRect(rectLeft + 3 * triangleHeightPx, y, rectRight - triangleHeightPx, resultHeight + y - 2 * triangleHeightPx, Path.Direction.CW);
                path.moveTo(x, centerY);
                path.lineTo(-translateX + triangleHeightPx, centerY - triangleHeightPx * 2 / 3);
                path.lineTo(-translateX + triangleHeightPx, centerY + triangleHeightPx * 2 / 3);
                path.lineTo(x, centerY);
                break;
            case RIGHT:
                rectLeft = x;
                float imgRight = resultWidth + rectLeft;
                rectRight = imgRight - scaledTriangleHeight;
                path.addRect(rectLeft + 2 * triangleHeightPx, y, rectRight - 2 * triangleHeightPx, resultHeight + y, Path.Direction.CW);
                path.moveTo(imgRight, centerY);
                path.lineTo(bitmapWidth + translateX - triangleHeightPx, centerY - triangleHeightPx * 2 / 3);
                path.lineTo(bitmapWidth + translateX - triangleHeightPx, centerY + triangleHeightPx * 2 / 3);
                path.lineTo(imgRight, centerY);
                break;
        }
    }

    @Override
    public void reset() {
        imageRect.set(0, 0, 0, 0);
        bitmapRadius = 0;
        path.reset();
    }

    public final int getRadius() {
        return radius;
    }

    public final void setRadius(final int radius) {
        this.radius = radius;
    }
}