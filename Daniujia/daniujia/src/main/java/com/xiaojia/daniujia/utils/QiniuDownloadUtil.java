package com.xiaojia.daniujia.utils;

import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.managers.ThreadWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class QiniuDownloadUtil {
	/**
	 *
	 * @param link
	 *            the path to store the file
	 */

	public static  void downloadPic(String savePath, final String link,final DownloadCallback callback){
		File imageDir = new File(FileStorage.APP_IMG_DIR);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}
		final File photoFile = new File(imageDir, savePath);
		download(photoFile,link,callback);
	}

	public static void downloadVoice(String savePath, final String link, final DownloadCallback callback) {
		File imageDir = new File(FileStorage.APP_VOICE_DIR);
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}
		final File voiceFile = new File(imageDir, savePath);
		download(voiceFile,link,callback);
	}

	public static void download(final File file , final String link,final DownloadCallback callback) {
		ThreadWorker.execute(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(link);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setReadTimeout(40000);
					con.setConnectTimeout(50000);
					if(con.getResponseCode() == 200){
						InputStream fi = con.getInputStream();
						FileOutputStream fos = new FileOutputStream(file);
						byte by[] = new byte[1024];
						int len;
						int total = 0;
						while((len = fi.read(by))!=-1){
							fos.write(by, 0, len);
							total += len;
							callback.onProgress(con.getContentLength(),total);
						}
						fos.close();
						callback.downloadComplete(file.getPath());
					}
				} catch (Exception e) {
					e.printStackTrace();
					callback.onFail(e);
				}
			}
		});
	}

	public interface DownloadCallback {
		void downloadComplete(String path);
		void onProgress(int max,int progress);
		void onFail(Exception e);
	}
}
