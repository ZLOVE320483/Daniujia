package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;

public class BaseMsgDlg extends BaseDialog implements View.OnClickListener {
	private TextView mTitleTv, mMsgTv;
	private Button mConfirmBtn, mCancelBtn;

	public BaseMsgDlg(Activity context) {
		super(context, R.layout.dlg_msg);
		mTitleTv = (TextView) mView.findViewById(R.id.title);
		mMsgTv = (TextView) mView.findViewById(R.id.msg);
		mConfirmBtn = (Button) mView.findViewById(R.id.confirm);
		mCancelBtn = (Button) mView.findViewById(R.id.cancel);
		mConfirmBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
	}

	public void setTitle(int resId) {
		mTitleTv.setText(resId);
	}
	
	public void setTitle(String title) {
		mTitleTv.setText(title);
	}

	public void setMsg(int resId) {
		mMsgTv.setText(resId);
	}
	
	public void setMsg(String msg) {
		mMsgTv.setText(msg);
	}
	
	public void setMsg(String msg, int textSize, int colorResId) {
		mMsgTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		mMsgTv.setTextColor(App.get().getResources().getColor(colorResId));
		mMsgTv.setText(msg);
	}

	public void setMsgGravity(int gravity){
		mMsgTv.setGravity(gravity);
	}

	public void setMsgTextSize(int textSize){
		mMsgTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}

	public void setBtnName(int whichBtn, int btnResId) {
		switch (whichBtn) {
		case DialogInterface.BUTTON_POSITIVE:
			mConfirmBtn.setText(btnResId);
			mConfirmBtn.setVisibility(View.VISIBLE);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			mCancelBtn.setText(btnResId);
			mCancelBtn.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			if (mClickListener != null) {
				mClickListener.onOK();
			}
			break;
		case R.id.cancel:
			if (mClickListener != null) {
				mClickListener.onCancel();
			}
			break;
		}
		dismiss();
	}

}
