package com.xiaojia.daniujia.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.xiaojia.daniujia.utils.JsonResult.Err;

import java.util.List;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class JsonUtil {

	public static <T> JsonResult<T> getCorrectJsonResult(String rawJson, Class<T> clz) throws Exception {
		if (rawJson == null) {
			throw new Exception(new JSONException("rawJson is NULL"));
		} else {
			try {
				Object jobj = JSON.parse(rawJson);// 验证json格式是否正确
				if (jobj == null || jobj instanceof JSONArray) {// =>是json// array格式：[...]，不作解析
					return null;
				}
				JsonResult<T> result = new JsonResult<T>(rawJson, clz);
				Err err = result.getErr();
				if (err != null) {
					throw new Exception(err.errMsg);
				}
				return result;
			} catch (JSONException e) {
				throw new Exception(e);
			}
		}
	}

	public static <T> JsonResult<T> getFailJsonResult(String rawJson, Class<T> clz) throws Exception {
		if (rawJson == null) {
			throw new Exception(new JSONException("rawJson is NULL"));
		} else {
			try {
				Object jobj = JSON.parse(rawJson);// 验证json格式是否正确
				if (jobj == null || jobj instanceof JSONArray) {// =>是json// array格式：[...]，不作解析
					return null;
				}
				JsonResult<T> result = new JsonResult<>(rawJson, clz);
				return result;
			} catch (JSONException e) {
				throw new Exception(e);
			}
		}
	}

	public static JsonResult<String> getCorrectJsonResult(String rawJson) throws Exception {
		if (rawJson == null || "null".equals(rawJson)) {
			throw new Exception(new JSONException("rawJson is NULL"));
		} else {
			try {
				Object jobj = JSON.parse(rawJson);// =>是json obj格式：{...}
				if (jobj == null || jobj instanceof JSONArray) {// =>是json
																// array格式：[...]，不作解析
					return null;
				}
				JsonResult<String> result = new JsonResult<String>(rawJson);
				Err err = result.getErr();
				if (err != null) {
					throw new Exception(err.errMsg);
				}
				return result;
			} catch (JSONException e) {
				throw new Exception(e);
			}
		}
	}

	public static <T> List<T> json2List(String json, String key, Class<T> clz) {
		JSONObject jOjb = JSON.parseObject(json);
		JSONArray jArr = jOjb.getJSONArray(key);
		List<T> list = JSONArray.parseArray(jArr.toJSONString(), clz);
		return list;
	}

	public static <T> List<T> json2List(String json, Class<T> clz) {
		JSONArray jArr = JSON.parseArray(json);
		List<T> list = JSONArray.parseArray(jArr.toJSONString(), clz);
		return list;
	}

	public static <T> String list2Json(List<T> list, String key) {
		JSONObject jObj = new JSONObject();
		jObj.put(key, list);
		return jObj.toJSONString();
	}

	public static <T> String list2Json(List<T> list) {
		return JSON.toJSONString(list);
	}

	public static org.json.JSONObject getShareInfo(String platform, String err) {
		String type = "";
		if (platform.equals(SinaWeibo.NAME)) {
			type = "SinaWeibo";
		} else if (platform.equals(Wechat.NAME)) {
			type = "shareMsg";
		} else if (platform.equals(WechatMoments.NAME)) {
			type = "shareTimeline ";
		} else if (platform.equals(QQ.NAME)) {
			type = "shareQQ";
		}
		org.json.JSONObject object = new org.json.JSONObject();
		try {
			object.put("type", type);
			object.put("err", err);
		} catch (org.json.JSONException e) {
			LogUtil.d("ZLOVE", "shareSuccess---JSONException---" + e.toString());
		}
		return object;
	}

}
