package com.weaver.utils;

import com.weaver.Global;
import android.util.Log;

public class LogUtil {
	public static void v(String tag, String msg) {
		if (Global.DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, Throwable e) {
		if (Global.DEBUG) {
			Log.v(tag, "", e);
		}
	}

	public static void d(String tag, String msg) {
		if (Global.DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, Throwable e) {
		if (Global.DEBUG) {
			Log.d(tag, "", e);
		}
	}

	public static void i(String tag, String msg) {
		if (Global.DEBUG) {
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (Global.DEBUG) {
			Log.w(tag, msg);
		}
	}

	public static void i(String tag, Throwable e) {
		if (Global.DEBUG) {
			Log.i(tag, "", e);
		}
	}

	public static void w(String tag, Throwable e) {
		if (Global.DEBUG) {
			Log.w(tag, "", e);
		}
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void e(String tag, Throwable e) {
		Log.e(tag, "", e);
	}

	public static void e(String tag, String msg, Throwable e) {
		Log.e(tag, msg, e);
	}

}
