package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.SearchDataEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */
public class LookForDirectionAdapter extends SingleDataListAdapter<SearchDataEntity.Industries>{

    public LookForDirectionAdapter(List<SearchDataEntity.Industries> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_look_for_direction,null);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.item_look_for_tv);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.item_look_for_iv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mData.get(position).getDesc())) {
            viewHolder.tv.setText(mData.get(position).getDesc());
        }

        if (!TextUtils.isEmpty(mData.get(position).getIconUrl())){
            Picasso.with(mContext).load(mData.get(position).getIconUrl()).into(viewHolder.iv);
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
