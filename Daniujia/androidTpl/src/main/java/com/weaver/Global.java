package com.weaver;

import java.io.File;

import com.weaver.utils.SysUtil;

import android.app.Application;

public final class Global {
	public static boolean DEBUG = false;
	private static Application mApp;
	private static File mSdCardDir;
	
	public static void init(boolean debug, Application app, File sdCardDir) {
		DEBUG = debug;
		mApp = app;
		mSdCardDir = sdCardDir;
		SysUtil.init(mApp);
	}

	public static Application app() {
		return mApp;
	}

	public static File getSdcardDir() {
		return mSdCardDir;
	}

}
