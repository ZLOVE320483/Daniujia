package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.GiftItem;
import com.xiaojia.daniujia.ui.views.RotateTextView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.DateUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3
 */
public class CouponAdapter extends SingleDataListAdapter<GiftItem> {

    public CouponAdapter(List<GiftItem> data, Context context) {
        super(data, context);
    }

    private boolean isHistory;

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_coupon, null);
            holder.tvValidDate = (TextView) convertView.findViewById(R.id.validity);
            holder.tvValue = (TextView) convertView.findViewById(R.id.coupon_value);
            holder.tvMoneyTag = (TextView) convertView.findViewById(R.id.money_tag);
            holder.tvCouponType = (TextView) convertView.findViewById(R.id.coupon_type);
            holder.tvHead = (TextView) convertView.findViewById(R.id.coupon_head);
            holder.tvRemainderDays = (RotateTextView) convertView.findViewById(R.id.remainder_days);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GiftItem item = getItem(position);

        if (isHistory) {
            holder.tvValue.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
            holder.tvMoneyTag.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
            holder.tvCouponType.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.second_text_color));
            holder.tvHead.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
            holder.tvRemainderDays.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
            convertView.setBackgroundResource(R.drawable.coupon_bg_invalid);
            if(item != null){
                if (item.usetime == 0){
                    holder.tvRemainderDays.setText("已过期");
                } else{
                    holder.tvRemainderDays.setText("已使用");
                }
            }
        } else {
            holder.tvValue.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.red_collection));
            holder.tvMoneyTag.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.red_collection));
            holder.tvCouponType.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.red_collection));
            holder.tvHead.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
            holder.tvRemainderDays.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.blue_text));
            convertView.setBackgroundResource(R.drawable.coupon_bg_valid);
            if (item != null){
                long nowTime = System.currentTimeMillis();
                // Log.w("zptest","nowTime: " + nowTime + "deadTime" + item.deadtime);这里的deadtime单位为秒
                int days = DateUtil.getDistanceDayCounts(nowTime,item.deadtime * 1000);
                if(days == 0){
                    holder.tvRemainderDays.setText("即将过期");
                }else{
                    holder.tvRemainderDays.setText("剩余"+days+"天");
                }

            }
        }
        if (item != null) {
            holder.tvValidDate.setText("有效期限至" + DateUtil.formatCouponDate(item.deadtime));
            holder.tvValue.setText(String.valueOf(item.value));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvValidDate;
        TextView tvValue;
        TextView tvMoneyTag;
        TextView tvCouponType;
        TextView tvHead;
        RotateTextView tvRemainderDays;
    }

}
