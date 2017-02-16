package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

import com.xiaojia.daniujia.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class GridViewSpaceAdapter extends SingleDataListAdapter<String>{
    public GridViewSpaceAdapter(List<String> data, Context context) {
        super(data, context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button btn;
        if (convertView == null) {
            btn = (Button) LayoutInflater.from(mContext).inflate(R.layout.gridview_btn, null);
        }else{
            btn = (Button) convertView;
        }
        btn.setText(mData.get(position));
        return btn;
    }
}
