package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.R;

public class DepositSuccessDlg extends BaseDialog implements View.OnClickListener {

	private ImageView ivIcon;
	private Button mConfirm;
	private TextView mContent;
	private TextView mTitle;
	private ImageView mClose;

	public DepositSuccessDlg(Activity context) {
		super(context, R.layout.dlg_common_tip_with_icon);
		ivIcon = (ImageView) mView.findViewById(R.id.title_iv);
		mTitle = (TextView) mView.findViewById(R.id.title);
		mContent = (TextView) mView.findViewById(R.id.content);
		mConfirm = (Button) mView.findViewById(R.id.confirm);
		mClose = (ImageView) mView.findViewById(R.id.close);

		mConfirm.setOnClickListener(this);
		mClose.setOnClickListener(this);
	}

	public void initDlg(int iconRes, String title, String content){
		ivIcon.setImageResource(iconRes);
		mTitle.setText(title);
		mContent.setText(content);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {
		if (mClickListener != null){
			if (v == mConfirm) {
				mClickListener.onOK("Confirm");
			} else if (v == mClose) {
				mClickListener.onOK("Close");
			}
		}

	}

	public void showDialog() {
		// 设置窗口弹出动画
		getWindow().setWindowAnimations(R.style.centerDialogWindowAnim);
		// setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.gravity = Gravity.CENTER;
		getWindow().setAttributes(wl);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setCanceledOnTouchOutside(false);
		show();
	}
}
