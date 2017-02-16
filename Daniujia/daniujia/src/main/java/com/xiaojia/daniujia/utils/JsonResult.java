package com.xiaojia.daniujia.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonResult<T> {
	private JSONObject rawJson;
	private Class<T> clz;

	public JsonResult(String json) {
		JSONObject jObj = JSON.parseObject(json);
		this.rawJson = jObj;
	}

	public JsonResult(String json, Class<T> clz) {
		JSONObject jObj = JSON.parseObject(json);
		this.rawJson = jObj;
		this.clz = clz;
	}

	public JSONObject getRawJson() {
		return rawJson;
	}

	public List<T> getArrayData(String key) {
		if (clz == null) {
			return null;
		} else {
			JSONArray jArr = rawJson.getJSONArray(key);
			return (List<T>) JSONArray.parseArray(jArr.toJSONString(), clz);
		}
	}

	@SuppressWarnings("unchecked")
	public T getData() {
		if (clz == null) {
			return (T) rawJson.values().iterator().next();
		} else {
			return JSON.parseObject(rawJson.toJSONString(), clz);
		}
	}

	public String getData(String key) {
		return rawJson.getString(key);
	}

	public Map<String, String> getDataAsMap() {
		Set<Entry<String, Object>> entries = rawJson.entrySet();
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<String, Object> entry : entries) {
			map.put(entry.getKey(), entry.getValue().toString());
		}
		return map;
	}

	public Err getErr() {
		if (rawJson.containsKey("errcode") && rawJson.containsKey("errmsg")) {
			return new Err(rawJson.getIntValue("errcode"), rawJson.getString("errmsg"));
		}
		return null;
	}

	public class Err {
		public int errCode;
		public String errMsg;

		public Err(int c, String m) {
			errCode = c;
			errMsg = m;
		}
	}
}
