package com.xiaojia.daniujia.ui.views.load;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by ZLOVE on 2016/4/13
 */
public class LoadableImageView extends BaseLoadableImageView {

    public LoadableImageView(Context context) {
        super(context);
    }

    public LoadableImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LoadableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private static final String IMAGE_PRE = "img/";

    /**
     * {@inheritDoc}
     */
    public void setUrl(String url, boolean needThumbNail) {
        if (TextUtils.isEmpty(url) || url.contains(PhotoLoader.SD_CARD_PATH)) {
            super.setUrl(url);
        } else {
            super.setUrl(IMAGE_PRE + getThumbNailUrl(url));
        }
    }

    private static String getThumbNailUrl(String url) {
        int i = url.lastIndexOf("/");
        if (i != -1) {
            return new StringBuilder(url.subSequence(0, i + 1)).append("thumbnail/").append(url.subSequence(i + 1, url.length())).toString();
        } else {
            return url;
        }
    }

    @Override
    protected int getDefaultImage(){
        return -1;
    }

    @Override
    protected int getBrokenImage(){
        return -1;
    }

}
