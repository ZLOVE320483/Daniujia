package com.weaver.utils;

import com.weaver.Global;

import android.annotation.SuppressLint;
import android.widget.Toast;

public class ToastUtil {
	private static Toast sToast;

	public static void makeToast(int resId) {
		getToast(Global.app().getString(resId), Toast.LENGTH_SHORT).show();
	}

	public static void makeToast(CharSequence msg) {
		getToast(msg, Toast.LENGTH_SHORT).show();
	}

	public static void makeLongToast(int resId) {
		getToast(Global.app().getString(resId), Toast.LENGTH_LONG).show();
	}

	public static void makeLongToast(CharSequence msg) {
		getToast(msg, Toast.LENGTH_LONG).show();
	}

	@SuppressLint("ShowToast")
	private static Toast getToast(CharSequence msg, int duration) {
		if (sToast == null) {
			sToast = Toast.makeText(Global.app(), msg, duration);
		} else {
			sToast.setText(msg);
		}
		return Toast.makeText(Global.app(), msg, duration);
	}

}
