package com.xiaojia.daniujia.ui.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.IMMsgFlag;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.domain.resp.ChildShareRespVo;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.domain.resp.OrderDetailRespVo;
import com.xiaojia.daniujia.mqtt.MqttMsgCallbackHandler;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.ui.act.ActExpertReply;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.ui.act.ActRejectOrder;
import com.xiaojia.daniujia.ui.control.ExpertOrderDetailControl;
import com.xiaojia.daniujia.ui.views.ChatOrderExpertView1To4;
import com.xiaojia.daniujia.ui.views.ChatOrderExpertView5;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.ui.views.HideChatOrderListener;

/**
 * Created by ZLOVE on 2016/5/29.
 */
public class ExpertOrderDetailFragment extends BaseFragment implements HideChatOrderListener, MqttMsgCallbackHandler.MqttChatCallback {

    private ExpertOrderDetailControl control;
    private TextView tvTitle;
    private LinearLayout orderContainer;

    private int orderId = 0;
    private String userComment;
    private OrderDetailRespVo detailVo;
    private ChatOrderExpertView5 view5;
    private ChatOrderExpertView1To4 view1To4;
    private String knowledgeState = "";

    private boolean isOrderStateChange = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new ExpertOrderDetailControl();
        setControl(control);
        MqttUtils.setChatCallback(this, hashCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MqttUtils.setChatCallback(null, hashCode());
        super.onDestroy();
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_first_graphic_order;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
        }

        view.findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(IntentKey.INTENT_KEY_ORDER_STATUS_CHANGE, isOrderStateChange);
                finishActivity(result);
            }
        });
        tvTitle = (TextView) view.findViewById(R.id.id_title);
        orderContainer = (LinearLayout) view.findViewById(R.id.container);

        control.requestOrderDetail(orderId);
    }

    public void setData(OrderDetailRespVo detailVo) {
        if (detailVo == null) {
            return;
        }
        this.detailVo = detailVo;
        int orderStatus = detailVo.getStatus();
        userComment = detailVo.getComment();
        if (orderStatus == 0) {
            tvTitle.setText("待确认");
        } else if (orderStatus == 1) {
            tvTitle.setText("待支付");
        } else if (orderStatus == 2) {
            tvTitle.setText("进行中");
        } else if (orderStatus == 3) {
            tvTitle.setText("待评价");
        } else if (orderStatus == 10) {
            tvTitle.setText("已取消");
        } else if (orderStatus == 11) {
            tvTitle.setText("已拒绝");
        } else if (orderStatus == 12) {
            tvTitle.setText("已完成");
        }
        orderContainer.removeAllViews();
        if (orderStatus == 12) {
            view5 = new ChatOrderExpertView5(getActivity(), this, false);
            view5.initData(detailVo);
            orderContainer.addView(view5);
        } else {
            view1To4 = new ChatOrderExpertView1To4(getActivity(), this, false);
            view1To4.initData(detailVo);
            orderContainer.addView(view1To4);
        }
        control.getKnowledgeIconState();
    }

    @Override
    public void hideOrder() {
        // Nothing to do
    }

    @Override
    public void userCancelOrder() {
        // Nothing to do
    }

    @Override
    public void expertRejectOrder() {
        Intent intent = new Intent(getActivity(), ActRejectOrder.class);
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
        startActivityForResult(intent, IntentKey.REQ_CODE_REJECT_ORDER);
    }

    @Override
    public void expertConfirmOrder() {
        isOrderStateChange = true;
        control.expertConfirmOrderRequest(orderId);
    }

    @Override
    public void userPayOrder() {
        // Nothing to do
    }

    @Override
    public void expertReplyOrder() {
        Intent intent = new Intent(getActivity(), ActExpertReply.class);
        intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
        intent.putExtra(IntentKey.INTENT_KEY_USER_COMMENT, userComment);
        startActivityForResult(intent, IntentKey.REQ_CODE_REPLY_ORDER);
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
                isOrderStateChange = true;
                control.expertFinishOrderRequest(orderId);
            }
        });
        dialog.show();
    }

    @Override
    public void userCommentOrder() {
        // Nothing to do
    }

    @Override
    public void gotoChat() {
        if (detailVo == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), ActMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, detailVo.getUsername());
        startActivity(intent);
    }

    @Override
    public void expertGotoCall() {
        if (detailVo == null) {
            return;
        }
        control.callServer(orderId, detailVo.getUserId());
        CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.call_phone_tip), "知道了", true);
        callTipDialog.showdialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_REJECT_ORDER) {
            if (data != null) {
                isOrderStateChange = true;
                control.requestOrderDetail(orderId);
            }
        } else if (requestCode == IntentKey.REQ_CODE_REPLY_ORDER) {
            if (data != null) {
                isOrderStateChange = true;
                control.requestOrderDetail(orderId);
            }
        }
    }


    @Override
    public void onSend(boolean isSuccess, String args) {

    }

    @Override
    public void onRecvChat(MessageEntity dataItem) {
        if (dataItem.getContent().getCtype() == IMMsgFlag.MSG_CHAT_EVENT && detailVo != null && dataItem.getFrom().equals(detailVo.getUsername())) {
            control.requestOrderDetail(orderId);
        }
    }

    @Override
    public void onRecvChatConfirm(int chatId, long time) {
    }

    @Override
    public void onDragHistoryMsg(String strMsg) {
    }

    @Override
    public void modify(String content, int modifyType) {
    }

    @Override
    public void gotoExpertMain() {
    }

    public void onSetKnowledgeState(ChildShareRespVo respVo){
        if (respVo == null || TextUtils.isEmpty(respVo.getCharityExpert())) {
            knowledgeState = "notJoin";
        } else {
            knowledgeState = respVo.getCharityExpert();
        }
        if (view5 != null) {
            view5.setKnowledgeState(knowledgeState);
        }
        if (view1To4 != null) {
            view1To4.setKnowledgeState(knowledgeState);
        }
    }
}
