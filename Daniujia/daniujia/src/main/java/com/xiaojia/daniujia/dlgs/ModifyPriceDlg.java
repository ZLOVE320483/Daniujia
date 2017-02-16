package com.xiaojia.daniujia.dlgs;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.WUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class ModifyPriceDlg extends BaseDialog implements android.view.View.OnClickListener {
	private int type;
	private String defPriceStr;
	private EditText priceEt;

	public ModifyPriceDlg(Activity context, int type, String defPriceStr) {
		super(context, R.layout.dlg_modify_price);
		this.type = type;
		this.defPriceStr = defPriceStr;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView typeTv = (TextView) mView.findViewById(R.id.type);
		TextView priceUnitTv = (TextView) mView.findViewById(R.id.price_unit);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		switch (type) {
		case 0:
			typeTv.setText(R.string.graphics_consult);
			priceUnitTv.setText(R.string.rmb_per_count);
			break;
		case 1:
			typeTv.setText(R.string.network_chat);
			priceUnitTv.setText(R.string.rmb_per_minute);
			break;
		case 2:
			typeTv.setText(R.string.offline_consult);
			priceUnitTv.setText(R.string.rmb_per_hour);
			break;
		}
		priceEt = (EditText) mView.findViewById(R.id.et_price);
		WUtil.setTextEndCursor(priceEt, defPriceStr);
		mView.findViewById(R.id.cancel).setOnClickListener(this);
		mView.findViewById(R.id.confirm).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			dismiss();
			break;
		case R.id.confirm:
			String priceStr = priceEt.getText().toString();
			if (TextUtils.isEmpty(priceStr)) {
				WUtil.setWarnText(priceEt, R.string.price);
			} else if (mClickListener != null) {
				Bundle b = new Bundle();
				b.putInt(ExtraConst.EXTRA_CONSULT_TYPE, type);
				b.putString(ExtraConst.EXTRA_CONSULT_PRICE, priceStr);
				mClickListener.onOK(b);
				dismiss();
			}
			break;
		}
	}

}
