package com.xiaojia.daniujia.ui.control;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.bean.BaseBeanDB;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseInsertExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseUpdateExecutor;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.domain.resp.OrderDetailRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.ui.frag.MessageFragment;
import com.xiaojia.daniujia.ui.views.MsgSendWidget;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.NetUtils;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/17
 */
public class MessageControl extends BaseLoaderControl<MessageFragment> implements MsgSendWidget.MsgSendListener {

    private int action;
    private static MessageLoaderCallback loaderCallback;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loaderCallback = new MessageLoaderCallback();
        initLoader(R.id.id_loader_message, null, loaderCallback);
    }

    private class MessageLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(ApplicationUtil.getApplicationContext(), DaniujiaUris.URI_MESSAGE, null,
                    DatabaseConstants.MessageColumn.COLUMN_NAME_FROM + "=? AND " + DatabaseConstants.MessageColumn.COLUMN_NAME_TO + "=?" + " OR " +
                            DatabaseConstants.MessageColumn.COLUMN_NAME_FROM + "=? AND " + DatabaseConstants.MessageColumn.COLUMN_NAME_TO + "=?",
                    new String[]{getFragment().topic, SysUtil.getPref(PrefKeyConst.PREF_ACCOUNT), SysUtil.getPref(PrefKeyConst.PREF_ACCOUNT), getFragment().topic},
                    DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STAMP + " ASC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            List<MessageEntity> messageList = BaseBeanDB.readListFromCursor(cursor, new MessageEntity());
            if (!ListUtils.isEmpty(messageList)) {
                getFragment().showMessageList(messageList);
            } else {
                MobclickAgent.reportError(getAttachedActivity(), "Message List Is Empty.");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    }

    public void getHistoryMessage(String topic, long messageId, int limit) {
        MqttUtils.getMessageList(topic, messageId, limit);
    }

    public void sendConfirmReadMsg(String topic, long messageId) {
        if (TextUtils.isEmpty(topic)) {
            return;
        }
        MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();
        contentEntity.setMsg("Confirm Read");
        contentEntity.setMessageId(messageId);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageId(System.currentTimeMillis());
        messageEntity.setMtype(IMMsgFlag.MSG_CHAT_READ_CONFIRM);
        messageEntity.setFrom(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME));
        messageEntity.setTo(getFragment().topic);
        messageEntity.setDnjts(System.currentTimeMillis());
        messageEntity.setCode(String.valueOf(System.currentTimeMillis()));
        contentEntity.setCtype(IMMsgFlag.MSG_CHAT);
        messageEntity.setContent(contentEntity);
        messageEntity.setMsgState(0);
        messageEntity.setIsRead(1);
        MqttUtils.sendChatMsg_newVersion(messageEntity);
    }

    public void clearUnRead(String topic) {
        if (TextUtils.isEmpty(topic)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.ConversationColumn.COLUMN_NAME_UNREAD_COUNT, 0);
        List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
        sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=?", new String[]{topic}, contentValues));
        DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, sqlSentences, DaniujiaUris.URI_CONVERSATION);
        executor.execute(executor);
    }

    public void setSendMsgFail() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE, 2);
        List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
        sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE + "=?", new String[]{"0"}, contentValues));
        DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
        executor.execute(executor);
    }

    @Override
    public void onSendText(String text) {
        if (!NetUtils.isNetAvailable(getContext())) {
            showShortToast("网络不可用,请检查您的网络");
            return;
        }
        if (!MqttUtils.isConnected()) {
            showShortToast("聊天服务器暂未连接,请稍后再试");
            return;
        }
        action = IMMsgFlag.MSG_CHAT_TEXT;
        String topic = getFragment().topic;
        if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(text)) {
            return;
        }
        MessageEntity messageEntity = new MessageEntity();
        MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();
        contentEntity.setMsg(text);
        messageEntity.setContent(contentEntity);
        insertTmp(messageEntity, IMMsgFlag.MSG_CHAT, IMMsgFlag.MSG_CHAT_TEXT);
        getFragment().scrollToBottom();
        MqttUtils.sendChatMsg_newVersion(messageEntity);
    }

    @Override
    public void onSendImage(File file, int width, int height) {
        if (!NetUtils.isNetAvailable(getContext())) {
            showShortToast("网络不可用,请检查您的网络");
            return;
        }
        if (!MqttUtils.isConnected()) {
            showShortToast("聊天服务器暂未连接,请稍后再试");
            return;
        }
        action = IMMsgFlag.MSG_CHAT_IMAGE;
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setUploadProgress(0);
        MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();
        contentEntity.setWidth(width);
        contentEntity.setHeight(height);
        contentEntity.setMsg(file.getAbsolutePath());
        messageEntity.setContent(contentEntity);

        insertTmp(messageEntity, IMMsgFlag.MSG_CHAT, IMMsgFlag.MSG_CHAT_IMAGE);
        getFragment().scrollToBottom();
        upLoad(file, messageEntity);
    }

    @Override
    public void onEmotionClick() {
        if (getFragment().isLastItem()) {
            // (zp) 解决由软键盘切换到表情符时，消息列表自动滑动到顶部的bug
            getFragment().scrollToBottom();
        }

    }

    @Override
    public void onSendVoice(String voiceFilePath, int duration) {
        if (!NetUtils.isNetAvailable(getContext())) {
            showShortToast("网络不可用,请检查您的网络");
            return;
        }
        if (!MqttUtils.isConnected()) {
            showShortToast("聊天服务器暂未连接,请稍后再试");
            return;
        }
        File file = new File(voiceFilePath);
        if (!file.exists() || file.length() == 0 || duration == 0) {
            return;
        }
        action = IMMsgFlag.MSG_CHAT_VOICE;
        MessageEntity messageEntity = new MessageEntity();
        MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();
        contentEntity.setUrl(voiceFilePath);
        contentEntity.setDuration(duration);
        messageEntity.setContent(contentEntity);
        insertTmp(messageEntity, IMMsgFlag.MSG_CHAT, IMMsgFlag.MSG_CHAT_VOICE);
        getFragment().scrollToBottom();
        upLoad(file, messageEntity);
    }

    private void getMsgContent(MessageEntity messageEntity, String key, int msgType) {
        String resUrl = QiniuUtil.getResUrl(key);
        if (msgType == IMMsgFlag.MSG_CHAT_VOICE) {
            messageEntity.getContent().setUrl(resUrl);
        } else {
            messageEntity.getContent().setMsg(resUrl);
        }
    }

    private void insertTmp(MessageEntity messageEntity, int msgType, int msgContentType) {
        MessageEntity.ContentEntity contentEntity = messageEntity.getContent();
        messageEntity.setMessageId(System.currentTimeMillis());
        messageEntity.setMtype(msgType);
        messageEntity.setFrom(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME));
        messageEntity.setTo(getFragment().topic);
        messageEntity.setDnjts(System.currentTimeMillis());
        messageEntity.setCode(String.valueOf(System.currentTimeMillis()));
        contentEntity.setCtype(msgContentType);
        messageEntity.setContent(contentEntity);
        messageEntity.setMsgState(0);
        messageEntity.setIsRead(0);
        if (msgType == IMMsgFlag.MSG_CHAT) {
            List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
            sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(null, null, messageEntity.getContentValues(messageEntity)));
            DatabaseInsertExecutor executor = new DatabaseInsertExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
            executor.execute(executor);
        }
    }

    private void upLoad(final File absoluteFile, final MessageEntity dataEntity) {
        ThreadWorker.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final String fileName;
                    if (action == IMMsgFlag.MSG_CHAT_VOICE) {
                        fileName = WUtil.genVoiceFileName();
                    } else {
                        fileName = absoluteFile.getName();
                    }
                    QiniuUtil.upload(absoluteFile, fileName, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                            if (action == IMMsgFlag.MSG_CHAT_VOICE) {
                                if (dataEntity.getContent().getDuration() == 0) {
                                    return;
                                }
                                getMsgContent(dataEntity, key, IMMsgFlag.MSG_CHAT_VOICE);
                                MqttUtils.sendChatMsg_newVersion(dataEntity);
                            } else if (action == IMMsgFlag.MSG_CHAT_IMAGE) {
                                if (dataEntity.getContent().getWidth() == 0 || dataEntity.getContent().getHeight() == 0) {
                                    return;
                                }
                                getMsgContent(dataEntity, key, IMMsgFlag.MSG_CHAT_IMAGE);
                                MqttUtils.sendChatMsg_newVersion(dataEntity);
                            }
                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {
                        int i = 0;

                        @Override
                        public void progress(String s, final double v) {
                            i++;
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IMG_UPLOAD_PROGRESS, (int) (v * 100));
                                    List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
                                    sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + "=?", new String[]{dataEntity.getCode()}, contentValues));
                                    DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
                                    executor.execute(executor);
                                }
                            }, i * 100);
                        }
                    }, null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getOrderInfoRequest(String topic) {
        String url = Config.WEB_API_SERVER + "/user/order/chat/detail/" + topic;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<OrderDetailRespVo>() {
            @Override
            public void onResponse(OrderDetailRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setData(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
            }
        });
    }

    public void cancelOrderRequest(final String topic, int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/cancel";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getOrderInfoRequest(topic);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "cancelOrderRequest---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void expertConfirmOrderRequest(final String topic, final int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/confirm";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getOrderInfoRequest(topic);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "cancelOrderRequest---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void expertFinishOrderRequest(final String topic, final int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/terminate";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getOrderInfoRequest(topic);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "cancelOrderRequest---onFail---" + errorMsg);
            }
        }, builder);
    }


    public void modifyQuesOrProfile(int orderId, String content, int modifyType) {
        String url = Config.WEB_API_SERVER + "/user/order/update";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        if (modifyType == 1) {
            builder.add("profile", content);
        } else if (modifyType == 2) {
            builder.add("quesDesc", content);
        }
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    showShortToast("更新成功");
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "modifyQuesOrProfile---onFail---" + errorMsg);
            }
        }, builder);
    }

    Handler mHandler = new Handler();


    public void callServer(int orderId, int userId) {
        String url = Config.WEB_API_SERVER + "/user/pstn/call";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("orderId", String.valueOf(orderId));
        builder.add("to_user", String.valueOf(userId));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(false, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    LogUtil.d("ZLOVE", "Call Success");
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "callServer---errorMsg---" + errorMsg);
            }
        }, builder);
    }

}
