package com.xiaojia.daniujia.managers;

import android.content.Context;

import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.NetUtils;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * single instance
 */
public enum PollManager {
	Instance;

	private final int MAX_RECONNECT_TIMES = Integer.MAX_VALUE;
	private final int INTERTVAL_RATE = 5000;
	private int currentConnectTimes = 0;
	private int currentConnectInterval = 0;
	private Context context = ApplicationUtil.getApplicationContext();

	public void init() {
		currentConnectTimes = 0;
		currentConnectInterval = 0;
		reconnect();
	}

	public void reconnect() {
		if (NetUtils.isNetAvailable(context) && (currentConnectTimes < MAX_RECONNECT_TIMES)
				&& SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN) && !MqttUtils.isConnected()) {
			App.get().postDelayed(connectRunnable, currentConnectInterval);
		}
	}

	Runnable connectRunnable = new Runnable() {
		@Override
		public void run() {
			currentConnectTimes += 1;
			if (currentConnectTimes < 10) {
				currentConnectInterval += INTERTVAL_RATE;
			}
			MqttUtils.reconnect();
		}
	};

	public PoolCallback poolCallback = new PoolCallback() {

		@Override
		public void onSuccess() {
			if (currentConnectTimes > 0) {
				MqttUtils.subscribe("private-" + SysUtil.getPref(PrefKeyConst.PREF_USER_NAME));
			}
			currentConnectTimes = 0;
			currentConnectInterval = 0;
		}

		@Override
		public void onFail() {
			reconnect();
		}
	};

	public interface PoolCallback {
		void onSuccess();

		void onFail();
	}

}
