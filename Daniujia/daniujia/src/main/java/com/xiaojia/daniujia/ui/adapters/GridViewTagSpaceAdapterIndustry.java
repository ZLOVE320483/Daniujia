package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.NovationByTopicVo;

import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class GridViewTagSpaceAdapterIndustry extends SingleDataListAdapter<NovationByTopicVo.FilterDirectionsEntity> {
    int selection = 0;

    public GridViewTagSpaceAdapterIndustry(List<NovationByTopicVo.FilterDirectionsEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView btn;
        if (convertView == null) {
            btn = (TextView) LayoutInflater.from(mContext).inflate(R.layout.gridview_btn, null);
        } else {
            btn = (TextView) convertView;
        }
        if (position == selection) {
            btn.setTextColor(mContext.getResources().getColor(R.color.theme_blue));
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        } else {
            btn.setTextColor(mContext.getResources().getColor(R.color.first_text_color));
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }
        btn.setText(mData.get(position).getName());
        return btn;
    }

    public void setSelection(int position) {
        selection = position;
        notifyDataSetChanged();
    }

    public int getSelection() {
        return selection;
    }
}
