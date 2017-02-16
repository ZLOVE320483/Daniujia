package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertSearchHotKeysVo;
import com.xiaojia.daniujia.ui.adapters.HotLabelAdapter;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/26.
 */
public class ExpertSearchHotLabelLayout extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private NoScrollViewPager viewPager;
    private LinearLayout llDotContainer;
    private List<View> gridViews = new ArrayList<>();
    private ImageView[] tips;
    private List<ExpertSearchHotKeysVo.HotKeyInfo> items;
    private int page = 0;
    private SearchHotKeysListener listener;

    public ExpertSearchHotLabelLayout(Context context) {
        super(context);
        this.mContext = context;
    }

    public ExpertSearchHotLabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ExpertSearchHotLabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_expert_search_hot_label, this);
        viewPager = (NoScrollViewPager) view.findViewById(R.id.id_viewpager);
        llDotContainer = (LinearLayout) view.findViewById(R.id.ll_dot_container);

        viewPager.setOnPageChangeListener(this);
    }

    public void setData(List<ExpertSearchHotKeysVo.HotKeyInfo> items) {
        this.items = items;
        if (ListUtils.isEmpty(items)) {
            return;
        }
        int size = items.size();
        int len = size / 10;
        if (size % 10 == 0) {
            page = len;
        } else {
            page = len + 1;
        }

        viewPager.setAdapter(new HotLabelPageAdapter());

        if (gridViews.size() <= 1) {
            llDotContainer.setVisibility(GONE);
            return;
        }

        viewPager.setEnableScroll(true);
        tips = new ImageView[gridViews.size()];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(8, 8));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.ic_dot_checked);
            } else {
                tips[i].setBackgroundResource(R.drawable.ic_dot_unchecked);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            llDotContainer.addView(imageView, layoutParams);
        }
    }

    class HotLabelPageAdapter extends PagerAdapter {

        public HotLabelPageAdapter() {
            for (int i = 0; i < page ; i++) {
                GridView gridView = new GridView(mContext);
                gridView.setNumColumns(5);
                gridView.setAdapter(new HotLabelAdapter(mContext, items, i));
                gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int realPos = position % 10 + viewPager.getCurrentItem() * 10;
                        if (listener != null) {
                            listener.searchHotKey(items.get(realPos).getKeyword());
                        }
                    }
                });
                gridViews.add(gridView);
            }
        }

        @Override
        public int getCount() {
            return page;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = gridViews.get(position);
            container.addView(view);
            return view;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0);
    }

    private void setImageBackground(int selectItems){
        if (tips == null || tips.length == 0) {
            return;
        }
        for(int i=0; i < tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.ic_dot_checked);
            }else{
                tips[i].setBackgroundResource(R.drawable.ic_dot_unchecked);
            }
        }
    }

    public interface SearchHotKeysListener {
        void searchHotKey(String hotKey);
    }

    public void setListener(SearchHotKeysListener listener) {
        this.listener = listener;
    }
}
