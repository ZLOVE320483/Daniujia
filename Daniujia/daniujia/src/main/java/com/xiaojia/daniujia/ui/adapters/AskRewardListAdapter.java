package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AskRewardListRespVo;
import com.xiaojia.daniujia.ui.views.MoreLineTextView;
import com.xiaojia.daniujia.utils.DateUtil;
import com.xiaojia.daniujia.utils.PatternUtil;

import java.util.List;

/**
 * Created by ZLOVE on 2016/10/24.
 */
public class AskRewardListAdapter extends SingleDataListAdapter<AskRewardListRespVo.AskRewardListItem> {

    private boolean isFinished;

    public AskRewardListAdapter(List<AskRewardListRespVo.AskRewardListItem> data, Context context, boolean isFinished) {
        super(data, context);
        this.isFinished = isFinished;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_ask_reward, null);
            holder.tvAskPrice = (TextView) convertView.findViewById(R.id.price);
            holder.ivSafeVerify = (ImageView) convertView.findViewById(R.id.safe_verify);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.user_name);
            holder.ivAskVerify = (ImageView) convertView.findViewById(R.id.ask_verify);
            holder.tvAskTitle = (MoreLineTextView) convertView.findViewById(R.id.title);
            holder.tvAskContent = (MoreLineTextView) convertView.findViewById(R.id.content);
            holder.tvAnswerCount = (TextView) convertView.findViewById(R.id.answer_count);
            holder.tvFollowCount = (TextView) convertView.findViewById(R.id.follow_count);
            holder.tvLeftDayCount = (TextView) convertView.findViewById(R.id.left_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AskRewardListRespVo.AskRewardListItem item = getItem(position);
        if (item != null) {
            holder.tvAskPrice.setText("￥" + item.getPrice());
            holder.tvUserName.setText(item.getWriteName());
            holder.tvAskTitle.setText(item.getTitle());
            holder.tvAskContent.setText(Html.fromHtml(PatternUtil.replaceString2(item.getContent(),PatternUtil.IMG_REGEX)));
            holder.tvAnswerCount.setText("回答 " + item.getAnswerCount());
            holder.tvFollowCount.setText("关注 " + item.getFollowCount());
            int leftDay = DateUtil.getDistanceDayCounts(System.currentTimeMillis(), item.getDeadline() * 1000);
            holder.tvLeftDayCount.setText("剩余天数 " + leftDay);
            if (isFinished) {
                holder.ivSafeVerify.setImageResource(R.drawable.safe_grey);
                holder.tvAskPrice.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            } else {
                holder.ivSafeVerify.setImageResource(R.drawable.safe_green);
                holder.tvAskPrice.setTextColor(mContext.getResources().getColor(R.color.common_orange));
            }
            String status = item.getStatus();
            if (status.equals("published") || status.equals("ending") || status.equals("closed")) {
                holder.ivAskVerify.setVisibility(View.VISIBLE);
            } else {
                holder.ivAskVerify.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvAskPrice;
        ImageView ivSafeVerify;
        TextView tvUserName;
        ImageView ivAskVerify;
        MoreLineTextView tvAskTitle;
        MoreLineTextView tvAskContent;
        TextView tvAnswerCount;
        TextView tvFollowCount;
        TextView tvLeftDayCount;
    }
}
