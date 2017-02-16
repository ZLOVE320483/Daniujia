/*
 * Copyright 2014 HuHoo. All rights reserved.
 * HuHoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @BaseListFragment.java - 2014年4月29日
 */

package com.xiaojia.daniujia.ui.frag;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * BaseListFragment
 * @author OCEAN
 */
@SuppressWarnings("rawtypes")
public abstract class BaseAdapterViewFragment<A extends BaseAdapter, T extends AdapterView> extends BaseFragment {

    private T adapterView;

    private A adapter;

    /**
     * 用于给 adapterView 赋值
     * @return AdapterView的Id
     */
    protected abstract int getAdapterViewId();

    protected abstract A initAdapter(T adapterView);
    
    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView(View view) {
        adapterView = setUpAdapterView(view);
        adapter = initAdapter(adapterView);
        if (adapterView != null && adapter != null) {
            configAdapterView(adapterView);
            adapterView.setAdapter(adapter);
        }
    }

    /**
     * 返回AdapterView
     * @param view
     * @return AdapterView
     */
    @SuppressWarnings("unchecked")
    protected T setUpAdapterView(View view){
        return (T)view.findViewById(getAdapterViewId());
    }

    /**
     * 配置AdapterView，可以用子类设置AdapterView的一些熟悉
     * @param adapterView
     */
    protected void configAdapterView(T adapterView) {
        
    }
    
    protected T getAdapterView(){
        return adapterView;
    }
    
    public A getAdapter() {
        return adapter;
    }
    
    /**
     * 将数据添加到AdapterView中，用于刷新数据
     * @param objList 数据List
     * @param arrayAdapter
     */
    public static <T extends Object> void setListToArrayAdapter(List<T> objList, ArrayAdapter<T> arrayAdapter) {
        if (arrayAdapter == null) {
            return;
        }
        
        arrayAdapter.clear();
        appendListToArrayAdapter(objList, arrayAdapter);
    }

    /**
     * 将数据添加到AdapterView中，用于加载更多数据
     * @param objList
     * @param arrayAdapter
     */
    public static <T extends Object> void appendListToArrayAdapter(List<T> objList, ArrayAdapter<T> arrayAdapter) {
        if (objList != null) {
            for (T object : objList) {
                if (object != null) {
                    arrayAdapter.add(object);
                }
            }
        }
    }
}
