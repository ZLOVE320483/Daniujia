package com.xiaojia.daniujia.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertSearchHotKeysVo;
import com.xiaojia.daniujia.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/26.
 */
public class HotLabelAdapter extends BaseAdapter {

    private List<ExpertSearchHotKeysVo.HotKeyInfo> tmpList;
    private Context context;

    public HotLabelAdapter(Context context, List<ExpertSearchHotKeysVo.HotKeyInfo> sourceList, int page) {
        this.context = context;
        tmpList = new ArrayList<>();
        for(int i = page * 10; i < page * 10 + 10 ;i++) {
            if(i == sourceList.size())
                break;
            tmpList.add(sourceList.get(i));
        }
    }

    @Override
    public int getCount() {
        if (ListUtils.isEmpty(tmpList)) {
            return 0;
        }
        return tmpList.size();
    }

    @Override
    public ExpertSearchHotKeysVo.HotKeyInfo getItem(int position) {
        return tmpList == null ? null : tmpList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_hot_label, null);
            holder.tvHotLabel = (TextView) convertView.findViewById(R.id.id_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ExpertSearchHotKeysVo.HotKeyInfo item = getItem(position);
        if (item != null) {
            holder.tvHotLabel.setText(item.getKeyword());
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvHotLabel;
    }


}
