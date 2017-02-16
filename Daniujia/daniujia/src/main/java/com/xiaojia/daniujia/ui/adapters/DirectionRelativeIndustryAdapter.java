package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.RelatedIndustry;

import java.util.List;

/**
 * Created by ZLOVE on 2016/6/30.
 */
public class DirectionRelativeIndustryAdapter extends SingleDataListAdapter<RelatedIndustry> {

    public DirectionRelativeIndustryAdapter(List<RelatedIndustry> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_direction_relative_industry, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RelatedIndustry industry = getItem(position);
        if (industry != null) {
            holder.tvName.setText(industry.getIndustryName());
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }
}
