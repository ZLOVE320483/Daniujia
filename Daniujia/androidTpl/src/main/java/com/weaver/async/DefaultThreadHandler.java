package com.weaver.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

public class DefaultThreadHandler {
	private static Handler handler = null;

	public static void post(Runnable r) {
		post(r, 0);
	}
	
	public static void postDelayed(Runnable r, long delayMillis) {
		post(r, delayMillis);
	}

	private static void post(final Runnable r, final long delay) {
		if (handler == null) {
			HandlerThread thread = new HandlerThread("Default Thread", Process.THREAD_PRIORITY_BACKGROUND);
			thread.start();
			handler = new Handler(thread.getLooper());
		}
		handler.postDelayed(r, delay);
	}

	public static void remove(Runnable r) {
		if (handler != null) {
			handler.removeCallbacks(r);
		}
	}
}
