package com.xiaojia.daniujia.ui.frag;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseConstants;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.db.executor.BaseDatabaseExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseInsertExecutor;
import com.xiaojia.daniujia.db.executor.DatabaseUpdateExecutor;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.domain.resp.MessageVo;
import com.xiaojia.daniujia.domain.resp.OrderDetailRespVo;
import com.xiaojia.daniujia.mqtt.MqttMsgCallbackHandler;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.provider.DaniujiaUris;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActExpertReply;
import com.xiaojia.daniujia.ui.act.ActPayDetail;
import com.xiaojia.daniujia.ui.act.ActRejectOrder;
import com.xiaojia.daniujia.ui.act.ActUserHome;
import com.xiaojia.daniujia.ui.act.ActUserOrderComment;
import com.xiaojia.daniujia.ui.adapters.MessageAdapter;
import com.xiaojia.daniujia.ui.control.MessageControl;
import com.xiaojia.daniujia.ui.views.ChatOrderExpertView1To4;
import com.xiaojia.daniujia.ui.views.ChatOrderExpertView5;
import com.xiaojia.daniujia.ui.views.ChatOrderUserView1To4;
import com.xiaojia.daniujia.ui.views.ChatOrderUserView5;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.ui.views.HideChatOrderListener;
import com.xiaojia.daniujia.ui.views.MsgSendWidget;
import com.xiaojia.daniujia.ui.views.MultiDirectionSlidingDrawer;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.TimeUtils;
import com.xiaojia.daniujia.utils.UIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/4/11
 */
public class MessageFragment extends BaseFragment implements XListView.IXListViewListener, MqttMsgCallbackHandler.MqttChatCallback, View.OnClickListener, HideChatOrderListener, AdapterView.OnItemClickListener {
    private MessageControl messageControl;

    private TextView tvTitle;
    private XListView messageList;
    private MessageAdapter messageAdapter;
    private MsgSendWidget msgSendWidget;
    private ImageView ivExpertHome;

    public String topic;
    private String avatarUrl;
    public String name;
    private int chatUserId = 0;
    private int chatUserIdentity;
    private long messageId = -1;
    private long lastMsgId = 0;
    private List<MessageEntity> localList;
    private long localMaxMsgId = 0;
    private boolean needInsertDb = false;
    private boolean needClearDb = false;

    //----------Order View-----------
    private MultiDirectionSlidingDrawer mDrawer;
    private View orderHeadContainer;
    private LinearLayout orderContent;

    private OrderDetailRespVo detailVo = null;
    private TextView tvOrderNo;
    private TextView tvOrderTime;

    private String orderStatus;
    private String title;
    private int orderId = 0;
    private String userComment;
    private boolean isFirstAdd = true;
    private ChatOrderUserView1To4 view1To4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_TOPIC)) {
                topic = intent.getStringExtra(IntentKey.INTENT_KEY_CHAT_TOPIC);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL)) {
                avatarUrl = intent.getStringExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_NAME)) {
                name = intent.getStringExtra(IntentKey.INTENT_KEY_CHAT_NAME);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_ID)) {
                chatUserId = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY)) {
                chatUserIdentity = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, 1);
            }
        }
        messageControl = new MessageControl();
        setControl(messageControl);
        messageControl.getHistoryMessage(topic, 0, 1);
        MqttUtils.setChatCallback(this, hashCode());
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_message;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        tvTitle = (TextView) view.findViewById(R.id.id_title);
        if (!TextUtils.isEmpty(name)) {
            if (name.equals("eric")) {
                title = "大牛家";
            } else {
                title = name;
            }
        } else {
            title = topic;
        }
        tvTitle.setText(title);
        ivExpertHome = (ImageView) view.findViewById(R.id.id_expert_home);
        if (chatUserId == SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)) {
            ivExpertHome.setVisibility(View.GONE);
        } else {
            ivExpertHome.setVisibility(View.VISIBLE);
            ivExpertHome.setOnClickListener(this);
        }

        msgSendWidget = (MsgSendWidget) view.findViewById(R.id.msg_send_widget);
        msgSendWidget.setMsgSendListener(messageControl);
        msgSendWidget.setFragment(this);

        messageList = (XListView) view.findViewById(R.id.xlv_chat_detail);
        messageList.setXListViewListener(this);
        messageList.setPullRefreshEnable(true);
        messageList.setPullLoadEnable(false);

        messageList.setOnItemClickListener(this);
        messageAdapter = new MessageAdapter(getActivity());
        messageAdapter.setAvatarUrl(avatarUrl);
        messageAdapter.setOtherSideId(chatUserId);
        messageAdapter.setOtherSideEntity(chatUserIdentity);
        messageAdapter.setMessageFragment(MessageFragment.this);
        messageList.setAdapter(messageAdapter);

        mDrawer = (MultiDirectionSlidingDrawer) view.findViewById(R.id.drawer);
        orderHeadContainer = view.findViewById(R.id.order_container);
        orderHeadContainer.setOnClickListener(this);
        orderHeadContainer.setVisibility(View.GONE);
        orderContent = (LinearLayout) view.findViewById(R.id.order_content);

        tvOrderNo = (TextView) view.findViewById(R.id.order_no);
        tvOrderTime = (TextView) view.findViewById(R.id.order_time);

        messageControl.getOrderInfoRequest(topic);
    }

    @Override
    public void onPause() {
        super.onPause();
        messageAdapter.clearVoice();
    }

    @Override
    public void onDestroy() {
        messageControl.clearUnRead(topic);
        messageControl.setSendMsgFail();
        MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_PUSH_NEW_MESSAGE);
        MqttUtils.setChatCallback(null, hashCode());
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == ivExpertHome) {
            if (chatUserIdentity == 2) {
                Intent intent = new Intent(getActivity(), ActExpertHome.class);
                intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, chatUserId);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), ActUserHome.class);
                intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, chatUserId);
                startActivity(intent);
            }
        } else if (v == orderHeadContainer) {
            if (!mDrawer.isOpened()) {
                tvTitle.setText(title + "(" + orderStatus + ")");
                orderHeadContainer.setVisibility(View.GONE);
                messageControl.getOrderInfoRequest(topic);
                mDrawer.animateOpen();
                msgSendWidget.setEditable(false);
            }
        }
    }

    public void performOrderHeadContainer() {
//  orderHeadContainer.performClick();（zp）不用这句代码的原因：点击事件触发会有声音，加上点击条目的声音，会出现两次声音
        if (!mDrawer.isOpened()) {
            tvTitle.setText(title + "(" + orderStatus + ")");
            orderHeadContainer.setVisibility(View.GONE);
            messageControl.getOrderInfoRequest(topic);
            mDrawer.animateOpen();
            msgSendWidget.setEditable(false);
        }
    }

    @Override
    public void onLoadMore() {
        // Nothing to do
    }

    @Override
    public void onRefresh() {
        if (!MqttUtils.isConnected()) {
            showShortToast("未连接服务器");
            messageList.stopRefresh();
            return;
        }
        needInsertDb = true;
        if (messageId == 0) {
            showShortToast("没有更多记录了");
            messageList.stopRefresh();
            return;
        }
        messageControl.getHistoryMessage(topic, messageId, 20);
    }

    public void showMessageList(List<MessageEntity> sourceData) {
        if (ListUtils.isEmpty(sourceData)) {
            return;
        }
        LogUtil.d("zp_test","source size: " + sourceData.size());
        this.localList = sourceData;
        messageId = sourceData.get(0).getMessageId() - 1;
        lastMsgId = DatabaseManager.getInstance().getMaxMsgIdFromMsgFrom(topic);
        if (isFirstAdd) {
            messageControl.sendConfirmReadMsg(topic, lastMsgId);
            isFirstAdd = false;
        }
        final int offset = sourceData.size() - messageAdapter.getCount();
        final int firstVisiblePosition = messageList.getFirstVisiblePosition();

        setListToArrayAdapter(sourceData, messageAdapter);
        messageAdapter.setList(sourceData);

        messageList.post(new Runnable() {
            @Override
            public void run() {
                if (offset > 0 && firstVisiblePosition == 0) {
                    messageList.setSelection(firstVisiblePosition + offset);
                }
            }
        });
    }

    public void scrollToBottom() {
        messageList.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageList.setSelection(messageAdapter.getCount() - 1);
            }
        }, 200);
    }

    public boolean isLastItem() {
        return messageList.getLastVisiblePosition() == messageList.getAdapter().getCount() - 1;
    }

    @Override
    public void onSend(boolean isSuccess, String args) {
        LogUtil.d("ZLOVE", "isSuccess---" + isSuccess);
        ContentValues contentValues = new ContentValues();
        if (!isSuccess) {
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_STATE, 2);
            List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
            sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_MSG_CODE + "=?", new String[]{args}, contentValues));
            DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
            executor.execute(executor);
        }
    }

    @Override
    public void onRecvChat(MessageEntity dataItem) {
        if (dataItem.getFrom().equals(topic)) {
            messageControl.sendConfirmReadMsg(topic, dataItem.getMessageId());
            if (messageAdapter.getCount() - messageList.getFirstVisiblePosition() < 20) {
                scrollToBottom();
            }
        }
        if (dataItem.getContent().getCtype() == IMMsgFlag.MSG_CHAT_EVENT && mDrawer.isOpened()) {
            messageControl.getOrderInfoRequest(topic);
        }
    }

    @Override
    public void onRecvChatConfirm(int chatId, long time) {

    }

    @Override
    public void onDragHistoryMsg(String strMsg) {
        messageList.stopRefresh();
        if (TextUtils.isEmpty(strMsg)) {
            return;
        }

        MessageVo messageVo = JSON.parseObject(strMsg, MessageVo.class);
        List<MessageEntity> data = messageVo.getData();
        LogUtil.d("zp_test","onDragHistoryMsg: " + data.size() + " thread name: " + Thread.currentThread().getName());
        if (!ListUtils.isEmpty(data)) {
            if (needInsertDb) {
                if (needClearDb) {
                    LogUtil.d("ZLOVE", "Clear Database because client and server is different");
                    needClearDb = false;
                    DatabaseManager.getInstance().clearMessageTable(topic);
                }
                List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
                for (MessageEntity entity : data) {
                    if (entity.getContent().getCtype() == IMMsgFlag.MSG_CHAT_VOICE) {
                        handlerVoiceMsg(entity);
                    }
                    entity.setIsRead(entity.isRead() ? 1 : 0);
                    entity.setMsgState(1);
                    entity.setIsPlay(1);
                    sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(null, null, entity.getContentValues(entity)));
                }
                DatabaseInsertExecutor executor = new DatabaseInsertExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
                executor.execute(executor);
                needInsertDb = false;
                return;
            }

            long maxMsgId = data.get(0).getMessageId();
            localMaxMsgId = DatabaseManager.getInstance().getMaxMsgId(topic);
            LogUtil.d("ZLOVE", "localMaxMsgId --- " + localMaxMsgId + "  maxMsgId--- " + maxMsgId);

            if (maxMsgId > localMaxMsgId) {
                needInsertDb = true;
                needClearDb  = true;
                messageControl.getHistoryMessage(topic, maxMsgId, 20);
            }

        }
    }

    private void handlerVoiceMsg(final MessageEntity messageEntity) {
        String url = messageEntity.getContent().getUrl();
        String[] str = url.split("/");
        File file = new File(FileStorage.APP_VOICE_DIR, str[str.length - 1]);
        if (file.exists() && file.length() > 0) {
            String sqlUrl = QiniuUtil.getResUrl(str[str.length - 1]);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IS_DOWN, 1);
            List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
            sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_URL + "=?", new String[]{sqlUrl}, contentValues));
            DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
            executor.execute(executor);
            return;
        }
        QiniuDownloadUtil.download(file, url, new QiniuDownloadUtil.DownloadCallback() {
            @Override
            public void downloadComplete(String path) {
                String[] str = path.split("/");
                String sqlUrl = QiniuUtil.getResUrl(str[str.length - 1]);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseConstants.MessageColumn.COLUMN_NAME_IS_DOWN, 1);
                List<BaseDatabaseExecutor.SQLSentence> sqlSentences = new ArrayList<>();
                sqlSentences.add(new BaseDatabaseExecutor.SQLSentence(DatabaseConstants.MessageColumn.COLUMN_NAME_URL + "=?", new String[]{sqlUrl}, contentValues));
                DatabaseUpdateExecutor executor = new DatabaseUpdateExecutor(DatabaseConstants.Tables.TABLE_NAME_MESSAGE, sqlSentences, DaniujiaUris.URI_MESSAGE);
                executor.execute(executor);
                messageAdapter.joinContinuePlay();
            }

            @Override
            public void onProgress(int max, int progress) {

            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    public void setData(OrderDetailRespVo detailVo) {
        if (detailVo == null) {
            orderHeadContainer.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(detailVo.getOrderUser())) {
            orderHeadContainer.setVisibility(View.GONE);
            return;
        }
        if (!mDrawer.isOpened()) {
            orderHeadContainer.setVisibility(View.VISIBLE);
        }
        this.detailVo = detailVo;
        this.orderId = detailVo.getOrderId();
        this.userComment = detailVo.getComment();
        int status = detailVo.getStatus();

        if (status == 0) {
            orderStatus = "待确认";
        } else if (status == 1) {
            orderStatus = "待支付";
        } else if (status == 2) {
            orderStatus = "进行中";
        } else if (status == 3) {
            orderStatus = "待评价";
        } else if (status == 10) {
            orderStatus = "已取消";
        } else if (status == 11) {
            orderStatus = "已拒绝";
        } else if (status == 12) {
            orderStatus = "已完成";
        }

        if (mDrawer.isOpened()) {
            tvTitle.setText(title + "(" + orderStatus + ")");
        } else {
            tvTitle.setText(title);
        }
        tvOrderNo.setText("订单编号:" + detailVo.getOrderNo());
        tvOrderTime.setText("(" + TimeUtils.getTime(detailVo.getCreateTime() * 1000, TimeUtils.DATE_FORMAT_ORDER) + ")");
        orderContent.removeAllViews();
        if (status == 12) {
            if (detailVo.getOrderUser().equals("asker")) {
                ChatOrderUserView5 view5 = new ChatOrderUserView5(getActivity(), this, true);
                view5.initData(detailVo);
                orderContent.addView(view5);
            } else {
                ChatOrderExpertView5 view5 = new ChatOrderExpertView5(getActivity(), this, true);
                view5.initData(detailVo);
                orderContent.addView(view5);
            }
        } else {
            if (detailVo.getOrderUser().equals("asker")) {
                view1To4 = new ChatOrderUserView1To4(getActivity(), this, true);
                view1To4.initData(detailVo);
                orderContent.addView(view1To4);
            } else {
                ChatOrderExpertView1To4 view1To4 = new ChatOrderExpertView1To4(getActivity(), this, true);
                view1To4.initData(detailVo);
                orderContent.addView(view1To4);
            }
        }
    }

    @Override
    public void hideOrder() {
        tvTitle.setText(title);
        mDrawer.animateClose();
        orderHeadContainer.setVisibility(View.VISIBLE);
        msgSendWidget.setEditable(true);
    }

    @Override
    public void userCancelOrder() {
        BaseMsgDlg dialog = new BaseMsgDlg(getActivity());
        dialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        dialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        dialog.setTitle("温馨提示");
        dialog.setMsg("确定要取消这个预约中的订单吗？");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                messageControl.cancelOrderRequest(topic, detailVo.getOrderId());
            }
        });
        dialog.show();
    }

    @Override
    public void expertRejectOrder() {
        Intent intent = new Intent(getActivity(), ActRejectOrder.class);
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
        startActivityForResult(intent, IntentKey.REQ_CODE_REJECT_ORDER);
    }

    @Override
    public void expertConfirmOrder() {
        messageControl.expertConfirmOrderRequest(topic, orderId);
    }

    @Override
    public void expertReplyOrder() {
        Intent intent = new Intent(getActivity(), ActExpertReply.class);
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
        intent.putExtra(IntentKey.INTENT_KEY_USER_COMMENT, userComment);
        startActivityForResult(intent, IntentKey.REQ_CODE_REPLY_ORDER);
    }

    @Override
    public void userPayOrder() {
        if (detailVo == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), ActPayDetail.class);
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, detailVo.getOrderId());
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_NUM, detailVo.getOrderNo());
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_SERVICE_TYPE, detailVo.getServiceType());
        intent.putExtra(IntentKey.INTENT_KEY_PER_PRICE, detailVo.getServicePrice());
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_TIME, detailVo.getServiceCnt());
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_TOTAL_PRICE, detailVo.getTotalPrice());
        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_NAME, detailVo.getName());
        startActivityForResult(intent, IntentKey.REQ_CODE_USER_PAY_ORDER);
    }

    @Override
    public void expertFinishOrder() {
        BaseMsgDlg dialog = new BaseMsgDlg(getActivity());
        dialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        dialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        dialog.setTitle("温馨提示");
        dialog.setMsg("结束约谈后，将不能再发送信息，确认结束吗？");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                messageControl.expertFinishOrderRequest(topic, orderId);
            }
        });
        dialog.show();
    }

    @Override
    public void userCommentOrder() {
        if (detailVo == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), ActUserOrderComment.class);
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, detailVo.getOrderId());
        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_NAME, detailVo.getName());
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_SERVICE_TYPE, detailVo.getServiceType());
        startActivityForResult(intent, IntentKey.REQ_CODE_USER_COMMENT_ORDER);
    }

    @Override
    public void gotoChat() {
    }


    @Override
    public void expertGotoCall() {
        if (detailVo == null) {
            return;
        }
        messageControl.callServer(orderId, detailVo.getUserId());
        CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.call_phone_tip), "知道了", true);
        callTipDialog.showdialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_REJECT_ORDER) {
            if (data != null) {
                messageControl.getOrderInfoRequest(topic);
            }
        } else if (requestCode == IntentKey.REQ_CODE_REPLY_ORDER) {
            if (data != null) {
                messageControl.getOrderInfoRequest(topic);
            }
        } else if (requestCode == IntentKey.REQ_CODE_USER_PAY_ORDER) {
            if (data != null) {
                messageControl.getOrderInfoRequest(topic);
            }
        } else if (requestCode == IntentKey.REQ_CODE_USER_COMMENT_ORDER) {
            if (data != null) {
                messageControl.getOrderInfoRequest(topic);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UIUtil.hideKeyboard(getActivity());
        msgSendWidget.restore();
    }

    @Override
    public void modify(String content, int modifyType) {
        if (content.length() > 500) {
            showShortToast("不能超过500字符");
            return;
        }
        if (modifyType == 1) {
            detailVo.setProfile(content);
        } else if (modifyType == 2) {
            detailVo.setQuesDesc(content);
        }
        view1To4.reSetProfileOrQues(detailVo.getProfile(), detailVo.getQuesDesc());
        messageControl.modifyQuesOrProfile(orderId, content, modifyType);
    }

    @Override
    public void gotoExpertMain() {
    }

}
