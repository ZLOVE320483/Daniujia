package com.xiaojia.daniujia.ui.frag;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseInsertExecutor;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.SendFileDlg;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.domain.resp.PullConversationsVo;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.ui.adapters.TransmitListAdapter;
import com.xiaojia.daniujia.ui.control.TransmitListControl;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.NetUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class TransmitListFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText mEtSearch;
    private XListView mLvContent;

    private TransmitListAdapter conversationAdapter;
    private List<PullConversationsVo.DataEntity> recordList = new ArrayList<>();
    private TransmitListControl conversationControl;
    private LinearLayout mLlSearch;
    private MessageEntity.ContentEntity fileObject;

    private BaseDialog.DialogClickListener listener = new BaseDialog.DialogClickListener() {
        @Override
        public void onOK(String s) {
            if (s != null) {
                if (TextUtils.equals(s, "cancel")) {
                    sendFileDlg.dismiss();
                } else {
                    if (TextUtils.equals(userName, "")) {
                        return;
                    }
                    sendAttach(userName);
                    if (!s.trim().equals("")) {
                        sendText(s, userName);
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity().getIntent() != null) {
            fileObject = (MessageEntity.ContentEntity) getActivity().getIntent().getSerializableExtra(IntentKey.INTENT_KEY_ATTACH_ITEM);
        }
    }


    @Override
    protected int getInflateLayout() {
        return R.layout.frag_transmit_list;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("选择约谈者");
        mEtSearch = (EditText) view.findViewById(R.id.transmit_et_search);
        mLvContent = (XListView) view.findViewById(R.id.transmit_list);
        mLlSearch = (LinearLayout) view.findViewById(R.id.transmit_search_container);

        mLvContent.setPullLoadEnable(false);
        mLvContent.setPullRefreshEnable(false);
        if (conversationAdapter == null) {
            conversationAdapter = new TransmitListAdapter(recordList, getActivity());
        }
        mLvContent.setAdapter(conversationAdapter);

        if (conversationControl == null) {
            conversationControl = new TransmitListControl();
        }
        setControl(conversationControl);

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.toString() != null) {
                    conversationControl.searchName(s.toString());
                }
            }
        });

        mLlSearch.setOnClickListener(this);
        mLvContent.setOnItemClickListener(this);

    }

    private void sendText(String text, String topic) {
        if (!NetUtils.isNetAvailable(getContext())) {
            showShortToast("网络不可用,请检查您的网络");
            return;
        }

        MessageEntity messageEntity = new MessageEntity();
        MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();
        contentEntity.setMsg(text);
        messageEntity.setContent(contentEntity);
        insertTmp(messageEntity, IMMsgFlag.MSG_CHAT, IMMsgFlag.MSG_CHAT_TEXT, topic);
        MqttUtils.sendChatMsg_newVersion(messageEntity);
    }

    private void sendAttach(String topic) {
        if (!NetUtils.isNetAvailable(getContext())) {
            showShortToast("网络不可用,请检查您的网络");
            return;
        }

        if (fileObject == null) {
            return;
        }

        MessageEntity messageEntity = new MessageEntity();
        MessageEntity.ContentEntity contentEntity = new MessageEntity.ContentEntity();
        contentEntity.setUrl(fileObject.getUrl());
        contentEntity.setFilename(fileObject.getFilename());
        contentEntity.setSize(fileObject.getSize());
        contentEntity.setMimeType(fileObject.getMimeType());
        contentEntity.setSourcePlatform(fileObject.getSourcePlatform());
        messageEntity.setContent(contentEntity);
        insertTmp(messageEntity, IMMsgFlag.MSG_CHAT, IMMsgFlag.MSG_CHAT_ATTACH, topic);

        MqttUtils.sendChatMsg_newVersion(messageEntity);
        sendFileDlg.dismiss();
        showShortToast("已发送");
        finishActivity();
    }

    private void insertTmp(MessageEntity messageEntity, int msgType, int msgContentType, String topic) {
        MessageEntity.ContentEntity contentEntity = messageEntity.getContent();
        messageEntity.setMessageId(System.currentTimeMillis());
        messageEntity.setMtype(msgType);
        messageEntity.setFrom(SysUtil.getPref(PrefKeyConst.PREF_USER_NAME));
        messageEntity.setTo(topic);
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

    public void showRecordList(List<PullConversationsVo.DataEntity> list) {
        this.recordList.clear();
        this.recordList.addAll(list);
        conversationAdapter.notifyDataSetChanged();
    }

    public void clearData() {
        this.recordList.clear();
        conversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == mLlSearch) {
            mEtSearch.requestFocus();
            UIUtil.showKeyboard(getActivity(), mEtSearch);
        }
    }

    private SendFileDlg sendFileDlg = null;
    private String userName;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headCount = mLvContent.getHeaderViewsCount();
        if (conversationAdapter.getItem(position - headCount) != null) {
            PullConversationsVo.DataEntity dataEntity = conversationAdapter.getItem(position - headCount);
            userName = dataEntity.getTarget().getUsername();
            String personName;
            if (!TextUtils.isEmpty(dataEntity.getTarget().getName())) {
                personName = dataEntity.getTarget().getName();
            } else {
                personName = dataEntity.getTarget().getUsername();
            }
            sendFileDlg = new SendFileDlg(getActivity(), dataEntity.getTarget().getAvatarUrl(), fileObject.getFilename(), personName);
            sendFileDlg.setOnClickListener(listener);
            sendFileDlg.showDialog();
        }
    }
}
