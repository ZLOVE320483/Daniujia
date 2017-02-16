package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class ConditionSelectLeftAdapter extends SingleDataListAdapter<ExpertInfoEntity.Direction> {
    private boolean isLeftList;
    private int mCurrentSelect = -1;

    public ConditionSelectLeftAdapter(List<ExpertInfoEntity.Direction> data, Context context , boolean isLeftList) {
        super(data, context);
        this.isLeftList = isLeftList;
    }

    public void setData(List<ExpertInfoEntity.Direction> data){
        mData.clear();
        mData.addAll(data);
    }

    public void clearData(){
        mData.clear();
    }

    public ExpertInfoEntity.Direction getItemData(int position){
        return mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adater_select_condition,null);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.adapter_tv);
            viewHolder.iv = convertView.findViewById(R.id.adapter_blue_line);
            viewHolder.ll = (LinearLayout) convertView.findViewById(R.id.adapter_ll);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(mData.get(position).getName());

        if (isLeftList){
            if (position == mCurrentSelect){
                viewHolder.tv.setTextColor(Color.parseColor("#2594e9"));
                viewHolder.ll.setBackgroundColor(Color.WHITE);
                viewHolder.iv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv.setTextColor(Color.parseColor("#333333"));
                viewHolder.ll.setBackgroundColor(0xfff5f5f5);
                viewHolder.iv.setVisibility(View.INVISIBLE);
            }
        } else {
            if (position == mCurrentSelect){
                viewHolder.tv.setTextColor(Color.parseColor("#2594e9"));
                viewHolder.ll.setBackgroundColor(0xfffafafa);
            } else {
                viewHolder.tv.setTextColor(Color.parseColor("#333333"));
                viewHolder.ll.setBackgroundColor(Color.WHITE);
            }
            viewHolder.iv.setVisibility(View.INVISIBLE);
        }



        return convertView;
    }

    public void setmCurrentSelect(int mCurrentSelect) {
        this.mCurrentSelect = mCurrentSelect;
    }

    class ViewHolder{
        View iv;
        TextView tv;
        LinearLayout ll;
    }
}
