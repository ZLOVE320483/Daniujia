package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.MyCommentToConsultantRespVo;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.DateUtil;

import java.util.List;

/**
 * Created by ZLOVE on 2017/1/3.
 */
public class MyCommentToConsultantAdapter extends SingleDataListAdapter<MyCommentToConsultantRespVo.Evaluation> {

    private String expertAvatarUrl;
    private String expertName;

    public MyCommentToConsultantAdapter(List<MyCommentToConsultantRespVo.Evaluation> data, Context context) {
        super(data, context);
    }

    public void setExpertInfo(String expertAvatarUrl, String expertName) {
        this.expertAvatarUrl = expertAvatarUrl;
        this.expertName = expertName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_comment_to_consultant, null);
            holder.ivAvatar = (RoundedImageView) convertView.findViewById(R.id.id_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.id_name);
            holder.tvContent = (TextView) convertView.findViewById(R.id.id_content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.id_time);
            holder.ivDivider = (ImageView) convertView.findViewById(R.id.id_divider_1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyCommentToConsultantRespVo.Evaluation evaluation = getItem(position);
        if (evaluation != null) {
            holder.tvContent.setText(evaluation.getContent());
            holder.tvTime.setText(DateUtil.formatCouponDate(evaluation.getDatetime()));
        }
        if (position == 0) {
            holder.ivAvatar.setVisibility(View.VISIBLE);
            holder.tvName.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(expertAvatarUrl).into(holder.ivAvatar);
            holder.tvName.setText(expertName);
        } else {
            holder.ivAvatar.setVisibility(View.GONE);
            holder.tvName.setVisibility(View.GONE);
        }
        if (position == getCount() - 1) {
            holder.ivDivider.setVisibility(View.GONE);
        } else {
            holder.ivDivider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        RoundedImageView ivAvatar;
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        ImageView ivDivider;
    }
}
