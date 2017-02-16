package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.frag.FilePreViewFragment;

public class AttachBottomDlg extends BaseBottomDialog implements View.OnClickListener {
	public AttachBottomDlg(Activity context) {
		super(context, LayoutInflater.from(context).inflate(R.layout.dlg_bottom_open_attach, null));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView.findViewById(R.id.cancel).setOnClickListener(this);
		mView.findViewById(R.id.send).setOnClickListener(this);
		mView.findViewById(R.id.open).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			break;
		case R.id.send:
			if (mClickListener != null) {
				mClickListener.onOK("send");
			}
			break;
		case R.id.open:
			if (mClickListener != null) {
				mClickListener.onOK("open");
			}
			break;
		}
		dismiss();
	}

	public void initOpenButton(int downloadStatus) {
		if (downloadStatus == FilePreViewFragment.DOWNLOAD_SUCCESS) {
			mView.findViewById(R.id.open).setVisibility(View.VISIBLE);
		} else {
			mView.findViewById(R.id.open).setVisibility(View.GONE);
		}
	}
}
