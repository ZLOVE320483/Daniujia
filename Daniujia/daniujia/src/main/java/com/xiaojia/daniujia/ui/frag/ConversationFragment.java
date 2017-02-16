package com.xiaojia.daniujia.ui.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseDeleteExecutor;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.domain.resp.PullConversationsVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.mqtt.MqttMsgCallbackHandler;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.msg.OnMsgCallback;
import com.xiaojia.daniujia.msg.annotation.OnMsg;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.ui.act.ActChat;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActRegister;
import com.xiaojia.daniujia.ui.adapters.ConversationAdapter;
import com.xiaojia.daniujia.ui.control.ConversationControl;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by ZLOVE on 2016/4/10
 */
public class ConversationFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, TagAliasCallback, XListView.IXListViewListener {

    private TextView tvTitle;
    private ConversationControl conversationControl;
    private XListView conversationList;
    private View notLoginView;
    private Button btnLogin;
    private Button btnRegister;
    private ConversationAdapter conversationAdapter;
    private List<PullConversationsVo.DataEntity> recordList = new ArrayList<>();

    private boolean isDeleteConversation = false;
    private String topic;

//    private View headView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MsgHelper.getInstance().registMsg(this);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_chat_record;
    }

    @Override
    protected void setUpView(View view) {
        tvTitle = (TextView) view.findViewById(R.id.id_title);
        tvTitle.setText("消息");
        conversationList = (XListView) view.findViewById(R.id.xlv_chat_record);
        notLoginView = view.findViewById(R.id.not_login);
        btnLogin = (Button) view.findViewById(R.id.login);
        btnRegister = (Button) view.findViewById(R.id.register);
        conversationList.setOnItemClickListener(this);
        conversationList.setOnItemLongClickListener(this);
        conversationList.setPullLoadEnable(false);
        conversationList.setPullRefreshEnable(true);
        conversationList.setXListViewListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
//        initHeadView();
        if (conversationAdapter == null) {
            conversationAdapter = new ConversationAdapter(recordList, getActivity());
        }
        conversationList.setAdapter(conversationAdapter);
    }

    @Override
    public void onResume() {
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            if (conversationControl == null) {
                conversationControl = new ConversationControl();
            }
            setControl(conversationControl);
            notLoginView.setVisibility(View.GONE);
            conversationList.setVisibility(View.VISIBLE);
            if (ListUtils.isEmpty(recordList)) {
                conversationControl.getHistoryConversationList();
            }
            if (!MqttUtils.isConnected()) {
                tvTitle.setText("消息(未连接)");
            }
        } else {
            tvTitle.setText("消息");
            notLoginView.setVisibility(View.VISIBLE);
            conversationList.setVisibility(View.GONE);
        }
        super.onResume();
    }

//    private void initHeadView() {
//        headView = LayoutInflater.from(getActivity()).inflate(R.layout.list_head_conversation, null);
//        conversationList.addHeaderView(headView);
//    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            Intent intent = new Intent(getActivity(), ActLogin.class);
            startActivity(intent);
        } else if (v == btnRegister) {
            Intent intent = new Intent(getActivity(), ActRegister.class);
            startActivityForResult(intent, IntentKey.REQ_CODE_REGISTER);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headCount = conversationList.getHeaderViewsCount();
        if (position - headCount < 0) {
            return;
        }

        PullConversationsVo.DataEntity item = conversationAdapter.getItem(position - headCount);
        if (item == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), ActChat.class);
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, item.getTarget().getUsername());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_NAME, item.getTarget().getName());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL, item.getTarget().getAvatarUrl());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, item.getTarget().getUserId());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, item.getTarget().getIdentity());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        BaseMsgDlg dialog = new BaseMsgDlg(getActivity());
        dialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        dialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        dialog.setTitle("温馨提示");
        dialog.setMsg("确定删除该会话？");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                int headCount = conversationList.getHeaderViewsCount();
                String topic = conversationAdapter.getItem(position - headCount).getTarget().getUsername();

                recordList.remove(position - headCount);
                conversationAdapter.notifyDataSetChanged();

                isDeleteConversation = true;
                conversationControl.deleteConversationItem(topic);
                BaseDatabaseExecutor.SQLSentence sqlSentence = new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.ConversationColumn.COLUMN_NAME_TARGET_NAME + "=?", new String[]{topic}, null);
                List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
                sqlSentences.add(sqlSentence);
                DatabaseDeleteExecutor executor = new DatabaseDeleteExecutor(DatabaseConstants.Tables.TABLE_NAME_CONVERSATION, sqlSentences, DaniujiaUris.URI_CONVERSATION);
                executor.execute(executor);
            }
        });
        dialog.show();
        return true;
    }

    public void showRecordList(List<PullConversationsVo.DataEntity> recordList) {
        conversationList.stopRefresh();
        if (isDeleteConversation) {
            isDeleteConversation = false;
            return;
        }
        this.recordList.clear();
        this.recordList.addAll(recordList);
        conversationAdapter.notifyDataSetChanged();
    }

    public void clearData() {
        this.recordList.clear();
        conversationAdapter.notifyDataSetChanged();
    }

    public void setTopic(String topic) {
        if (TextUtils.isEmpty(topic)) {
            return;
        }
        this.topic = topic;
        if (conversationControl == null) {
            conversationControl = new ConversationControl();
            setControl(conversationControl);
        }
        conversationControl.judgeConversation(topic);
    }

    public void goChat(PullConversationsVo.DataEntity dataEntity) {
        if (!isAdded()) {
            return;
        }
        topic = "";
        Intent intent = new Intent(getActivity(), ActChat.class);
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, dataEntity.getTarget().getUsername());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_NAME, dataEntity.getTarget().getName());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL, dataEntity.getTarget().getAvatarUrl());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, dataEntity.getTarget().getUserId());
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, dataEntity.getTarget().getIdentity());
        startActivity(intent);

    }

    @OnMsg(msg = {InsideMsg.NOTIFY_GET_CONVERSATION, InsideMsg.NOTIFY_CONVERSATION_ARRIVED, InsideMsg.NOTIFY_CONVERSATION_EMPTY
            , InsideMsg.NOTIFY_MQTT_CONNECTED, InsideMsg.NOTIFY_MQTT_CONNECTING, InsideMsg.NOTIFY_MQTT_UNCONNECT}, useLastMsg = false)
    OnMsgCallback onMsgCallback = new OnMsgCallback() {
        @Override
        public boolean handleMsg(Message msg) {
            if (!isAdded()) {
                return false;
            }
            switch (msg.what) {
                case InsideMsg.NOTIFY_GET_CONVERSATION:
                    if (!TextUtils.isEmpty(topic)) {
                        PullConversationsVo.DataEntity item = (PullConversationsVo.DataEntity) msg.obj;
                        if (item.getTarget().getUsername().equals(topic)) {
                            topic = "";
                            Intent intent = new Intent(getActivity(), ActChat.class);
                            intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, item.getTarget().getUsername());
                            intent.putExtra(IntentKey.INTENT_KEY_CHAT_NAME, item.getTarget().getName());
                            intent.putExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL, item.getTarget().getAvatarUrl());
                            intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, item.getTarget().getUserId());
                            intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, item.getTarget().getIdentity());
                            startActivity(intent);
                        }
                    }
                    break;
                case InsideMsg.NOTIFY_CONVERSATION_ARRIVED:
                    conversationList.stopRefresh();
                    break;
                case InsideMsg.NOTIFY_CONVERSATION_EMPTY:
                    if (TextUtils.isEmpty(topic)) {
                        clearData();
                    }
                    break;
                case InsideMsg.NOTIFY_MQTT_CONNECTED:
                    tvTitle.setText("消息");
                    break;
                case InsideMsg.NOTIFY_MQTT_CONNECTING:
                    tvTitle.setText("消息(连接中...)");
                    break;
                case InsideMsg.NOTIFY_MQTT_UNCONNECT:
                    tvTitle.setText("消息(未连接)");
                    break;
            }
            return false;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentKey.REQ_CODE_REGISTER) {
            if (data != null) {
                doLogin();
            }
        }
    }

    private void doLogin() {
        String loginUrl = Config.WEB_API_SERVER + "/login";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("username", SysUtil.getPref(PrefKeyConst.PREF_ACCOUNT));
        builder.add("password", SysUtil.getPref(PrefKeyConst.PREF_PASSWORD));
        OkHttpClientManager.getInstance(getActivity()).postWithPlatform(true, loginUrl, new OkHttpClientManager.ResultCallback<UserInfo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                LogUtil.d("ZLOVE", "login---onError---" + e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(UserInfo userInfo) {
                if (userInfo == null) {
                    return;
                }
                userInfo.setPassword(SysUtil.getPref(PrefKeyConst.PREF_PASSWORD));
                SysUtil.saveUserInfo(userInfo);
                MqttUtils.setLoginCallback(loginCallback, hashCode());
                if (!MqttUtils.isConnected()) {
                    MqttUtils.connect();
                    LogUtil.d("ZLOVE", "do Connect 333333333");
                }
                JPushInterface.setAlias(getActivity(), userInfo.getUsername(), ConversationFragment.this);
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "login---onFail---" + errorMsg);
                try {
                    JSONObject object = new JSONObject(errorMsg);
                    showShortToast(object.getString("errmsg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, builder);
    }

    MqttMsgCallbackHandler.MqttLoginCallback loginCallback = new MqttMsgCallbackHandler.MqttLoginCallback() {
        @Override
        public void onConnect(boolean connected) {
            /**
             *  pull all conversations
             */
            MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, new JSONObject());
        }

        @Override
        public void onSubscirbe(boolean isSuccess) {
        }
    };

    @Override
    public void gotResult(int i, String s, Set<String> set) {
        if (i == 0) {
            LogUtil.d("JPUSH", "设置别名成功");
        }
    }

    @Override
    public void onRefresh() {
        if (!MqttUtils.isConnected()) {
            conversationList.stopRefresh();
            return;
        }
        MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, new JSONObject());
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            conversationList.stopRefresh();
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                if (!MqttUtils.isConnected()) {
                    tvTitle.setText("消息(未连接)");
                }
            }
        }
    }
}
