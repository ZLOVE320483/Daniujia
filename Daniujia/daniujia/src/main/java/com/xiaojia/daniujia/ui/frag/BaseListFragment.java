/*
 * Copyright 2014 HuHoo. All rights reserved.
 * HuHoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @BaseListFragment.java - 2014年4月29日
 */

package com.xiaojia.daniujia.ui.frag;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.ApplicationUtil;

/**
 * BaseListFragment
 * 
 * @author OCEAN
 */
public abstract class BaseListFragment<A extends BaseAdapter> extends BaseAdapterViewFragment<A, ListView> {

    @Override
    protected int getInflateLayout() {
        return R.layout.layout_base_list_fragment;
    }

    @Override
    protected int getAdapterViewId() {
        return R.id.id_base_list;
    }
    
    protected void addHeaders() {
    	
    }

    @Override
    protected void configAdapterView(ListView listView) {
        listView.setDivider(getDividerDrawable());
        listView.setDividerHeight(getDividerHeight());
    }

    /**
     * 
     * @return 分割Drawable
     */
    protected Drawable getDividerDrawable() {
        return new ColorDrawable(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.common_line_color));
    }

    /**
     * 
     * @return 分割高度
     */
    protected int getDividerHeight() {
        return ApplicationUtil.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.list_divider);
    }

}
