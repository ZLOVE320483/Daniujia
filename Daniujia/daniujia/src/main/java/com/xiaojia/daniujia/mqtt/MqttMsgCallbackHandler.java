package com.xiaojia.daniujia.mqtt;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.SysUtil;

public class MqttMsgCallbackHandler {
	private MqttLoginCallback loginCallback;
	private MqttChatCallback chatCallback;
	private MqttRealTimeChatCallback realtimeChatCallback;
	private MqttConnLostCallback connLostCallback;

	public MqttMsgCallbackHandler() {
		loginCallback = null;
		chatCallback = null;
		realtimeChatCallback = null;
		connLostCallback = null;
	}

	void setLoginCallback(MqttLoginCallback loginCallback) {
		this.loginCallback = loginCallback;
	}

	void setChatCallback(MqttChatCallback chatCallback) {
		this.chatCallback = chatCallback;
	}

	void setRealTimeChatCallback(MqttRealTimeChatCallback realtimeChatCallback) {
		this.realtimeChatCallback = realtimeChatCallback;
	}

	void setConnLostCallback(MqttConnLostCallback connLostCallback) {
		this.connLostCallback = connLostCallback;
	}

	public void onConnect(boolean connected) {
		if (loginCallback != null) {
			loginCallback.onConnect(connected);
		}
	}

	public void onSubscribe(boolean isSuccess) {
		if (loginCallback != null) {
			loginCallback.onSubscirbe(isSuccess);
		}
	}

	public void onSendChatMsg(boolean isSuccess, final String args) {
		if (chatCallback != null) {
			if (!TextUtils.isEmpty(args)) {
				chatCallback.onSend(isSuccess, args);
			}
		}
	}

	public void onRecvHistoryMsg(String msg) {
		if (chatCallback != null) {
			chatCallback.onDragHistoryMsg(msg);
		}
	}


	public void onRecvChatMsg(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			MessageEntity dataItem = JSON.parseObject(msg, MessageEntity.class);
			if (chatCallback != null) {
				chatCallback.onRecvChat(dataItem);
			} else {
				if (!dataItem.getFrom().equals(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME))) {
					if (dataItem.getMtype() == IMMsgFlag.MSG_CHAT) {
						MediaPlayer player = MediaPlayer.create(ApplicationUtil.getApplicationContext(), R.raw.notify);
						player.setVolume(1.0f, 1.0f);
						player.start();
					}
				}
			}
		}
	}

	public void onRecvChatConfirmMsg(int chatId, long time) {
		if (chatCallback != null) {
			chatCallback.onRecvChatConfirm(chatId, time);
		}
	}

	public void onCallReq(String msg) {
		if (msg != null) {
			if (realtimeChatCallback != null) {
				realtimeChatCallback.onCallReq(msg);
			}
		}
	}

	public void onCallStart(String msg) {
		if (msg != null) {
			if (realtimeChatCallback != null) {
				realtimeChatCallback.onCallStart(msg);
			}
		}
	}

	public void onCallEnd(String msg) {
		if (msg != null) {
			if (realtimeChatCallback != null) {
				realtimeChatCallback.onCallEnd(msg);
			}
		}
	}

	public void onConnLost() {
		if (connLostCallback != null) {
			connLostCallback.onConnLost();
		}
	}

	public void destroy() {
		loginCallback = null;
		chatCallback = null;
		realtimeChatCallback = null;
		connLostCallback = null;
	}

	public interface MqttLoginCallback {
		void onConnect(boolean connected);

		void onSubscirbe(boolean isSuccess);
	}

	public interface MqttChatCallback {
		void onSend(boolean isSuccess, String args);

		void onRecvChat(MessageEntity dataItem);

		void onRecvChatConfirm(int chatId, long time);

		void onDragHistoryMsg(String strMsg);
	}

	public interface MqttRealTimeChatCallback {
		void onCallReq(String msg);

		void onCallStart(String msg);

		void onCallEnd(String msg);
	}

	public interface MqttConnLostCallback {
		void onConnLost();
	}

}
