package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/26.
 */
public class SearchRecordAdapter extends SingleDataListAdapter<String> {

    public SearchRecordAdapter(List<String> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_search_record, null);
            holder.tvRecord = (TextView) convertView.findViewById(R.id.id_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        if (!TextUtils.isEmpty(item)) {
            holder.tvRecord.setText(item);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvRecord;
    }
}
