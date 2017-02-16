package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/9/8.
 */
public class VerticalDashedLineView extends View {
    public VerticalDashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#dfdfdf"));//颜色可以自己设置
        paint.setStrokeWidth(10);
        Path path = new Path();
        path.moveTo(0, 0);//起始坐标
        path.lineTo(1000,0);//终点坐标
        PathEffect effects = new DashPathEffect(new float[]{10,8,10,8},1);//设置虚线的间隔和点的长度
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
