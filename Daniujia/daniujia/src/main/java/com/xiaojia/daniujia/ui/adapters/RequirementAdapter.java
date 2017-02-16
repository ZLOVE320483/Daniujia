package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.RequireListEntity;
import com.xiaojia.daniujia.ui.views.CircleImageView;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */
public class RequirementAdapter extends SingleDataListAdapter<RequireListEntity> {
    private boolean isLooking = false;
    private boolean isShowStatus = false;

    public RequirementAdapter(List<RequireListEntity> data, Context context, boolean isLooking, boolean showStatus) {
        super(data, context);
        this.isLooking = isLooking;
        this.isShowStatus = showStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_requirement_listview, null);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.requirement_item_address);
            viewHolder.tvPosition = (TextView) convertView.findViewById(R.id.requirement_item_position);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.requirement_item_description);
            viewHolder.tvBudget = (TextView) convertView.findViewById(R.id.requirement_item_budget);
            viewHolder.tvServerTime = (TextView) convertView.findViewById(R.id.requirement_item_server_time);
            viewHolder.tvExpertCount = (TextView) convertView.findViewById(R.id.requirement_item_expert_count);
            viewHolder.tvViewCount = (TextView) convertView.findViewById(R.id.requirement_item_view_count);
            viewHolder.tvTimeTo = (TextView) convertView.findViewById(R.id.requirement_item_time_to);
            viewHolder.rlCooperation = (RelativeLayout) convertView.findViewById(R.id.requirement_item_rl_cooperation);
            viewHolder.cvOne = (CircleImageView) convertView.findViewById(R.id.requirement_item_circle_one);
            viewHolder.cvTwo = (CircleImageView) convertView.findViewById(R.id.requirement_item_circle_two);
            viewHolder.viewLine = convertView.findViewById(R.id.requirement_item_line);
            viewHolder.tvAtPresent = (TextView) convertView.findViewById(R.id.requirement_item_at_present);
            viewHolder.mLlStatus = (LinearLayout) convertView.findViewById(R.id.require_ll_status);
            viewHolder.tvPublisher = (TextView) convertView.findViewById(R.id.require_publisher_name);
            viewHolder.viewSignUp = convertView.findViewById(R.id.require_view_sign_up_dot);
            viewHolder.viewStatusDot = convertView.findViewById(R.id.require_view_status_dot);
            viewHolder.viewStatusLine = convertView.findViewById(R.id.require_view_line);
            viewHolder.tvStatusText = (TextView) convertView.findViewById(R.id.require_text_status);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isLooking) {
            viewHolder.rlCooperation.setVisibility(View.GONE);
            viewHolder.viewLine.setVisibility(View.GONE);
        } else {
            viewHolder.rlCooperation.setVisibility(View.VISIBLE);
            viewHolder.viewLine.setVisibility(View.VISIBLE);
            List<RequireListEntity.ConfirmExpert> list = mData.get(position).getConfirmedExperts();
            if (list == null || list.size() == 0){
                viewHolder.cvOne.setVisibility(View.GONE);
                viewHolder.cvTwo.setVisibility(View.GONE);

            } else {
                if (list.size() == 1){
                    viewHolder.cvOne.setVisibility(View.VISIBLE);
                    viewHolder.cvTwo.setVisibility(View.GONE);
                    Picasso.with(mContext).load(list.get(0).getImgUrl()).resize(ScreenUtils.dip2px(30),ScreenUtils.dip2px(30)).
                            placeholder(R.drawable.ic_avatar_default).error(R.drawable.ic_avatar_default).into(viewHolder.cvOne);
                } else {
                    viewHolder.cvOne.setVisibility(View.VISIBLE);
                    viewHolder.cvTwo.setVisibility(View.VISIBLE);
                    Picasso.with(mContext).load(list.get(0).getImgUrl()).resize(ScreenUtils.dip2px(30),ScreenUtils.dip2px(30)).
                            placeholder(R.drawable.ic_avatar_default).error(R.drawable.ic_avatar_default).into(viewHolder.cvOne);

                    Picasso.with(mContext).load(list.get(1).getImgUrl()).resize(ScreenUtils.dip2px(30),ScreenUtils.dip2px(30)).
                            placeholder(R.drawable.ic_avatar_default).error(R.drawable.ic_avatar_default).into(viewHolder.cvTwo);
                }
            }
        }

        if (isShowStatus) {
            viewHolder.mLlStatus.setVisibility(View.VISIBLE);
            viewHolder.tvPublisher.setVisibility(View.VISIBLE);

            String status = mData.get(position).getStatus();
            if (!TextUtils.isEmpty(status)){
                if (status.equals(RequireListEntity.CLOSED)){
                    viewHolder.tvStatusText.setText("需求已关闭");
                    viewHolder.tvStatusText.setTextColor(mContext.getResources().
                            getColor(R.color.order_state_color_cancel));
                    viewHolder.viewStatusLine.setBackgroundColor(mContext.getResources().
                            getColor(R.color.order_state_color_cancel));

                } else if (status.equals(RequireListEntity.CONFIREMED)){
                    viewHolder.tvStatusText.setText("顾问已确定");
                    viewHolder.tvStatusText.setTextColor(mContext.getResources().
                            getColor(R.color.order_state_color_finished));
                    viewHolder.viewStatusLine.setBackgroundColor(mContext.getResources().
                            getColor(R.color.order_state_color_finished));


                } else if (status.equals(RequireListEntity.SEARCHING)){
                    viewHolder.tvStatusText.setText("顾问寻找中");
                    viewHolder.tvStatusText.setTextColor(mContext.getResources().
                            getColor(R.color.order_state_color_doing));
                    viewHolder.viewStatusLine.setBackgroundColor(mContext.getResources().
                            getColor(R.color.order_state_color_doing));
                } else {
                    viewHolder.tvStatusText.setText("需求待确认");
                    viewHolder.tvStatusText.setTextColor(mContext.getResources().
                            getColor(R.color.order_state_color_wait));
                    viewHolder.viewStatusLine.setBackgroundColor(mContext.getResources().
                            getColor(R.color.order_state_color_wait));
                }
            } else {
                viewHolder.mLlStatus.setVisibility(View.GONE);
            }

            // 发布者名称
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ask_verify);
            if (mData.get(position).isCertified()){
                drawable.setBounds(0,0,drawable.getIntrinsicHeight(),drawable.getIntrinsicWidth());
            } else {
                drawable.setBounds(0,0,0,0);
            }
            viewHolder.tvPublisher.setCompoundDrawables(null,null,drawable,null);
            viewHolder.tvPublisher.setText(mData.get(position).getWriteName());

        } else {
            viewHolder.mLlStatus.setVisibility(View.GONE);
            viewHolder.tvPublisher.setVisibility(View.GONE);

        }

        if (mData.get(position).isStatusChanged()){
            viewHolder.viewStatusDot.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewStatusDot.setVisibility(View.GONE);
        }

        if (mData.get(position).isApplicantChanged()){
            viewHolder.viewSignUp.setVisibility(View.VISIBLE);
        } else {
            viewHolder.viewSignUp.setVisibility(View.GONE);
        }

        viewHolder.tvPosition.setText(mData.get(position).getConsultantName());
        viewHolder.tvAddress.setText(mData.get(position).getCityName());

        if (TextUtils.isEmpty(mData.get(position).getDeadline())){
            viewHolder.tvTimeTo.setText("已完成");
        } else {
            viewHolder.tvTimeTo.setText("有效期  "+ TimeUtils.getTime(Long.parseLong(mData.get(position).getDeadline()) * 1000,
                    TimeUtils.DATE_FORMAT_DATE));
        }

        viewHolder.tvBudget.setText(mData.get(position).getBudget());
        viewHolder.tvViewCount.setText("浏览数 "+ mData.get(position).getViewCount());
        viewHolder.tvExpertCount.setText("报名专家 " + mData.get(position).getApplicantCount());
        viewHolder.tvDescription.setText(mData.get(position).getDesc());
        viewHolder.tvServerTime.setText("服务期 " + mData.get(position).getServiceCycle());
        viewHolder.tvAtPresent.setText("到现场服务说明：" + mData.get(position).getServiceDay());

        return convertView;
    }

    class ViewHolder {
        TextView tvAddress;
        TextView tvPosition;
//        // 预付款
//        TextView tvPrePay;
        TextView tvDescription;
        // 预算
        TextView tvBudget;
        TextView tvServerTime;
        TextView tvExpertCount;
        TextView tvViewCount;
        TextView tvTimeTo;
        TextView tvAtPresent;

        RelativeLayout rlCooperation;
        CircleImageView cvOne, cvTwo;
        View viewLine;

        LinearLayout mLlStatus;
        TextView tvPublisher;
        View viewStatusDot,viewSignUp;
        View viewStatusLine;
        TextView tvStatusText;
    }
}
