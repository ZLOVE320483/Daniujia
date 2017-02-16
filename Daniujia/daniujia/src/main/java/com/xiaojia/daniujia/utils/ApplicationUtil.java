package com.xiaojia.daniujia.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xiaojia.daniujia.base.App;


/**
 * Created by Administrator on 2016/4/7.
 */
public class ApplicationUtil {

    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();

    public static Context getApplicationContext() {
        return App.get();
    }

    public static ContentResolver getContentResolver() {
        return getApplicationContext().getContentResolver();
    }

    public static boolean hasSdCard() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(ApplicationUtil.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return TextUtils.isEmpty(deviceId) ? "" : deviceId;
    }

    public static String getVersionCode()//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            return String.valueOf(pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getVersionName()
    {
        try {
            PackageInfo pi = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
