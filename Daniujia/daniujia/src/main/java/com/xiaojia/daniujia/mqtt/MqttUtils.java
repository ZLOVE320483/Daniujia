package com.xiaojia.daniujia.mqtt;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseUpdateExecutor;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.mqtt.ActionListener.Action;
import com.xiaojia.daniujia.mqtt.MqttMsgCallbackHandler.MqttChatCallback;
import com.xiaojia.daniujia.mqtt.MqttMsgCallbackHandler.MqttRealTimeChatCallback;
import com.xiaojia.daniujia.mqtt.service.MqttClientAndroidService;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SpecificJsonUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Description package functions used by mqtt every function has it own
 * corresponding callback in MqttMsgCallbackHandler
 * msgHandler.setCallback(Instance) and
 * msgHandler.setCallback(null) come in pairs
 */
public class MqttUtils {
    private static final String TAG = "MqttUtils";
    private static Context context;
    public static MqttMsgCallbackHandler msgHandler;
    private static int chatHashCode, realtimeChatHashCode;
    private static final String CA_AUTH = "123456";
    private static ActionListener callback;
    private static Connection connection;

    static {
        context = ApplicationUtil.getApplicationContext();
        msgHandler = new MqttMsgCallbackHandler();
        chatHashCode = realtimeChatHashCode = 0;
    }

    public static void setChatCallback(MqttChatCallback chatCallback, int hashCode) {
        System.out.println("chatCallback" + chatCallback);
        if (chatCallback == null) {
            if (MqttUtils.chatHashCode == hashCode) {
                msgHandler.setChatCallback(chatCallback);
            }
        } else {
            MqttUtils.chatHashCode = hashCode;
            msgHandler.setChatCallback(chatCallback);
        }
    }

    public static void setLoginCallback(MqttMsgCallbackHandler.MqttLoginCallback chatCallback, int hashCode) {
        System.out.println("chatCallback" + chatCallback);
        if (chatCallback == null) {
            if (MqttUtils.chatHashCode == hashCode) {
                msgHandler.setLoginCallback(chatCallback);
            }
        } else {
            MqttUtils.chatHashCode = hashCode;
            msgHandler.setLoginCallback(chatCallback);
        }
    }

    public static void setRealTimeChatCallback(MqttRealTimeChatCallback realtimeChatCallback, int hashCode) {
        if (realtimeChatCallback == null) {
            if (MqttUtils.realtimeChatHashCode == hashCode) {
                msgHandler.setRealTimeChatCallback(realtimeChatCallback);
            }
        } else {
            MqttUtils.realtimeChatHashCode = hashCode;
            msgHandler.setRealTimeChatCallback(realtimeChatCallback);
        }
    }

    private static ServerConfig getServerConfig() {
        ServerConfig mServerConfig = new ServerConfig();
        mServerConfig.user = SysUtil.getPref(PrefKeyConst.PREF_USER_NAME);
        mServerConfig.pwd = SysUtil.getPref(PrefKeyConst.PREF_PASSWORD);
        mServerConfig.host = Config.MQTT_SERVER_HOST;
        mServerConfig.port = Config.MQTT_SERVER_PORT;
        mServerConfig.clientId = mServerConfig.user + "-2-" + ApplicationUtil.getVersionName();
        return mServerConfig;
    }

    public static MqttConnectOptions getConPt() {
        MqttConnectOptions conOpt = new MqttConnectOptions();
        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLS");
//            if (Config.DEBUG) {
            sslContext.init(null, new TrustManager[]{new MyTrustManager()}, null);
//            } else {
//                KeyStore ts = KeyStore.getInstance("BKS");
//                ts.load(context.getResources().openRawResource(Config.DEBUG ? R.raw.test_cert : R.raw.cert),
//                        CA_AUTH.toCharArray());
//                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
//                tmf.init(ts);
//                TrustManager[] tm = tmf.getTrustManagers();
//                sslContext.init(null, tm, null);
//            }
            SocketFactory factory = sslContext.getSocketFactory();
            conOpt.setSocketFactory(factory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conOpt;
    }

    /**
     * every connection will read SSL config, bad performance TODO
     */
    public static void connect() {
        System.out.println("do Connect");

        // 连接中...
        MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_MQTT_CONNECTING);

        ServerConfig serverConfig = getServerConfig();
        // connection option
        String[] actionArgs = new String[1];
        actionArgs[0] = serverConfig.clientId;
        MqttConnectOptions conOpt = getConPt();
        conOpt.setCleanSession(false);
        conOpt.setConnectionTimeout(ServerConfig.CONN_TIMEOUT);
        conOpt.setKeepAliveInterval(ServerConfig.CONN_KEEPALIVE);
        if (!TextUtils.isEmpty(serverConfig.user)) {
            conOpt.setUserName(serverConfig.user);
        }
        if (!TextUtils.isEmpty(serverConfig.pwd)) {
            conOpt.setPassword(serverConfig.pwd.toCharArray());
        }
        if (callback == null) {
            callback = new ActionListener(context, ActionListener.Action.CONNECT, msgHandler,
                    actionArgs);
        }
        if (connection == null) {
            connection = Connection.getInstance().init(serverConfig, conOpt);
        }
        MqttCallbackHandler handler = new MqttCallbackHandler(context, msgHandler);
        connection.setClientCallback(handler);
        try {
            connection.connect(callback);
        } catch (MqttException e) {
            Log.e(TAG, "MqttException Occured", e);
        }
    }

    public static void reconnect() {
        if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            return;
        }
        if (isConnected()) {
            return;
        }
        System.out.println("do Reconnect");
        MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_MQTT_CONNECTING);
        //handleConnectionLost();
        ServerConfig serverConfig = getServerConfig();
        // it should be static
        MqttConnectOptions conOpt = getConPt();

        String[] actionArgs = new String[1];
        actionArgs[0] = serverConfig.clientId;

        conOpt.setCleanSession(false);
        conOpt.setConnectionTimeout(ServerConfig.CONN_TIMEOUT);
        conOpt.setKeepAliveInterval(ServerConfig.CONN_KEEPALIVE);
        if (!TextUtils.isEmpty(serverConfig.user)) {
            conOpt.setUserName(serverConfig.user);
        }
        if (!TextUtils.isEmpty(serverConfig.pwd)) {
            conOpt.setPassword(serverConfig.pwd.toCharArray());
        }
        final ActionListener callback = new ActionListener(context, ActionListener.Action.CONNECT, msgHandler,
                actionArgs);
        if (connection == null) {
            connection = Connection.getInstance().init(serverConfig, conOpt);
        }
        MqttCallbackHandler handler = new MqttCallbackHandler(context, msgHandler);
        connection.setClientCallback(handler);
        try {
            connection.connect(callback);
        } catch (MqttException e) {
            Log.e(TAG, "MqttException Occured", e);
        }
    }

    public static void subscribe(String topic) {
        MqttClientAndroidService client = Connection.getInstance().getClient();
        if (!client.isConnected()) {
            return;
        }
        if (TextUtils.isEmpty(topic)) {
            return;
        }
//		topic = new String(new StringBuffer(topic).insert(0, "dnj/"));
        String[] topics = new String[1];
        topics[0] = topic;
        try {
            client.subscribe(topic, 1, null, new ActionListener(context, ActionListener.Action.SUBSCRIBE, msgHandler,
                    topics));
        } catch (MqttSecurityException e) {
            Log.e(TAG, "Failed to subscribe to" + topic, e);
        } catch (MqttException e) {
            Log.e(TAG, "Failed to subscribe to" + topic, e);
        }
    }

    public static void subscribe(String... topics) {
        MqttClientAndroidService client = Connection.getInstance().getClient();
        if (!client.isConnected()) {
            return;
        }
        if (topics == null || topics.length == 0) {
            return;
        }
        for (String topic : topics) {
            if (!TextUtils.isEmpty(topic)) {
                try {
                    topic = new String(new StringBuffer(topic).insert(0, "presence-"));
                    client.subscribe(topic, 1, null, new ActionListener(context, ActionListener.Action.SUBSCRIBE,
                            msgHandler, topics));
                } catch (MqttSecurityException e) {
                    Log.e(TAG, "Failed to subscribe to" + topic, e);
                } catch (MqttException e) {
                    Log.e(TAG, "Failed to subscribe to" + topic, e);
                }
            }
        }
    }

    public static void unsubscribe(String topic) {
        MqttClientAndroidService client = Connection.getInstance().getClient();
        if (!client.isConnected()) {
            return;
        }
        if (TextUtils.isEmpty(topic)) {
            return;
        }
        String[] topics = new String[1];
        topics[0] = topic;
        topic = new String(new StringBuffer(topic).insert(0, "presence-"));
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void publish(String topic, JSONObject jsonObj, int qos) {
        MqttClientAndroidService client = Connection.getInstance().getClient();
        if (client == null || !client.isConnected()) {
            return;
        }
        if (TextUtils.isEmpty(topic)) {
            return;
        }
        //topic = new String(new StringBuffer(topic).insert(0, "private-"));
        String[] args = new String[2];
        if (jsonObj != null && TextUtils.isEmpty(jsonObj.toString()) && jsonObj.optLong("time") != 0) {
            args[0] = String.valueOf(jsonObj.optLong("time"));
            args[1] = topic;
        }
        try {
            client.publish(topic, jsonObj.toString().getBytes(), qos, false, null, new ActionListener(context,
                    Action.PUBLISH, null, args));
        } catch (MqttSecurityException e) {
            Log.e(TAG, "Failed to publish a messged " + topic, e);
        } catch (MqttException e) {
            Log.e(TAG, "Failed to publish a messged " + topic, e);
        }
    }

    public static void publish(String topic, JSONObject jsonObj) {
        MqttClientAndroidService client = Connection.getInstance().getClient();
        if (client != null && !client.isConnected()) {
            return;
        }

        String[] args = new String[2];
        if (jsonObj.optLong("time") != 0) {
            args[0] = String.valueOf(jsonObj.optLong("time"));
            args[1] = topic;
        }

        try {
            client.publish(topic, jsonObj.toString().getBytes(), 1, false, null, new ActionListener(context,
                    Action.PUBLISH, msgHandler, args));
        } catch (MqttSecurityException e) {
            Log.e(TAG, "Failed to publish a messged " + topic, e);
        } catch (MqttException e) {
            Log.e(TAG, "Failed to publish a messged " + topic, e);
        }
    }

    public static void publish(String topic, JSONObject jsonObj, boolean prefix) {
        MqttClientAndroidService client = Connection.getInstance().getClient();
        if (client == null || !client.isConnected()) {
            return;
        }
        if (TextUtils.isEmpty(topic)) {
            return;
        }
        String[] args = new String[2];
        if (jsonObj.optLong("time") != 0) {
            args[0] = String.valueOf(jsonObj.optLong("time"));
            args[1] = topic;
        }

        try {
            client.publish(topic, jsonObj.toString().getBytes(), 1, false, null, new ActionListener(context,
                    Action.PUBLISH, msgHandler, args));
        } catch (MqttSecurityException e) {
            Log.e(TAG, "Failed to publish a messged " + topic, e);
        } catch (MqttException e) {
            Log.e(TAG, "Failed to publish a messged " + topic, e);
        }
    }


//	public static void sendCandidate(IceCandidate candidate, Driver driver) {
//		JSONObject json = new JSONObject();
//		try {
//			json.put("type", "candidate");
//			json.put("label", candidate.sdpMLineIndex);
//			json.put("ID", candidate.sdpMid);
//			json.put("candidate", candidate.sdp);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		OtherInfo other = driver.getOtherInfo();
//		if (other == null) {
//			return;
//		}
//		MessageImpl msg = new MessageImpl();
//		msg.content = json;
//		msg.fromUser = UserModule.Instance.getUserInfo().getUsername();
//		msg.toUser = other.username;
//		msg.msgType = IMMsgFlag.MSG_WEB_RTC_CHAT;
//
//		publish(other.username, SpecificJsonUtils.generateRTCJson(msg), 0);
//	}

//	public static void sendCloseMsg(Driver driver) {
//		OtherInfo other = driver.getOtherInfo();
//		if (other == null) {
//			return;
//		}
//		MessageImpl msg = new MessageImpl();
//		msg.fromUser = UserModule.Instance.getUserInfo().getUsername();
//		msg.toUser = other.username;
//		msg.msgType = IMMsgFlag.MSG_WEB_RTC_CHAT_END;
//		msg.chatId = other.chatId;
//		publish(other.username, SpecificJsonUtils.generateRTCJson(msg), 0);
//	}

//	public static void sendChatMsg(ChatDetail chatDetail, OtherInfo other) {
//		if (other == null) {
//			return;
//		}
//		MessageImpl msg = new MessageImpl();
//		msg.fromUser = UserModule.Instance.getUserInfo().getUsername();
//		msg.toUser = other.username;
//		msg.msgType = IMMsgFlag.MSG_CHAT;
//		msg.chatId = other.chatId;
//		publish(other.username, SpecificJsonUtils.generateChatJson(chatDetail, msg));
//	}

    public static void sendChatMsg_newVersion(MessageEntity entity) {

        if (entity == null || TextUtils.isEmpty(entity.getTo())) {
            return;
        }

        JSONObject jsonObject = SpecificJsonUtils.generateChatJson_newVersion(entity);
        MqttClientAndroidService client = Connection.getInstance().getClient();

        if (client == null) {
            LogUtil.d("ZLOVE", "client is null");
            return;
        }

        if (jsonObject == null) {
            LogUtil.d("ZLOVE", "jsonObject is null");
            return;
        }
        String[] args = new String[2];
        args[0] = String.valueOf(entity.getCode());
        args[1] = "private-" + entity.getTo();
        byte[] playLoads = jsonObject.toString().getBytes();
        ActionListener actionListener = new ActionListener(context, Action.PUBLISH, msgHandler, args);
        try {
            if (client.isConnected()) {
                client.publish(MqttServiceConstants.MESSAGE_SEND_REQUEST, playLoads, 1, false, null, actionListener);
            } else if (entity.getMtype() == IMMsgFlag.MSG_CHAT){
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE, 2);
                        List<BaseDatabaseExecutor.SQLSentence> updateSql = new ArrayList<>();
                        updateSql.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE + "=?", new String[]{"0"}, contentValues));
                        DatabaseUpdateExecutor updateExecutor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, updateSql, DaniujiaUris.URI_MESSAGE);
                        updateExecutor.execute(updateExecutor);
                    }
                }, 2000);
            }
        } catch (Exception e) {
            LogUtil.d("ZLOVE", "Failed to publish a message ---" + e.toString());
        }
    }

//	public static void sendChatMsgToCustomer(ChatDetail chatDetail, OtherInfo other) {
//		if (other == null) {
//			return;
//		}
//		MessageImpl msg = new MessageImpl();
//		msg.fromUser = UserModule.Instance.getUserInfo().getUsername();
//		msg.toUser = "eric";
//		msg.msgType = IMMsgFlag.MSG_CHAT;
//		msg.chatId = other.chatId;
//		publish("", SpecificJsonUtils.generateChatJson(chatDetail, msg));
//	}

//	public static void sendCheckMsg(ChatDetail chatDetail, OtherInfo other) {
//		if (other == null) {
//			return;
//		}
//		publish(other.username, SpecificJsonUtils.generateCheckJson(chatDetail, other.username));
//	}

    public static boolean isConnected() {
        return Connection.getInstance().isConnected();
    }

    //handle exception here? TODO
    public static void disconnect() {
        try {
            Connection.getInstance().disConnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * It have to go to this step when connection lost
     */
    public static void handleConnectionLost() {
        System.out.println("must go to this step");
        Connection instance = Connection.getInstance();
        if (instance != null) {
            MqttClientAndroidService client = instance.getClient();
            if (client != null) {
                client.doHandleExit();
            }
        }
        connection = null;
    }

    public static void getMessageList(String target, long startId, int limit) {
        JSONObject json = new JSONObject();
        try {
            json.put("target", target);
            json.put("type", "singleChat");
            if (startId != 0) {
                json.put("startId", startId);
            }
            json.put("limit", limit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MqttUtils.publish_request(MqttServiceConstants.MESSAGE_GET_REQUEST, json);
    }

    public static void publish_request(String request, JSONObject jsonObj) {
        MqttClientAndroidService client = Connection.getInstance().getClient();
        if (client == null || !client.isConnected()) {
            return;
        }

        if (TextUtils.isEmpty(request)) {
            return;
        }

        if (jsonObj == null) {
            return;
        }

        String[] args = new String[2];
        args[0] = System.currentTimeMillis() + "";
        args[1] = request;

        byte[] playLoads = jsonObj.toString().getBytes();
        ActionListener actionListener = new ActionListener(context, Action.PUBLISH, msgHandler, args);
        try {
            client.publish(request, playLoads, 0, false, null, actionListener);
        } catch (Exception e) {
            Log.e(TAG, "Failed to publish a messged " + request, e);
        }
    }

    private static class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
