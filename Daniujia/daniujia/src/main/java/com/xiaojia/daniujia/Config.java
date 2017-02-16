package com.xiaojia.daniujia;

import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.utils.ApplicationUtil;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Config {

	public static final boolean DEBUG;
	public static final String SDCARD_DIR;
	public static final String WEB_API_SERVER;
	public static final String WEB_API_SERVER_V2;
	public static final String WEB_API_SERVER_V3;
	public static final String WEB_URL_QINIU_RES;
	public static final String WEB_URL_QINIU_RES_V2;
	public static final String MQTT_SERVER_HOST;
	public static final String MQTT_SERVER_PORT;
	public static final String WXPAY_APP_ID;
	public static final String KEY_CYPHER_SEED;
	public static final String PREFIX_SUBSCRIBE_CONSULT;
	

	static {
		Properties prop = new Properties();
		try {
			prop.load(App.get().getResources().openRawResource(R.raw.conf));
		} catch (IOException e) {
			e.printStackTrace();
		}
		DEBUG = Boolean.parseBoolean(getProp(prop, "system.debug"));
		MQTT_SERVER_HOST = getProp(prop, "mqtt.server.host");
		MQTT_SERVER_PORT = getProp(prop, "mqtt.server.port");
		SDCARD_DIR = getProp(prop, "sdcardDir.name");
		WEB_API_SERVER = getProp(prop, "web.url.api");
		WEB_API_SERVER_V2 = getProp(prop, "web.url.api_v2");
		WEB_API_SERVER_V3 = getProp(prop, "web.url.api_v3");
		WEB_URL_QINIU_RES = getProp(prop, "web.url.qiniu.res");
		WEB_URL_QINIU_RES_V2 = getProp(prop, "web.url.qiniu.res_v2");
		WXPAY_APP_ID = getProp(prop, "web.wxpay.appId");
		KEY_CYPHER_SEED = getProp(prop, "key.cypherSeed");
		PREFIX_SUBSCRIBE_CONSULT = "dnjsubcategory/";
	}

	private static String getProp(Properties prop, String key) {
		String value = null;
		if (DEBUG) {
			value = prop.getProperty(key + ".debug");
		}
		if (value == null) {
			value = prop.getProperty(key);
		}
		return value;
	}
}
