package com.xiaojia.daniujia.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
    public static boolean isNetAvailable(Context context) {
        boolean available = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            NetworkInfo activeNetworkInfo = conManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                available = activeNetworkInfo.isAvailable();
            }
        }
        return available;
    }

    public static String getNetworkLinkWay(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING) {
            return "gprs";
        }
        //判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
			return "wifi";
        }
        return "not connect";
    }
}
