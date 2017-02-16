package com.xiaojia.daniujia.ui.views.load;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.xiaojia.daniujia.utils.BitmapUtil;
import com.xiaojia.daniujia.utils.FileUtil;

/**
 * Created by ZLOVE on 2016/4/14.
 */
public class ImageLoaderHelper<T extends ImageView> implements ImageCachable {

    protected final String LOG_TAG = "LoadableImageView";

    private T imageView;

    private String url = null;

    private int pictureType;

    private int defaultImage = -1;

    private int brokenImage = -1;

    private boolean needCircleImage;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUrl(String url) {
        this.url = url;
        load();
    }

    private void load() {
        if (TextUtils.isEmpty(url)) {
            setImageResource(getDefaultImage());
        } else {
            PhotoLoader.getInstance().loadPhoto(this, url, 10);
        }
    }

    public void onImageViewDraw(Canvas canvas) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxRequiredWidth() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxRequiredHeight() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onStartLoad(String url) {
        if ((this.url == null && url == null) || this.url != null && this.url.equals(url)) {
            setImageResource(getDefaultImage());
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setBitmap(Bitmap paramBitmap, boolean paramBoolean, String url) {
        if ((this.url == null && url == null) || (this.url != null && this.url.equals(url))) {
            if (paramBitmap != null && paramBitmap.isRecycled()) {
                setUrl(url);
                Log.i(LOG_TAG, "loaded a recyced LoadableImageView");
                return false;
            }
            imageView.setImageBitmap(paramBitmap);
            return true;
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onFail(String url) {
        if ((this.url == null && url == null) || this.url != null && this.url.equals(url)) {
            setImageResource(getBrokenImage());
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void setImageResource(int resId) {
        if (resId != -1) {
            imageView.setImageResource(resId);
        } else {
            imageView.setImageDrawable(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onProgress(String url, int percent) {
        if ((this.url == null && url == null) || this.url != null && this.url.equals(url)) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDownLoadUrl(String url) {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCacheUrl(String url) {
        return FileUtil.urlToLocalPath(url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPictureType(int type) {
        this.pictureType = type;
    }

    /**
     * @return
     */
    public int getPictureType() {
        return pictureType;
    }


    /**
     * @return
     */
    public int getDefaultImage() {
        return defaultImage;
    }


    /**
     * @param
     */
    public void setDefaultImage(int defaultImage) {
        this.defaultImage = defaultImage;
    }


    /**
     * @return
     */
    public int getBrokenImage() {
        return brokenImage;
    }


    /**
     * @param
     */
    public void setBrokenImage(int brokenImage) {
        this.brokenImage = brokenImage;
    }


    /**
     * @param
     */
    public void setImageView(T imageView) {
        this.imageView = imageView;
    }


    /**
     * @return
     */
    public T getImageView() {
        return imageView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bitmap reSizeBitmap(String path, int maxWidth, int maxHeight) {
        Bitmap bitMap = BitmapUtil.reSizeBitmap(path, maxWidth, maxHeight);
        if (needCircleImage) {
            return BitmapUtil.getCircleBitmap(bitMap);
        } else {
            return bitMap;
        }
    }


    /**
     * @return
     */
    public boolean isNeedCircleImage() {
        return needCircleImage;
    }


    /**
     * @param
     */
    public void setNeedCircleImage(boolean needCircleImage) {
        this.needCircleImage = needCircleImage;
    }

}
