package com.xiaojia.daniujia.ui.act;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.dlgs.BaseBottomDialog;
import com.xiaojia.daniujia.dlgs.DownLoadBottomDlg;
import com.xiaojia.daniujia.domain.SerializableMap;
import com.xiaojia.daniujia.ui.views.SmoothImageView;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/4/21
 */
public class ImagePreActivity extends ActDnjBase implements View.OnClickListener {

    private ViewPager viewPager;
    private SparseArray<Object> map;
    private int currentPosition;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);
        prepData();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        MyPagerAdapter adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(currentPosition);
        findViewById(R.id.id_framework).setOnClickListener(this);
    }

    private void prepData() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Bundle bundle = getIntent().getExtras().getBundle("bundle");
        if (bundle == null) {
            return;
        }
        SerializableMap serializableMap = bundle.getParcelable(IntentKey.INTENT_KEY_IMAGE_MAP);
        if (serializableMap != null) {
            map = serializableMap.getMap();
        }
        String currentUrl = bundle.getString(IntentKey.INTENT_CLICK_VALUE);
        for (int i = 0; i < map.size(); i++) {
            String data = (String) map.get(i);
            if (currentUrl != null && currentUrl.trim().equals(data.trim())) {
                currentPosition = i;
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.BOTTOM;
    }

    private class MyPagerAdapter extends PagerAdapter implements View.OnLongClickListener{

        @Override
        public int getCount() {
            return map.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            final SmoothImageView imageView = new SmoothImageView(ImagePreActivity.this);
            String source = (String) map.get(position);
            File file;
            if (!source.startsWith("https://")) {
                file = new File(source);
            } else {
                String[] split = source.split("/");
                file = new File(FileStorage.APP_IMG_DIR, split[split.length - 1]);
            }
            if (file.length() != 0) {
                Picasso.with(App.get()).load(file)
                        .resize(ScreenUtils.dip2px(screenWidth/2), ScreenUtils.dip2px(screenHeight/2))
                        .centerInside()
                        .into(imageView);
            } else {
                Picasso.with(App.get()).load(source)
                        .resize(ScreenUtils.dip2px(200), ScreenUtils.dip2px(200))
                        .error(R.drawable.ic_avatar_default)
                        .into(imageView);
            }
            container.addView(imageView);
            imageView.setTag(R.id.id_position,position);
            imageView.setOnClickListener(ImagePreActivity.this);
            imageView.setOnLongClickListener(this);

            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v2) {
                    finish();
                    overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean onLongClick(View v) {
            int tag = (int) v.getTag(R.id.id_position);
            String url = (String) map.get(tag);
            DownLoadBottomDlg dlg = new DownLoadBottomDlg(ImagePreActivity.this);
            dlg.setData(url);
            dlg.setOnDialogClickListener(onDialogClickListener);
            dlg.show();
            return true;
        }

    }

    private void DownLoadImage(final String url) {

        String[] split = url.split("/");
        File file = new File(FileStorage.APP_IMG_DIR, split[split.length - 1]);
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), System.currentTimeMillis() + ".jpg"
                    , null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        showShortToast("已保存到相册");
    }

    BaseBottomDialog.OnDialogClickListener onDialogClickListener = new BaseBottomDialog.OnDialogClickListener() {
        @Override
        public void onOK(Bundle b) {
            if (b == null) {
                return;
            }
            String url = b.getString(ExtraConst.DOWNLOAD_URL);
            if (!TextUtils.isEmpty(url)) {
                DownLoadImage(url);
            }
        }
    };
}
