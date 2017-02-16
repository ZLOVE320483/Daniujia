package com.xiaojia.daniujia.ui.views.imageview.BubberImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.utils.BitmapUtil;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.io.File;

public class BubbleImageView extends ShaderImageView {
    private BubbleShader shader;
    private Paint mPaint;//画笔

    public BubbleImageView(Context context) {
        super(context);
        init();
    }

    public BubbleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    AnimationDrawable animationDrawable;

    private void init() {
        mPaint=new Paint();
        setImageResource(R.drawable.frame_loading);
        animationDrawable = (AnimationDrawable) getDrawable();
        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new BubbleShader();
        return shader;
    }

    public void setUrl(MessageEntity.ContentEntity contentEntity) throws Exception {
        if (contentEntity == null) {
            return;
        }
        String url = contentEntity.getMsg();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        final int width = contentEntity.getWidth();
        final int height = contentEntity.getHeight();
        if (!url.startsWith("https://")) {
            /**
             *  get bitmap from path
             */
            File file = new File(url);
            showImage(file, width, height);
        } else {
            /**
             *get bitmap from url
             */
            String content[] = url.split("/");
            String path = content[content.length - 1];
            final File file = new File(FileStorage.APP_IMG_DIR, path);
            if (file.length() == 0) {
                Picasso.with(App.get()).load(R.drawable.frame_loading).into(this);
                QiniuDownloadUtil.downloadPic(path, url, new QiniuDownloadUtil.DownloadCallback() {
                    @Override
                    public void downloadComplete(String path) {
                        showImage(file, width, height);
                    }

                    @Override
                    public void onProgress(int max, int progress) {
                    }

                    @Override
                    public void onFail(Exception e) {

                    }
                });
            } else {
                showImage(file, width, height);
            }
        }
    }

    private void showImage(final File file,int width,int height) {
        final int [] arr = BitmapUtil.calculatePicSize(width, height);
        if (arr[0] < 1 || arr[1] < 1) {
            return;
        }
        Activity activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestCreator resize = Picasso.with(App.get()).load(file)
                        .config(Bitmap.Config.RGB_565)
                        .resize(ScreenUtils.dip2px(arr[0]), ScreenUtils.dip2px(arr[1]))
                        .centerCrop();
                if (resize != null) {
                    resize.error(R.drawable.frame_loading).into(BubbleImageView.this);
                }

            }
        });
    }


    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shader.drawProgress(canvas, mPaint, progress);
    }

    int progress=0;
    public void setProgress(int progress){
        this.progress=progress;
        postInvalidate();
    }

}
