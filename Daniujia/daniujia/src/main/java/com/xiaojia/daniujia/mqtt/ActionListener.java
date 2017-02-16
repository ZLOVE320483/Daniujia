/*
 * Licensed Materials - Property of IBM
 *
 * 5747-SM3
 *
 * (C) Copyright IBM Corp. 1999, 2012 All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 *
 */
package com.xiaojia.daniujia.mqtt;

import android.content.Context;

import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.managers.PollManager;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * This Class handles receiving information from the
 * {@link } and updating the {@link Connection}
 * associated with the action
 */
public class ActionListener implements IMqttActionListener {

	/**
	 * Actions that can be performed Asynchronously <strong>and</strong>
	 * associated with a {@link ActionListener} object
	 *
	 */
	public enum Action {
		/** Connect Action **/
		CONNECT,
		/** Disconnect Action **/
		DISCONNECT,
		/** Subscribe Action **/
		SUBSCRIBE,
		/** Publish Action **/
		PUBLISH
	}

	/**
	 * The {@link Action} that is associated with this instance of
	 * <code>ActionListener</code>
	 **/
	private Action action;
	/** The arguments passed to be used for formatting strings **/
	private String[] additionalArgs;

	private MqttMsgCallbackHandler handler;

	/**
	 * Creates a generic action listener for actions performed form any activity
	 *
	 * @param context
	 *            The application context
	 * @param action
	 *            The action that is being performed
	 * @param additionalArgs
	 *            Used for as arguments for string formating
	 */
	public ActionListener(Context context, Action action, MqttMsgCallbackHandler handler,
						  String... additionalArgs) {
		this.action = action;
		this.handler = handler;
		this.additionalArgs = additionalArgs;
	}

	/**
	 * The action associated with this listener has been successful.
	 *
	 * @param asyncActionToken
	 *            This argument is not used
	 */
	@Override
	public void onSuccess(IMqttToken asyncActionToken) {

		switch (action) {
			case CONNECT:
				connect();
				break;
			case DISCONNECT:
				disconnect();
				break;
			case SUBSCRIBE:
				subscribe();
				break;
			case PUBLISH:
				publish();
				break;
		}

	}

	/**
	 * A publish action has been successfully completed, update connection
	 * object associated with the client this action belongs to, then notify the
	 * user of success
	 */
	private void publish() {
		// Connection c =
		// Connections.getInstance(context).getConnection(clientHandle);
		// String actionTaken = context.getString(R.string.toast_pub_success,
		// (Object[]) additionalArgs);
		// c.addAction(actionTaken);
		// Notify.toast(context, "publish succeed", Toast.LENGTH_SHORT);
		String time = additionalArgs[0];
		if (time != null) {
			if (handler != null) {
				handler.onSendChatMsg(true, time);
			}
		}
	}

	/**
	 * A subscribe action has been successfully completed, update the connection
	 * object associated with the client this action belongs to and then notify
	 * the user of success
	 */
	private void subscribe() {
		// Connection c =
		// Connections.getInstance(context).getConnection(clientHandle);
		// String actionTaken = context.getString(R.string.toast_sub_success,
		// (Object[]) additionalArgs);
		// c.addAction(actionTaken);
		// Notify.toast(context, "subscribe success", Toast.LENGTH_SHORT);
		if (handler != null) {
			handler.onSubscribe(true);
		}
	}

	/**
	 * A disconnection action has been successfully completed, update the
	 * connection object associated with the client this action belongs to and
	 * then notify the user of success.
	 */
	private void disconnect() {
	}

	/**
	 * A connection action has been successfully completed, update the
	 * connection object associated with the client this action belongs to and
	 * then notify the user of success.
	 */
	private void connect() {
		System.out.println("connect" + "success");

		// 连接成功...
		MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_MQTT_CONNECTED);

		UserInfo userInfo = SysUtil.getUsrInfo();
		if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
//			MqttUtils.subscribe(UserModule.Instance.getUserInfo().getUsername());
			MqttUtils.subscribe("private-" + userInfo.getUsername());
			MqttUtils.subscribe("public-global");
		}

		if (handler != null) {
			handler.onConnect(true);
		}
		SysUtil.savePref(PrefKeyConst.PREF_mqttLoginError, false);
		PollManager.Instance.poolCallback.onSuccess();
	}

	/**
	 * The action associated with the object was a failure
	 *
	 * @param token
	 *            This argument is not used
	 * @param exception
	 *            The exception which indicates why the action failed
	 */
	@Override
	public void onFailure(IMqttToken token, Throwable exception) {
		LogUtil.d("zptest","action_onFailure: "+ action);
		switch (action) {
			case CONNECT:
				connect(exception);
				break;
			case DISCONNECT:
				disconnect(exception);
				break;
			case SUBSCRIBE:
				subscribe(exception);
				break;
			case PUBLISH:
				publish(exception);
				break;
		}

	}

	/**
	 * A publish action was unsuccessful, notify user and update client history
	 *
	 * @param exception
	 *            This argument is not used
	 */
	private void publish(Throwable exception) {
		// Connection c =
		// Connections.getInstance(context).getConnection(clientHandle);
		// String action = context.getString(R.string.toast_pub_failed,
		// (Object[]) additionalArgs);
		// c.addAction(action);
		// Notify.toast(context, action, Toast.LENGTH_SHORT);
		System.out.println("exception---------->");
		if (exception != null) {
			exception.printStackTrace();
		}
		String time = additionalArgs[0];
		if (handler != null) {
			handler.onSendChatMsg(false, time);
		}
	}

	/**
	 * A subscribe action was unsuccessful, notify user and update client
	 * history
	 *
	 * @param exception
	 *            This argument is not used
	 */
	private void subscribe(Throwable exception) {
		if (exception != null) {
			exception.printStackTrace();
		}
		if (handler != null) {
			handler.onSubscribe(false);
		}
	}

	/**
	 * A disconnect action was unsuccessful, notify user and update client
	 * history
	 *
	 * @param exception
	 *            This argument is not used
	 */
	private void disconnect(Throwable exception) {
	}

	/**
	 * A connect action was unsuccessful, notify the user and update client
	 * history
	 *
	 * @param exception
	 *            This argument is not used
	 */
	private void connect(Throwable exception) {
		System.out.println("connect " + "fail");
		MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_MQTT_UNCONNECT);
		LogUtil.e("ZLOVE", "lient failed to connect\n" + exception.toString());
		MqttUtils.handleConnectionLost();
		if (handler != null) {
			handler.onConnect(false);
		}
		if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_mqttLoginError)) {
			PollManager.Instance.poolCallback.onFail();
		}
	}

}