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


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.alibaba.fastjson.JSON;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseInsertExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseUpdateExecutor;
import com.xiaojia.daniujia.domain.resp.DispatchCustomerResponseVo;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.domain.resp.MessageResponseErrorVo;
import com.xiaojia.daniujia.domain.resp.PullConversationsVo;
import com.xiaojia.daniujia.domain.resp.ReceiveMsgVo;
import com.xiaojia.daniujia.managers.DnjNotificationManager;
import com.xiaojia.daniujia.managers.PollManager;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.msg.McMsg;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SpecificJsonUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.UIUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles call backs from the MQTT Client
 */
public class MqttCallbackHandler implements MqttCallback {
    /**
     * {@link Context} for the application used to format and import external
     * strings
     **/
    private Context context;

    private MqttMsgCallbackHandler handler;

//	private static long time_stamp;

    /**
     * Creates an <code>MqttCallbackHandler</code> object
     *
     * @param context The application's context
     */
    public MqttCallbackHandler(Context context, MqttMsgCallbackHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    /**
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
     */
    @Override
    public void connectionLost(Throwable cause) {
        /**
         *  set the old connect null
         *  	and decide weather to reconnect;
         */
        MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_CONVERSATION_ARRIVED);
        // 未连接
        MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_MQTT_UNCONNECT);

        MqttUtils.handleConnectionLost();
        if (cause != null) {
            Connection c = Connection.getInstance();
            // format string to use a notification text
            Object[] args = new Object[2];
            args[0] = c.getId();
            args[1] = c.getHostName();

            String message = context.getString(R.string.connection_lost, args);

            if (handler != null) {
                handler.onConnLost();
            }
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_isMulityLogin)) {
                if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_mqttLoginError)) {
                    PollManager.Instance.init();
                }
            } else {
                SysUtil.savePref(PrefKeyConst.PREF_isMulityLogin, true);
            }
            LogUtil.e("", message + "\n" + cause.toString());
        }
    }

    /**
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String,
     * org.eclipse.paho.client.mqttv3.MqttMessage) run on UIThread
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String strMsg = new String(message.getPayload());

        LogUtil.d("ZLOVE", "strMsg---" + strMsg + "---" + topic);

        if (MqttServiceConstants.PULL_CONVERSATIONS_RESPONSE.equals(topic)) {
            // 拉取会话列表
            getHistoryConversationList(strMsg);
        } else if (MqttServiceConstants.NEW_CONVERSATION_RECEIVE.equals(topic)) {
            // 产生新会话
            pushNewConversation(strMsg);
        } else if (MqttServiceConstants.MESSAGE_RECEIVE.equals(topic)) {
            // 服务端主动推消息
            pushNewMessage(strMsg);
        } else if (MqttServiceConstants.DELETE_CONVERSATION_RESPONSE.equals(topic)) {
            // 删除某条会话
            //// FIXME: 2016/4/17
        } else if (MqttServiceConstants.MESSAGE_GET_RESPONSE.equals(topic)) {
            // 拉取历史消息列表
            getHistoryMessageList(strMsg);
        } else if (MqttServiceConstants.MESSAGE_SEND_RESPONSE.equals(topic)) {
            // 发送消息后的回包
            pushSendMsgBack(strMsg);
        } else if (MqttServiceConstants.MESSGAE_RESPONSE_ERROR.equals(topic)) {
            // 错误消息
            handleErrorResponse(strMsg);
        } else if (MqttServiceConstants.REMOTE_LOGIN.equals(topic)) {
            // 异地登录的消息
            kickOffline();
        } else if (MqttServiceConstants.MESSAGE_VERSION_CHANGE.equals(topic)) {
            MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_VERSION_CHANGE);
        } else if (MqttServiceConstants.USER_LOGIN_RESPONSE.equals(topic)) {
            //登录认证授权
//            mqttLoginAuth();
        } else if (MqttServiceConstants.USER_LOGOUT_RESPONSE.equals(topic)) {
            // 用户登出回包
            LogUtil.d("ZLOVE", "user logout...");
        } else if (MqttServiceConstants.CUSTOMER_SERVICE_DISPATCH_RESPONSE.equals(topic)) {
            // 请求客服回包
            dispatchCustomerResponse(strMsg);
        }
    }

    private void dispatchCustomerResponse(String strMsg) {
        LogUtil.d("ZLOVE", "dispatch customer response");
        DispatchCustomerResponseVo responseVo = JSON.parseObject(strMsg, DispatchCustomerResponseVo.class);
        if (responseVo == null) {
            return;
        }

    }

    private void mqttLoginAuth() {
        LogUtil.d("ZLOVE", "---mqttLoginAuth---");
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            MqttUtils.subscribe("private-" + SysUtil.getPref(PrefKeyConst.PREF_USER_NAME));
            MqttUtils.subscribe("public-global");
        }
    }

    private void handleErrorResponse(String strMsg) {
        MessageResponseErrorVo bean = JSON.parseObject(strMsg, MessageResponseErrorVo.class);
        if (bean == null) {
            return;
        }
        UIUtil.showShortToast(bean.getError().getMessage());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE, 2);
        List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
        sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_FROM + "=? AND " + DatabaseConstants.MessageColumn.COLUMN_NAME_TO + "=? AND " + DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + "=?",
                new String[]{bean.getRequestParams().getMessage().getFrom(), bean.getRequestParams().getMessage().getTo(), bean.getRequestParams().getMessage().getCode()}, contentValues));
        DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
        executor.execute(executor);
    }

    private void kickOffline() {
        SysUtil.savePref(PrefKeyConst.PREF_isMulityLogin, true);
        SysUtil.clearUserInfo();
        SysUtil.savePref(PrefKeyConst.PREF_ACCOUNT, "");
        Intent it = new Intent();
        it.setClassName("com.xiaojia.daniujia", "com.xiaojia.daniujia.ui.act.TwoButtonDialogActivity");
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.get().startActivity(it);
    }

    /**
     * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Do nothing
    }

    /**
     * 获取历史会话列表
     *
     * @param strMsg
     */
    private void getHistoryConversationList(String strMsg) {
        LogUtil.d("ZLOVE", "---getHistoryConversationList---");

        PullConversationsVo bean = JSON.parseObject(strMsg, PullConversationsVo.class);
        List<PullConversationsVo.DataEntity> data = bean.getData();
        if (!ListUtils.isEmpty(data)) {
            List<BaseDatabaseExecutor.SQLSentence> insertSqlSentences = new ArrayList<>();
            List<BaseDatabaseExecutor.SQLSentence> updateSqlSentences = new ArrayList<>();
            for (PullConversationsVo.DataEntity dataEntity : data) {
                if (!DatabaseManager.getInstance().isExistConversation(dataEntity.getTarget().getUsername())) {
                    if (!dataEntity.getTarget().getUsername().equals("eric")) {
                        dataEntity.setUnreadMessageCount(0);
                    }
                    if (data.size() == 1) {
                        dataEntity.setUnreadMessageCount(1);
                    }
                    insertSqlSentences.add(new BaseDatabaseExecutor.SQLSentence(null, null, dataEntity.getContentValues(dataEntity)));
                } else {
                    dataEntity.setUnreadMessageCount(DatabaseManager.getInstance().getConversationUnReadCount(dataEntity.getTarget().getUsername()));
                    updateSqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=?", new String[]{dataEntity.getTarget().getUsername()}, dataEntity.getContentValues(dataEntity)));
                }
            }
            if (!ListUtils.isEmpty(insertSqlSentences)) {
                DatabaseInsertExecutor executor = new DatabaseInsertExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, insertSqlSentences, DaniujiaUris.URI_CONVERSATION);
                executor.execute(executor);
            }
            if (!ListUtils.isEmpty(updateSqlSentences)) {
                DatabaseUpdateExecutor updateExecutor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, updateSqlSentences, DaniujiaUris.URI_CONVERSATION);
                updateExecutor.execute(updateExecutor);
            }
            McMsg msg = McMsg.newInstance(InsideMsg.NOTIFY_GET_CONVERSATION, data.get(0));
            MsgHelper.getInstance().sendMsg(msg);
        } else {
            MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_CONVERSATION_EMPTY);
        }
        MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_CONVERSATION_ARRIVED);
    }

    /**
     * 服务端主动推送的新会话
     *
     * @param strMsg
     */
    private void pushNewConversation(String strMsg) {

        LogUtil.d("ZLOVE", "---pushNewConversation---");

        PullConversationsVo.DataEntity dataEntity = JSON.parseObject(strMsg, PullConversationsVo.DataEntity.class);
        if (dataEntity != null) {
            List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
            sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(null, null, dataEntity.getContentValues(dataEntity)));
            DatabaseInsertExecutor executor = new DatabaseInsertExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, sqlSentences, DaniujiaUris.URI_CONVERSATION);
            executor.execute(executor);
        }
    }

    /**
     * 服务端主动推来的一条新消息
     *
     * @param strMsg
     */
    private void pushNewMessage(String strMsg) {

        LogUtil.d("ZLOVE", "---pushNewMessage---");

        MessageEntity messageEntity = JSON.parseObject(strMsg, MessageEntity.class);
        if (messageEntity != null) {
            String target;
            if (messageEntity.getType().equals("groupChat")) {
                target = messageEntity.getTo();
            } else {
                if (messageEntity.getFrom().equals(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME))) {
                    target = messageEntity.getTo();
                } else {
                    target = messageEntity.getFrom();
                }
            }
            if (!DatabaseManager.getInstance().isExistConversation(target)) {
                JSONObject object = new JSONObject();
                try {
                    object.put("target", target);
                } catch (JSONException e) {
                    LogUtil.d("ZLOVE", "JSONException---" + e.toString());
                }
                MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, object);
            }

            if (messageEntity.getMtype() == IMMsgFlag.MSG_CHAT) {
                if (!WUtil.isTopActivity() || ScreenUtils.isScreenLocked()) {
                    DnjNotificationManager.getInstance(ApplicationUtil.getApplicationContext()).notify(SpecificJsonUtils.generateNotifyMsg(messageEntity));
                }

                // 普通聊天消息
                messageEntity.setIsDown(0);
                messageEntity.setMsgState(1);
                messageEntity.setIsPlay(0);
                List<BaseDatabaseExecutor.SQLSentence> insertSql = new ArrayList<>();
                insertSql.add(new BaseDatabaseExecutor.SQLSentence(null, null, messageEntity.getContentValues(messageEntity)));
                DatabaseInsertExecutor insertExecutor = new DatabaseInsertExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, insertSql, DaniujiaUris.URI_MESSAGE);
                insertExecutor.execute(insertExecutor);

                if (messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_VOICE) {
                    handlerVoiceMsg(messageEntity);
                } else if (messageEntity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_EVENT) {
                    ContentValues contentValues = new ContentValues();
                    if (messageEntity.getContent().getType().equals("order:paid")) {
                        contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS, "paid");
                    } else {
                        contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_SERVICE_STATUS, messageEntity.getContent().getType());
                    }
                    List<BaseDatabaseExecutor.SQLSentence> updateSql = new ArrayList<>();
                    updateSql.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=? OR " + DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=?", new String[]{messageEntity.getFrom(), messageEntity.getTo()}, contentValues));
                    DatabaseUpdateExecutor updateExecutor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, updateSql, DaniujiaUris.URI_CONVERSATION);
                    updateExecutor.execute(updateExecutor);
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT_TYPE, messageEntity.getContent().getCtype());
                contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT, messageEntity.getContent().getMsg());
                contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_STAMP, messageEntity.getDnjts());
                List<BaseDatabaseExecutor.SQLSentence> updateSql = new ArrayList<>();
//                updateSql.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=? OR " + DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=?", new String[]{messageEntity.getFrom() , messageEntity.getTo()}, contentValues));
                updateSql.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=? ", new String[]{target}, contentValues));
                DatabaseUpdateExecutor updateExecutor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, updateSql, DaniujiaUris.URI_CONVERSATION);
                updateExecutor.execute(updateExecutor);
                if (handler != null) {
                    // to send read confirm in chat surface
                    handler.onRecvChatMsg(strMsg);
                } else {
                    if (!messageEntity.getFrom().equals(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME))) {
                        MediaPlayer player = MediaPlayer.create(ApplicationUtil.getApplicationContext(), R.raw.notify);
                        player.setVolume(1.0f, 1.0f);
                        player.start();
                    }
                }

            } else if (messageEntity.getMtype() == IMMsgFlag.MSG_CHAT_READ_CONFIRM) {
                // 对方确认已读消息
                if (!messageEntity.getFrom().equals(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME))) {
                    LogUtil.d("ZLOVE", "Other Confirm Read...");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_IS_READ, 1);
                    List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
                    sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_TO + "=?", new String[]{messageEntity.getFrom()}, contentValues));
                    DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
                    executor.execute(executor);
                }
            }
        }
    }

    /**
     * 获取历史消息列表
     *
     * @param strMsg
     */
    private void getHistoryMessageList(String strMsg) {
        LogUtil.d("ZLOVE", "---getHistoryMessageList---");
        if (handler != null) {
            handler.onRecvHistoryMsg(strMsg);
        }
    }

    /**
     * 发送消息后的回包
     * modify table conversation
     *
     * @param strMsg
     */
    private void pushSendMsgBack(String strMsg) {

        LogUtil.d("ZLOVE", "---pushSendMsgBack---");

        ReceiveMsgVo receiveMsgVo = JSON.parseObject(strMsg, ReceiveMsgVo.class);
        if (receiveMsgVo != null) {
            if (receiveMsgVo.getData().getMtype() == IMMsgFlag.MSG_CHAT) {
                List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
                BaseDatabaseExecutor.SQLSentence updateMsgSql;
                if (receiveMsgVo.getData().getContent().getCtype() == IMMsgFlag.MSG_CHAT_IMAGE) {
                    updateMsgSql = new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + "=?", new String[]{receiveMsgVo.getData().getCode()}, receiveMsgVo.getImgContentValues(receiveMsgVo));
                } else {
                    updateMsgSql = new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + "=?", new String[]{receiveMsgVo.getData().getCode()}, receiveMsgVo.getContentValues(receiveMsgVo));
                }
                sqlSentences.add(updateMsgSql);
                ContentValues values = new ContentValues();
                values.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE, 1);
                sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + "=?", new String[]{receiveMsgVo.getData().getCode()}, values));
                DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
                executor.execute(executor);

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT_TYPE, receiveMsgVo.getData().getContent().getCtype());
                contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_CONTENT, receiveMsgVo.getData().getContent().getMsg());
                contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_MSG_STAMP, receiveMsgVo.getData().getDnjts());
                List<BaseDatabaseExecutor.SQLSentence> updateConversationSql = new ArrayList<>();
                updateConversationSql.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=?", new String[]{receiveMsgVo.getData().getTo()}, contentValues));
                DatabaseUpdateExecutor updateExecutor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, updateConversationSql, DaniujiaUris.URI_CONVERSATION);
                updateExecutor.execute(updateExecutor);
            }
        }
    }

    private void handlerVoiceMsg(MessageEntity messageEntity) {
        String url = messageEntity.getContent().getUrl();
        String[] str = url.split("/");
        final File file = new File(FileStorage.APP_VOICE_DIR, str[str.length - 1]);
        if (file.exists() && file.length() > 0) {
            return;
        }
        QiniuDownloadUtil.download(file, url, new QiniuDownloadUtil.DownloadCallback() {
            @Override
            public void downloadComplete(String path) {
                updateDownState(path);
            }

            @Override
            public void onProgress(int max, int progress) {

            }

            @Override
            public void onFail(Exception e) {
                updateDownState(file.getPath());
            }
        });
    }

    private void updateDownState(String path) {
        String[] str = path.split("/");
        String sqlUrl = QiniuUtil.getResUrl(str[str.length - 1]);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IS_DOWN, 1);
        List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
        sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_URL + "=?", new String[]{sqlUrl}, contentValues));
        DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
        executor.execute(executor);
    }
}
