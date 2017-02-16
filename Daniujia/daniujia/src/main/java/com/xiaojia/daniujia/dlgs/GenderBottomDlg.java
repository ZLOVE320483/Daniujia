package com.xiaojia.daniujia.dlgs;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class GenderBottomDlg extends BaseBottomDialog implements android.view.View.OnClickListener {

	public GenderBottomDlg(Activity context) {
		super(context, LayoutInflater.from(context).inflate(R.layout.dlg_bottom_choose_gender, null));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView.findViewById(R.id.cancel).setOnClickListener(this);
		mView.findViewById(R.id.male).setOnClickListener(this);
		mView.findViewById(R.id.female).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			break;
		case R.id.male:
			if (mClickListener != null) {
				Bundle b = new Bundle();
				b.putChar(ExtraConst.EXTRA_PROFILE_GENDER, 'M');
				mClickListener.onOK(b);
			}
			break;
		case R.id.female:
			if (mClickListener != null) {
				Bundle b = new Bundle();
				b.putChar(ExtraConst.EXTRA_PROFILE_GENDER, 'F');
				mClickListener.onOK(b);
			}
			break;
		}
		dismiss();
	}

}
