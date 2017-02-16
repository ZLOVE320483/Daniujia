package com.xiaojia.daniujia.msg;

import android.os.Message;

import com.xiaojia.daniujia.msg.annotation.OnMsg;

public class MsgCallbackImpl implements OnMsgCallback {

	int msgCmd;
	OnMsg onMsg;
	OnMsgCallback callback;

	public MsgCallbackImpl(int msgCmd, OnMsg onMsg, OnMsgCallback callback) {
		super();
		this.msgCmd = msgCmd;
		this.onMsg = onMsg;
		this.callback = callback;
	}

	public boolean isRunInUI() {
		return onMsg.ui();
	}

	public boolean handleMsg(Message msg) {

		return callback.handleMsg(msg);

	}
}