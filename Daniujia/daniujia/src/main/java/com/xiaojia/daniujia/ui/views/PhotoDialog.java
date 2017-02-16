package com.xiaojia.daniujia.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.utils.ScreenUtils;

public class PhotoDialog {
	
	public static void showDialog(Dialog dialog) {
		if(dialog != null){
			if(!dialog.isShowing()){
				dialog.show();
			}
		}
	}

	public static void dismissDialog(Dialog dialog){
		if(dialog != null){
			if(dialog.isShowing()){
				dialog.dismiss();
			}
		}
	}
	
	public static Dialog makePopup(Activity activity, View dialogContentView) {
		Dialog dialog = new Dialog(activity, R.style.popupDialog);

		Window window = dialog.getWindow();
		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		windowParams.x = 0;
		windowParams.y = ScreenUtils.getDisplayMetrics(activity).heightPixels;
		window.setAttributes(windowParams);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(dialogContentView);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		return dialog;
	}
}
