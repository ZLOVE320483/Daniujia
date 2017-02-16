package com.weaver.async;

import android.os.Handler;
import android.os.Looper;

import com.weaver.utils.ToastUtil;

public class MainThreadHandler {
	private static Handler sMainLooperHandler;

	private MainThreadHandler() {
	}

	private static void init() {
		if (sMainLooperHandler == null) {
			sMainLooperHandler = new Handler(Looper.getMainLooper());
		}
	}

	public static void post(Runnable r) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			r.run();
			return;
		}
		init();
		sMainLooperHandler.post(r);
	}

	public static void postDelayed(Runnable r, long delayed) {
		init();
		sMainLooperHandler.postDelayed(r, delayed);
	}
	
	public static void remove(Runnable r) {
		sMainLooperHandler.removeCallbacks(r);
	}

	public static void makeToast(final String msg) {
		init();
		sMainLooperHandler.post(new Runnable() {

			@Override
			public void run() {
				ToastUtil.makeToast(msg);
			}
		});
	}
	
	public static void makeToast(final int res) {
		init();
		sMainLooperHandler.post(new Runnable() {

			@Override
			public void run() {
				ToastUtil.makeToast(res);
			}
		});
	}
	
	public static void makeLongToast(final String msg) {
		init();
		sMainLooperHandler.post(new Runnable() {

			@Override
			public void run() {
				ToastUtil.makeLongToast(msg);
			}
		});
	}
	
	public static void makeLongToast(final int res) {
		init();
		sMainLooperHandler.post(new Runnable() {

			@Override
			public void run() {
				ToastUtil.makeLongToast(res);
			}
		});
	}
	
}
