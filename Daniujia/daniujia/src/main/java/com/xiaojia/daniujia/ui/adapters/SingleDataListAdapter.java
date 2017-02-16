package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xiaojia.daniujia.utils.ListUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/8.
 */
public abstract class SingleDataListAdapter<T> extends BaseAdapter {

    protected List<T> mData;
    protected Context mContext;

    public SingleDataListAdapter(List<T> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (ListUtils.isEmpty(mData)) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        if (ListUtils.isEmpty(mData)) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
