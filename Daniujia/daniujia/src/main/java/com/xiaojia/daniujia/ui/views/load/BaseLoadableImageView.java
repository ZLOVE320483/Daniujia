package com.xiaojia.daniujia.ui.views.load;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xiaojia.daniujia.ui.views.imageview.BubberImageView.BubbleImageView;

/**
 * Created by ZLOVE on 2016/4/14
 */
public abstract class BaseLoadableImageView extends BubbleImageView implements ILoadImage {

    protected ImageLoaderHelper<ImageView> loaderHelper;

    protected final String LOG_TAG = "LoadableImageView";

    protected abstract int getDefaultImage();

    protected abstract int getBrokenImage();

    public BaseLoadableImageView(Context context) {
        super(context);
    }

    public BaseLoadableImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaseLoadableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDraw(Canvas canvas) {
        try {
            if (loaderHelper != null) {
                loaderHelper.onImageViewDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDraw(canvas);
    }

    @Override
    public void setUrl(String url) {
        if (loaderHelper == null) {
            loaderHelper = new ImageLoaderHelper<>();
            loaderHelper.setDefaultImage(getDefaultImage());
            loaderHelper.setBrokenImage(getBrokenImage());
            loaderHelper.setNeedCircleImage(isNeedCircleImage());
            loaderHelper.setImageView(this);
        }
        loaderHelper.setUrl(url);
    }

    protected boolean isNeedCircleImage() {
        return false;
    }

}
