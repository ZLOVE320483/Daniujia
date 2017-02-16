package com.xiaojia.daniujia.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.AccBalanceVo.RecordItem;
import com.xiaojia.daniujia.utils.DateUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import java.text.DecimalFormat;
import java.util.List;

public class BalanceListAdapter extends SingleDataListAdapter<RecordItem> {

	private DecimalFormat df = new DecimalFormat("###0.00");
	public BalanceListAdapter(List<RecordItem> balanceRecordList, Context context) {
		super(balanceRecordList, context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = SysUtil.inflate(R.layout.balance_list_item);
			holder.descTv = (TextView) convertView.findViewById(R.id.desc);
			holder.leftMoneyTv = (TextView) convertView.findViewById(R.id.left_money);
			holder.deltaFeeTv = (TextView) convertView.findViewById(R.id.delta_fee);
			holder.dateTv = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		RecordItem item = mData.get(position);
		holder.descTv.setText(item.descrip);
		holder.leftMoneyTv.setText(df.format(item.actbalance ) + "");
		if (item.flag == 1) {// 支出
			holder.deltaFeeTv.setTextColor(Color.RED);
			holder.deltaFeeTv.setText("-" + df.format(item.amount));
		} else if (item.flag == 2) {// 收入
			holder.deltaFeeTv.setTextColor(Color.GREEN);
			holder.deltaFeeTv.setText("+" + df.format(item.amount));
		}
		holder.dateTv.setText(DateUtil.formatCouponDate(item.recordtime));
		return convertView;
	}

	class Holder {
		TextView descTv;
		TextView leftMoneyTv;
		TextView deltaFeeTv;
		TextView dateTv;
	}
}
