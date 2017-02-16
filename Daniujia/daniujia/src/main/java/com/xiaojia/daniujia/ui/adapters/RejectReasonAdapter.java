package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

import java.util.List;

/**
 * Created by ZLOVE on 2016/5/30.
 */
public class RejectReasonAdapter extends SingleDataListAdapter<String> {

    private int curPos = 0;

    public RejectReasonAdapter(List<String> data, Context context) {
        super(data, context);
    }

    public void setCurPos(int curPos) {
        this.curPos = curPos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_reject, null);
            holder.tvReason = (TextView) convertView.findViewById(R.id.reason);
            holder.ivSelect = (ImageView) convertView.findViewById(R.id.select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String reason = getItem(position);
        holder.tvReason.setText(reason);
        if (position == curPos) {
            holder.ivSelect.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelect.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvReason;
        ImageView ivSelect;
    }
}
