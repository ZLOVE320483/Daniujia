package com.xiaojia.daniujia.ui.views.imageview.BubberImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2016/4/19.
 */
public class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 20f;
    private Path path;

    public GlideRoundTransform(Context context) {
        super(context);
    }

    public GlideRoundTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {

        if (source == null) return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        canvas.save();
        canvas.concat(new Matrix());

        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);

        /**
         * canvas.drawPath(path, imagePaint);
         *  canvas.restore();
         */
//        if (path != null) {
//            path.reset();
//        }
//        path = new Path();
//        path.setFillType(Path.FillType.WINDING);
//        path.addRect(0, 0, result.getWidth() - 10, result.getHeight(), Path.Direction.CW);
//        path.moveTo(result.getWidth(), 1 / 2 * result.getHeight());
//        path.lineTo(result.getWidth() - 10, result.getHeight() - 10);
//        path.lineTo(result.getWidth() - 10, result.getHeight() + 10);
//        path.lineTo(result.getWidth(), 1 / 2 * result.getHeight());
//        canvas.drawPath(path, paint);
//        canvas.restore();
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}
