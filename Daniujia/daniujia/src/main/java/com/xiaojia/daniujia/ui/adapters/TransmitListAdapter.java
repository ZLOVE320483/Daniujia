package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.PullConversationsVo;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.DisplayUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class TransmitListAdapter extends SingleDataListAdapter<PullConversationsVo.DataEntity>{

    public TransmitListAdapter(List<PullConversationsVo.DataEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_record_transmitlist, null);
            holder.ivHead = (RoundedImageView) convertView.findViewById(R.id.iv_cr_head);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_cr_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PullConversationsVo.DataEntity item = getItem(position);
        if (item != null) {
            PullConversationsVo.DataEntity.TargetEntity entity = item.getTarget();
            if (entity != null) {
                if (TextUtils.isEmpty(entity.getAvatarUrl())) {
                    holder.ivHead.setImageResource(R.drawable.ic_avatar_default);
                } else {
                    DisplayUtil.display(entity.getAvatarUrl()).resize(ScreenUtils.dip2px(60.0f), ScreenUtils.dip2px(60.0f)).into(holder.ivHead);
                }
                if (!TextUtils.isEmpty(entity.getName())) {
                    if (entity.getName().equals("eric")) {
                        holder.tvName.setText(R.string.app_name);
                    } else {
                        holder.tvName.setText(entity.getName());
                    }
                } else {
                    holder.tvName.setText(entity.getUsername());
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        RoundedImageView ivHead;
        TextView tvName;
    }
}
