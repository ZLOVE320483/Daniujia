package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.MyAppointExpert;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class ExpertOrderAdapter extends SingleDataListAdapter<MyAppointExpert.OrdersEntity> {

    public ExpertOrderAdapter(List<MyAppointExpert.OrdersEntity> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_expert_order_new, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyAppointExpert.OrdersEntity item = getItem(position);
        if (item != null) {
            if (!TextUtils.isEmpty(item.getImgurl())) {
                Picasso.with(mContext).load(item.getImgurl()).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(viewHolder.header);
            }else{
                Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(viewHolder.header);
            }
            if (item.isRead()) {
                viewHolder.consultType.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
                viewHolder.name.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
                viewHolder.consultType.getPaint().setFakeBoldText(false);
            } else {
                viewHolder.consultType.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
                viewHolder.name.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
                viewHolder.consultType.getPaint().setFakeBoldText(true);
            }
            if (!TextUtils.isEmpty(item.getName())) {
                viewHolder.name.setText(item.getName());
            } else {
                viewHolder.name.setText(item.getUsername());
            }
            viewHolder.totalPrice.setText(item.getTotalPrice());

            int serviceType = item.getServiceType();
            if (serviceType == 1) {
                viewHolder.consultType.setText("图文约谈");
                viewHolder.perPrice.setText("单价: " + item.getServicePrice() + " 元/次");
                viewHolder.time.setVisibility(View.GONE);
                viewHolder.tvHasCall.setVisibility(View.GONE);
            } else if (serviceType == 2) {
                viewHolder.consultType.setText("电话约谈");
                viewHolder.perPrice.setText("单价: " + item.getServicePrice() + " 元/分钟");
                viewHolder.time.setVisibility(View.VISIBLE);
                viewHolder.time.setText("总计: " + item.getServiceCnt() + " 分钟");
                viewHolder.tvHasCall.setVisibility(View.VISIBLE);
                viewHolder.tvHasCall.setText("已通话: " + item.getDuration() + " 分钟");
            } else if (serviceType == 3) {
                viewHolder.consultType.setText("线下约谈");
                viewHolder.perPrice.setText("单价: " + item.getServicePrice() + " 元/小时");
                viewHolder.time.setVisibility(View.VISIBLE);
                viewHolder.time.setText("总计: " + item.getServiceCnt() + " 小时");
                viewHolder.tvHasCall.setVisibility(View.GONE);
            }
            int orderState = item.getStatus();
            if (orderState == 0) {
                viewHolder.state.setText("待确认");
                viewHolder.state.setBackgroundColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.order_state_color_wait));
            } else if (orderState == 1) {
                viewHolder.state.setText("待支付");
                viewHolder.state.setBackgroundColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.order_state_color_wait));
            } else if (orderState == 2) {
                viewHolder.state.setText("进行中");
                viewHolder.state.setBackgroundColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.order_state_color_doing));
            } else if (orderState == 3) {
                viewHolder.state.setText("待评价");
                viewHolder.state.setBackgroundColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.order_state_color_wait));
            } else if (orderState == 10) {
                viewHolder.state.setText("已取消");
                viewHolder.state.setBackgroundColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.order_state_color_cancel));
            } else if (orderState == 11) {
                viewHolder.state.setText("已拒绝");
                viewHolder.state.setBackgroundColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.order_state_color_cancel));
            } else if (orderState == 12) {
                viewHolder.state.setText("已完成");
                viewHolder.state.setBackgroundColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.order_state_color_finished));
            }
        }

        return convertView;
    }

    class ViewHolder {
        private RoundedImageView header;
        private TextView perPrice;
        private TextView name;
        private TextView consultType;
        private TextView state;
        private TextView totalPrice;
        private TextView time;
        private TextView tvHasCall;

        public ViewHolder(View convertView) {
            header = (RoundedImageView) convertView.findViewById(R.id.expert_header);
            perPrice = (TextView) convertView.findViewById(R.id.per_price);
            name = (TextView) convertView.findViewById(R.id.expert_name);
            consultType = (TextView) convertView.findViewById(R.id.consult_type);
            state = (TextView) convertView.findViewById(R.id.order_state);

            totalPrice = (TextView) convertView.findViewById(R.id.total_price);
            time = (TextView) convertView.findViewById(R.id.total_time);
            tvHasCall = (TextView) convertView.findViewById(R.id.has_call);
            convertView.setTag(this);
        }
    }
}
