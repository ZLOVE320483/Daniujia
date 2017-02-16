package com.xiaojia.daniujia;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

public class FileStorage {
	public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();
	public static final String APP_DIR = Environment.getExternalStorageDirectory() + File.separator + "com.xiaojia.daniujia" + File.separator;
	public static final String APP_DB_DIR = APP_DIR + "db";
	public static final String APP_IMG_DIR = APP_DIR + "img";
	public static final String APP_LOG_DIR = APP_DIR + "logs";
	public static final String APP_VOICE_DIR = APP_DIR + "voice";
	public static final String APP_ATTACH_DIR = APP_DIR + "attach";

	
	public static final String FACE_FILE_NAME_SUFFIX = "_";
	public static final String PHOTO_FILE_NAME_SUFFIX = "_dnj.jpg";
	
	public static final File baseDir = new File(APP_DIR);
	public static final String cacheDir = "cache";
	private static final String apkDir = "apk";
	private static final String avatarDir = "avatar";
	private static final String hideFolderFileName = ".nomedia";
	private static final String chatrecordDir = "chatrecord";
	private static final String histAudioDir = chatrecordDir + "/audio";
	private static final String histImageDir = chatrecordDir + "/image";
	public static final String VOICE_EXTENSION = ".amr";
	public static final String VOICE_PREFIX= "voice_";

	public static final String buildApkPath(String fileName) {
		return apkDir + "/" + fileName;
	}
	
	public static final String buildCachePath(String fileName) {
		return cacheDir + "/" + fileName;
	}
	
	public static final String buildAvatarPath(String fileName) {
		return avatarDir + "/" + fileName;
	}

	public static final String buildChatAudioPath(String fileName) {
		return histAudioDir + "/" + fileName;
	}

	public static final String buildChatImgPath(String fileName) {
		return histImageDir + "/" + fileName;
	}

	public static File  getFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		return new File(FileStorage.APP_IMG_DIR, path);
	}

	public static void init() {
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}

		File f = new File(baseDir, apkDir);
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(baseDir, avatarDir);
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(baseDir, hideFolderFileName);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		f = new File(baseDir, cacheDir);
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(baseDir, chatrecordDir);
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(baseDir, histAudioDir);
		if (!f.exists()) {
			f.mkdir();
		}
		f = new File(baseDir, histImageDir);
		if (!f.exists()) {
			f.mkdir();
		}
	}
	
}
