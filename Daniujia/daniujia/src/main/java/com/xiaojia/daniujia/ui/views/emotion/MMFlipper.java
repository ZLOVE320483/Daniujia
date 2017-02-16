package com.xiaojia.daniujia.ui.views.emotion;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.EmotionUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class MMFlipper extends LinearLayout {

    private static final int EMO_COUNT_NORMAL_EACH_COLUMN = 7;
    private static final int EMO_COUNT_GIF_EACH_COLUMN = 4;
    private static final int EMO_COUNT_NORMAL_EACH_PAGE_MAX = 20;
    private static final int EMO_COUNT_GIF_EACH_PAGE_MAX = 8;

    private ViewPager mViewPager;
    private PagerAdapterEmo mAdapterEmo;
    private FixedSpeedScroller mScroller;
    private LinearLayout indexerGroup;
    private int currentPage = 0;
    private boolean init = false;
    private OnFaceSelectedListener mEmoSelectedListener;
    private List<Emotion> emotionList = EmotionUtil.emotionSearchList;
    private boolean gifEmo = false;

    public MMFlipper(Context context) {
        super(context);
    }

    public MMFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MMFlipper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initControl() {
        if (!init) {
            initView();
        }
    }

    private void initView() {
        init = true;
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.bg_emo));
        int pageCount = (int) Math.ceil(getEmoTotalCount() * 1.0 / getEmoCountEachPageMax());
        int margin = getResources().getDimensionPixelSize(R.dimen.gridview_space);
        mViewPager = new ViewPager(getContext());
        mAdapterEmo = new PagerAdapterEmo(pageCount);
        mViewPager.setAdapter(mAdapterEmo);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.height = getResources().getDimensionPixelSize(R.dimen.mmflipper_height);
        params.topMargin = margin;
        addView(mViewPager, params);
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            // 设置加速度 ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
            mScroller = new FixedSpeedScroller(mViewPager.getContext(), new OvershootInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Indexer
        indexerGroup = new LinearLayout(getContext());
        indexerGroup.setGravity(Gravity.CENTER_HORIZONTAL);
        for (int i = 0; i < pageCount; i++) {
            ImageView indexerView = new ImageView(getContext());
            indexerView.setPadding(margin, 0, 0, margin);
            indexerView.setImageResource(i == 0 ? R.drawable.img_banner_dot_p : R.drawable.img_banner_dot_n);
            indexerGroup.addView(indexerView);
        }
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) (margin / 2);
        params.bottomMargin = (int) (margin / 2);
        // indexerGroup.setPadding(0, 0, 0, margin);
        addView(indexerGroup, params);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    mViewPager.setCurrentItem(arg0 + 1, true);
                    return;
                }

                if (arg0 == mViewPager.getAdapter().getCount() - 1) {
                    mViewPager.setCurrentItem(arg0 - 1, true);
                    return;
                }

                try {
                    ((ImageView) indexerGroup.getChildAt(currentPage)).setImageResource(R.drawable.img_banner_dot_n);
                    // One Dummy View at begin.
                    currentPage = arg0 - 1;
                    ((ImageView) indexerGroup.getChildAt(currentPage)).setImageResource(R.drawable.img_banner_dot_p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        mViewPager.setCurrentItem(1);
    }

    protected int getEmoTotalCount() {
        return emotionList != null ? emotionList.size() : 0;
    }

    private int getEmoCountEachPageMax() {
        return !gifEmo ? EMO_COUNT_NORMAL_EACH_PAGE_MAX : EMO_COUNT_GIF_EACH_PAGE_MAX;
    }

    private int getEmoCountEachColumn() {
        return !gifEmo ? EMO_COUNT_NORMAL_EACH_COLUMN : EMO_COUNT_GIF_EACH_COLUMN;
    }

    public void reArraygePage(List<Emotion> emotionList, boolean gifEmo) {
        if (this.emotionList == emotionList) {
            return;
        }

        this.emotionList = emotionList;
        this.gifEmo = gifEmo;
        this.currentPage = 0;
        if (!init) {
            initView();
        } else {
            int pageCount = (int) Math.ceil(getEmoTotalCount() * 1.0 / getEmoCountEachPageMax());
            int margin = getResources().getDimensionPixelSize(R.dimen.gridview_space);

            mAdapterEmo = new PagerAdapterEmo(pageCount);
            mViewPager.setAdapter(mAdapterEmo);

            indexerGroup.removeAllViews();
            for (int i = 0; i < pageCount; i++) {
                ImageView indexerView = new ImageView(getContext());
                indexerView.setPadding(margin / 2, 0, 0, margin / 2);
                indexerView.setImageResource(i == 0 ? R.drawable.img_banner_dot_p : R.drawable.img_banner_dot_n);
                indexerGroup.addView(indexerView);
            }
            mViewPager.setCurrentItem(1);
        }
    }

    private class PagerAdapterEmo extends PagerAdapter {

        private SparseArray<View> mListViews;
        private int pageCount;
        private int emoTotalCount;
        private boolean dataSetChanging = false;

        private PagerAdapterEmo(int pageCount) {
            this.mListViews = new SparseArray<>();
            this.emoTotalCount = getEmoTotalCount();
            this.pageCount = pageCount;
        }

        @Override
        public int getCount() {
            return pageCount + 2;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mListViews.get(position) == null) {
                fillView(position);
            }

            container.addView(mListViews.get(position), 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return mListViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));

            if (dataSetChanging) {
                mListViews.remove(position);
            }
        }

        @Override
        public void notifyDataSetChanged() {
            dataSetChanging = true;
            super.notifyDataSetChanged();
            dataSetChanging = false;
        }

        @Override
        public int getItemPosition(Object object) {
            if (dataSetChanging)
                return POSITION_NONE;
            else
                return super.getItemPosition(object);
        }

        private View fillView(int position) {
            if (position == 0 || position == getCount() - 1) {
                return getDummyView(position);
            } else {
                return getEmoGridView(position - 1, position);
            }
        }

        private View getDummyView(int position) {
            TextView dummyView = new TextView(getContext());
            dummyView.setWidth(20);
            mListViews.put(position, dummyView);
            return dummyView;
        }

        private View getEmoGridView(int position, int realPosInAdapter) {
            int emoOffSet = position != 0 ? position * getEmoCountEachPageMax( ) : 0;
            int emoPageCount = getEmoCountEachPageMax( );

            if (position == pageCount - 1) {
                int remind = emoTotalCount % getEmoCountEachPageMax( );
                if (remind != 0)
                    emoPageCount = remind;
            }

            GridView girdView = new GridView(getContext());
            girdView.setNumColumns(getEmoCountEachColumn());
            girdView.setPadding(10, 10, 10, 10);
            girdView.setCacheColorHint(0);
            girdView.setAdapter(new AdapterEmo(emoOffSet, emoPageCount));
            girdView.setSelector(getResources().getDrawable(android.R.color.transparent));
            girdView.setFocusable(true);
            mListViews.put(realPosInAdapter, girdView);
            return girdView;
        }
    }

    private class AdapterEmo extends BaseAdapter {

        private int emoOffSet;
        private int emoCount;
        private int height;

        public AdapterEmo(int emoOffSet, int emoCount) {
            this.emoOffSet = emoOffSet;
            this.emoCount = emoCount;
            this.height = getResources().getDimensionPixelSize(gifEmo ? R.dimen.emo_gif_item_height : R.dimen.emo_item_height);
        }

        @Override
        public int getCount() {
            return gifEmo ? emoCount : getEmoCountEachPageMax() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (emotionList != null && position < emoCount ) {
                Emotion emotion = emotionList.get((int) getItemId(position));
                return getContext().getResources().getDrawable(emotion.getRes());
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return emoOffSet + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FrameLayout frameLayout;
            ImageView imageView;
            if (convertView == null) {
                frameLayout = new FrameLayout(getContext());
                frameLayout.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
                frameLayout.setPadding(1, 20, 1, 20);
                //frameLayout.setBackgroundColor(getResources().getColor(R.color.bg_emo_grid));

                imageView = new ImageView(getContext());
                int padding = getResources().getDimensionPixelSize(R.dimen.gridview_item_space);
                imageView.setPadding(padding, padding, padding, padding);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setBackgroundResource(R.drawable.bg_smiley_item);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long position = (Long) v.getTag(R.id.id_position);
                        onItemClick((int) position);
                    }
                });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
                params.gravity = Gravity.CENTER;
                frameLayout.addView(imageView, params);

                convertView = frameLayout;
                convertView.setTag(imageView);
            } else {
                frameLayout = (FrameLayout) convertView;
                imageView = (ImageView) convertView.getTag();
            }
            if (position <= emoCount - 1) {
                imageView.setClickable(true);
                imageView.setTag(R.id.id_position, getItemId(position));
                imageView.setImageDrawable((Drawable) getItem(position));
                imageView.setBackgroundResource(R.drawable.bg_smiley_item);
            } else if (position == getEmoCountEachPageMax()) {
                imageView.setClickable(true);
                imageView.setTag(R.id.id_position, -1L);
                imageView.setImageResource(R.drawable.btn_del);
                imageView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            } else {
                imageView.setTag(R.id.id_position, -1L);
                imageView.setClickable(false);
                imageView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
            }
            return frameLayout;
        }
    }

    private boolean onItemClick(int position) {
        try {
            if (position == -1) {
                onDeleteClicked();
                return true;
            }

            Emotion emotion = emotionList.get(position);
            if (emotion != null) {
                onEmoSelected(emotion);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface OnFaceSelectedListener {
        void onFaceSelected(Emotion faceObject, boolean isGifEmo);
        void onDeleteClicked();
    }

    public void setEmoSelectedListener(OnFaceSelectedListener emoSelectedListener) {
        this.mEmoSelectedListener = emoSelectedListener;
    }

    protected void onDeleteClicked() {
        if (mEmoSelectedListener != null) {
            mEmoSelectedListener.onDeleteClicked();
        }
    }

    protected void onEmoSelected(Emotion faceObject) {
        if (mEmoSelectedListener != null) {
            mEmoSelectedListener.onFaceSelected(faceObject, gifEmo);
        }
    }
}
