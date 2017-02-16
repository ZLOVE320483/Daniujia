package com.xiaojia.daniujia.ui.views.load;

import android.graphics.Bitmap;

/**
 * Created by ZLOVE on 2016/4/14.
 */
public interface ImageCachable {

    public String getUrl();

    public void setUrl(String url);

    public int getMaxRequiredWidth();

    public int getMaxRequiredHeight();

    public boolean onStartLoad(String url);

    public boolean onProgress(String url, int percent);

    public boolean setBitmap(Bitmap paramBitmap, boolean paramBoolean, String url);

    public boolean onFail(String url);

    public String getDownLoadUrl(String url);

    public String getCacheUrl(String url);

    public void setPictureType(int type);

    public Bitmap reSizeBitmap(String path, int maxWidth, int maxHeight);

}
