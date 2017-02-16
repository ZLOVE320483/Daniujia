package com.xiaojia.daniujia.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.util.DisplayMetrics;

import com.xiaojia.daniujia.base.App;


public class ScreenUtils {
	public static int dip2px(float dipValue) {
		final float scale = App.get().getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = App.get().getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int px2sp(float pxValue) { 
        final float fontScale = App.get().getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     */ 
    public static int sp2px(float spValue) { 
        final float fontScale = App.get().getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
	
	public static DisplayMetrics getDisplayMetrics(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	public static boolean isScreenLocked() {
		KeyguardManager mKeyguardManager = (KeyguardManager) ApplicationUtil.getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
		boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
		return flag;
	}

	public static int getStatusBarHeight(){
		int statusBarHeight = -1;
		//获取status_bar_height资源的ID
		int resourceId = App.get().getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight = App.get().getResources().getDimensionPixelSize(resourceId);
		}
		LogUtil.d("zptest","status bar: " + statusBarHeight);
		return statusBarHeight;
	}
}
