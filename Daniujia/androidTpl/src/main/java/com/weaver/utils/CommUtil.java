package com.weaver.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.weaver.Global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommUtil {

	public static boolean isValidChar(char c) {
		if (c > 31 && c < 127 || c >= '\u4e00' && c <= '\u9fa5') {
			return true;
		}
		return false;
	}

	public static String getThrowableDetailMsg(Throwable e) {
		Writer sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String result = sw.toString();
		pw.close();
		return result;
	}

	public static boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) Global.app()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

}
