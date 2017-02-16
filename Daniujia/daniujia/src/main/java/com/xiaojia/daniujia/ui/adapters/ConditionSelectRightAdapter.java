package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
public class ConditionSelectRightAdapter extends SingleDataListAdapter<ExpertInfoEntity.Direction.SubDirection> {
    private boolean isLeftList;
    private int mCurrentSelect = -1;

    public ConditionSelectRightAdapter(List<ExpertInfoEntity.Direction.SubDirection> data, Context context , boolean isLeftList) {
        super(data, context);
        this.isLeftList = isLeftList;
    }

    public void setData(List<ExpertInfoEntity.Direction.SubDirection> data){
        mData.clear();
        mData.addAll(data);
    }

    public void clearData(){
        mData.clear();
    }

    public ExpertInfoEntity.Direction.SubDirection getItemData(int position){
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

        // 只显示右边的ListView时，隐藏掉左边的ListView，这时需要显示那个蓝色的竖条，所以isLeftList设置为true
        // 这里就专门给默认排序和按浏览数来使用，所以背景色都设置为white。
        if (isLeftList){
            if (position == mCurrentSelect){
                viewHolder.tv.setSelected(true);
                viewHolder.ll.setBackgroundColor(Color.WHITE);
                viewHolder.iv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv.setSelected(false);
                viewHolder.ll.setBackgroundColor(Color.WHITE);
                viewHolder.iv.setVisibility(View.INVISIBLE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.ll.setBackground(mContext.getResources().getDrawable(R.drawable.select_view_back_color));
            }
            if (position == mCurrentSelect){
                viewHolder.ll.setSelected(true);
                viewHolder.tv.setSelected(true);
            } else {
                viewHolder.ll.setSelected(false);
                viewHolder.tv.setSelected(false);
            }
            viewHolder.iv.setVisibility(View.INVISIBLE);
        }

        viewHolder.tv.setText(mData.get(position).getName());

        return convertView;
    }

    public void setLeftList(boolean leftList) {
        isLeftList = leftList;
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
