package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.CollectMeRespVo;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/5/9.
 */
public class CollectMeAdapter extends SingleDataListAdapter<CollectMeRespVo.FavoriteBean> {

    public CollectMeAdapter(List<CollectMeRespVo.FavoriteBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_collect_me, null);
            holder.ivIcon = (RoundedImageView) convertView.findViewById(R.id.id_avatar);
            holder.ivExpertAuth = (TextView) convertView.findViewById(R.id.tv_call_item_collect_me);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_collect_me);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CollectMeRespVo.FavoriteBean item = getItem(position);
        if (item != null) {
            if(!TextUtils.isEmpty(item.getImgurl())){
                Picasso.with(mContext).load(item.getImgurl()).resize(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60)).into(holder.ivIcon);
            }else{
                Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60)).into(holder.ivIcon);
            }
            holder.tvName.setText(TextUtils.isEmpty(item.getName()) ? item.getUsername() : item.getName());
            if (item.getIdentity() == 1) {
                holder.ivExpertAuth.setVisibility(View.GONE);
            } else {
                holder.ivExpertAuth.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        RoundedImageView ivIcon;
        TextView tvName;
        TextView ivExpertAuth;
    }
}
