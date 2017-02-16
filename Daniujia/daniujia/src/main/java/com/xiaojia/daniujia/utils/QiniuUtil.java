package com.xiaojia.daniujia.utils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.managers.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class QiniuUtil {
	private static UploadManager uploadManager = new UploadManager();

	public static void upload(File data, String key) throws IOException {
		String url = Config.WEB_API_SERVER_V3 + "/user/qiniutoken/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
		String qnToken = OkHttpClientManager.getInstance(null).getSyncWithToken(url);
		if (qnToken != null) {
			uploadManager.put(data, key, getQnToken(qnToken), new UpCompletionHandler() {
				@Override
				public void complete(String key, ResponseInfo info, JSONObject res) {
					if (info.isOK()) {// 上传成功
					} else if ("no token".equals(info.error)) {// token为空
					} else {// 其他异常
					}
				}
			}, null);
		}
	}

	/**
	 * 
	 * @param data
	 *            :上传的文件
	 * @param key
	 *            ：指定上传后到七牛服务上的文件名
	 * @param handler
	 *            :上传完成后的回调
	 * @throws IOException
	 */
	public static void upload(File data, String key, UpCompletionHandler handler) throws IOException {
		String url = Config.WEB_API_SERVER_V3 + "/user/qiniutoken/new/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
		String qnToken = OkHttpClientManager.getInstance(null).getSyncWithToken(url);
		if (qnToken != null) {
			uploadManager.put(data, key, getQnToken(qnToken), handler, null);
		}
	}

	public static void upload(byte[] data, String key, UpCompletionHandler handler) throws IOException {
		String url = Config.WEB_API_SERVER_V3 + "/user/qiniutoken/new/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
		String qnToken = OkHttpClientManager.getInstance(null).getSyncWithToken(url);
		
		if(qnToken != null){
			uploadManager.put(data, key, getQnToken(qnToken), handler, null);
		}
	}
	
	private static String getQnToken(String json){
		String token = null;
		try {
			JSONObject jsonObj = new JSONObject(json);
			token = jsonObj.getString("uptoken");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return token;
	}

	public static String getResUrl(String fileName) {
		return Config.WEB_URL_QINIU_RES_V2 + fileName;
	}

	public static void upload(File data, String key, UpCompletionHandler handler, UploadOptions options) throws IOException {
		String url = Config.WEB_API_SERVER_V3 + "/user/qiniutoken/new/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
		String qnToken = OkHttpClientManager.getInstance(null).getSyncWithToken(url);
		if (qnToken != null) {
			uploadManager.put(data, key, getQnToken(qnToken), handler, options);
		}
	}

	public static void uploadVoiceAmrToMp3(File data, String key, UpCompletionHandler handler, UploadOptions options) throws IOException {
		String url = Config.WEB_API_SERVER_V3 + "/qiniu/token?type=audioMp3";
		String qnToken = OkHttpClientManager.getInstance(null).getSyncWithToken(url);
		LogUtil.d("ZLOVE", "qnToken---" + qnToken);
		if (qnToken != null) {
			uploadManager.put(data, key, getQnToken(qnToken), handler, options);
		}
	}
}
