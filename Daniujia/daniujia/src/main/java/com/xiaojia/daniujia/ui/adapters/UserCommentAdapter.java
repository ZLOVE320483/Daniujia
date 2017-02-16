package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.UserCommentVo;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.util.List;

/**
 * Created by ZLOVE on 2016/4/27.
 */
public class UserCommentAdapter extends SingleDataListAdapter<UserCommentVo.CommentInfo> {

    public UserCommentAdapter(List<UserCommentVo.CommentInfo> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_comment, null);
            holder.ivAvatar = (RoundedImageView) convertView.findViewById(R.id.id_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.id_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.id_time);
            holder.tvContent = (TextView) convertView.findViewById(R.id.id_content);
            holder.tvServiceWay = (TextView) convertView.findViewById(R.id.id_service_way);
            holder.tvExpertReply = (TextView) convertView.findViewById(R.id.id_expert_reply);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserCommentVo.CommentInfo info = getItem(position);
        if (info != null) {
            if (!TextUtils.isEmpty(info.getImgurl())) {
                Picasso.with(mContext).load(info.getImgurl()).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).into(holder.ivAvatar);
            }else{
                Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).into(holder.ivAvatar);
            }
            if (!TextUtils.isEmpty(info.getName())) {
                holder.tvName.setText(info.getName());
            } else {
                holder.tvName.setText(info.getUsername());
            }
            holder.tvTime.setText(TimeUtils.getTime(info.getCommenttime() * 1000, TimeUtils.DATE_FORMAT_DATE));
            holder.tvContent.setText(info.getContent());
            if (info.getServiceType() == 1) {
                holder.tvServiceWay.setText("服务方式:图文咨询");
            } else if (info.getServiceType() == 2) {
                holder.tvServiceWay.setText("服务方式:电话咨询");
            } else if (info.getServiceType() == 3) {
                holder.tvServiceWay.setText("服务方式:线下咨询");
            }
            if (!TextUtils.isEmpty(info.getReply())) {
                holder.tvExpertReply.setVisibility(View.VISIBLE);
                holder.tvExpertReply.setText("专家回复：" + info.getReply());
            } else {
                holder.tvExpertReply.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolder {
        RoundedImageView ivAvatar;
        TextView tvName;
        TextView tvTime;
        TextView tvContent;
        TextView tvServiceWay;
        TextView tvExpertReply;
    }
}
