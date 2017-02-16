package com.weaver.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.weaver.Global;

import android.annotation.SuppressLint;

/**
 * 该程序用来在sd卡上logs/flog/目录下自动生成log, 最多10条,超过会自动删除以前的.
 */
@SuppressLint("SimpleDateFormat")
public class FLog {
	private static final String DEF_TAG = "default";
	private static final int MAX_LOG_NUM = 10;
	private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private final static File LOG_DIR;

	static {
		File f = null;
		File sdPath = Global.getSdcardDir();
		if (sdPath != null) {
			try {
				f = new File(sdPath, "logs/logf");
				if (!f.exists()) {
					f.mkdirs();
				} else {
					String[] subFile = f.list();
					if (subFile != null && subFile.length > MAX_LOG_NUM) {
						deleteFile(f);
						f.mkdir();
					}
				}
			} catch (Throwable e) {
				f = null;
			}
		}
		LOG_DIR = f;
	}

	public static void d(String msg) {
		d(DEF_TAG, msg);
	}

	public static void d(Throwable e) {
		d(DEF_TAG, e);
	}

	public static void d(String tag, Throwable e) {
		d(tag, CommUtil.getThrowableDetailMsg(e));
	}

	public static void d(String tag, final String msg) {
		if (LOG_DIR == null) {
			return;
		}
		write(tag, msg);
	}

	private synchronized static void write(String tag, String msg) {
		PrintWriter pw = null;
		try {
			File f = new File(LOG_DIR, tag + "_" + DATE_TIME_FORMAT.format(new Date()) + ".txt");
			pw = new PrintWriter(new FileWriter(f, true));
			pw.println("【" + TIME_FORMAT.format(new Date()) + "】" + msg);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			pw = null;
		}
	}
	
	private static void deleteFile(File f) {
		if (f.isFile()) {
			f.delete();
			return;
		}
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File subF : files) {
				deleteFile(subF);
			}
			f.delete();
		}
	}

}
