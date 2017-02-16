package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.toast.ToastCompat;
import com.xiaojia.daniujia.utils.ApplicationUtil;

public class BaseDialog extends Dialog {
	protected Activity mActivity;
	protected View mView;
	protected DialogClickListener mClickListener;

	public BaseDialog(Activity context, int resId) {
		super(context, R.style.CustomDialog);
		this.mActivity = context;
		mView = LayoutInflater.from(mActivity).inflate(resId, null);
		setContentView(mView);
	}
	
	@Override
	public void show() {// 可在任意线程
		// 防止View not attached to window manager
		if (!mActivity.isFinishing()) {
			BaseDialog.super.show();
		}
	}
	
	public void setOnClickListener(DialogClickListener listener) {
		mClickListener = listener;
	}
	
	public static class DialogClickListener {
		public void onOK() {
		}
		
		public void onOK(String s) {
		}
		
		public void onOK(String... strArr){
		}
		
		public void onOK(Bundle bundle) {// 带多种参数
		}
		public void onCancel() {
		}
	}

	protected void showShortToast(String msg) {
		ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void showShortToast(int message) {
		String msg = ApplicationUtil.getApplicationContext().getResources().getString(message);
		ToastCompat.makeText(ApplicationUtil.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}
