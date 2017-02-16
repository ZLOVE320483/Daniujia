package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.SysUtil;

public class BaseBottomDialog extends Dialog {
	protected Activity mActivity;
	protected View mView;
	protected OnDialogClickListener mClickListener;
	
	public BaseBottomDialog(Activity context, View view) {
		super(context, R.style.MenuDialogStyle);
		mActivity = context;
		mView = view;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mView);
		Window window = getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = SysUtil.getDisplayMetrics().widthPixels;
		lp.width = screenW;
		window.setGravity(Gravity.BOTTOM); // 设置dialog显示的位置
		window.setWindowAnimations(R.style.MenuDialogAnimation); // 添加动画
	}
	
	@Override
	public void show() {// 可在任意线程
		// 防止View not attached to window manager
		if (!mActivity.isFinishing()) {
			BaseBottomDialog.super.show();
		}
	}
	
	public void setOnDialogClickListener(OnDialogClickListener listener) {
		mClickListener = listener;
	}
	
	public static abstract class OnDialogClickListener {
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
	
}
