package com.weaver.utils;

import android.util.DisplayMetrics;

public class PixUtil {
	private static DisplayMetrics sDisplayMetrics = SysUtil.getDisplayMetrics();

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(float dpValue) {
		final float scale = sDisplayMetrics.density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(float pxValue) {
		final float scale = sDisplayMetrics.density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int px2sp(float pxValue) { 
        final float fontScale = sDisplayMetrics.scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     */ 
    public static int sp2px(float spValue) { 
        final float fontScale = sDisplayMetrics.scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
    
}
