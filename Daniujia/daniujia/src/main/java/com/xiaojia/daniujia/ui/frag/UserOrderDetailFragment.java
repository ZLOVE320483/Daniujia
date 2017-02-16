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
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.ui.act.ActPayDetail;
import com.xiaojia.daniujia.ui.act.ActUserOrderComment;
import com.xiaojia.daniujia.ui.control.UserOrderDetailControl;
import com.xiaojia.daniujia.ui.views.ChatOrderUserView1To4;
import com.xiaojia.daniujia.ui.views.ChatOrderUserView5;
import com.xiaojia.daniujia.ui.views.HideChatOrderListener;

/**
 * Created by Administrator on 2016/5/26.
 */
public class UserOrderDetailFragment extends BaseFragment implements HideChatOrderListener, MqttMsgCallbackHandler.MqttChatCallback {

    private UserOrderDetailControl control;
    private TextView tvTitle;
    private LinearLayout orderContainer;

    private int orderId = 0;
    private OrderDetailRespVo detailVo;
    private boolean isOrderStateChange = false;
    private ChatOrderUserView1To4 view1To4;
    private ChatOrderUserView5 view5;
    private String knowledgeState = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new UserOrderDetailControl();
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
        if (!isAdded()) {
            return;
        }
        this.detailVo = detailVo;
        int orderStatus = detailVo.getStatus();
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
            view5 = new ChatOrderUserView5(getActivity(), this, false);
            view5.initData(detailVo);
            orderContainer.addView(view5);
        } else {
            view1To4 = new ChatOrderUserView1To4(getActivity(), this, false);
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
        BaseMsgDlg dialog = new BaseMsgDlg(getActivity());
        dialog.setBtnName(DialogInterface.BUTTON_POSITIVE, R.string.confirm);
        dialog.setBtnName(DialogInterface.BUTTON_NEGATIVE, R.string.cancel);
        dialog.setTitle("温馨提示");
        dialog.setMsg("确定要取消这个预约中的订单吗？");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnClickListener(new BaseDialog.DialogClickListener() {
            public void onOK() {
                isOrderStateChange = true;
                control.cancelOrderRequest(orderId);
            }
        });
        dialog.show();
    }

    @Override
    public void expertRejectOrder() {
        //Nothing to do
    }

    @Override
    public void expertConfirmOrder() {
        //Nothing to do
    }

    @Override
    public void expertReplyOrder() {
        //Nothing to do
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
        // Nothing to do
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
        //Nothing to do
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_USER_PAY_ORDER) {
            if (data != null) {
                isOrderStateChange = true;
                control.requestOrderDetail(orderId);
            }
        } else if (requestCode == IntentKey.REQ_CODE_USER_COMMENT_ORDER) {
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
        control.modifyQuesOrProfile(orderId, content, modifyType);
    }

    @Override
    public void gotoExpertMain() {
        Intent intent = new Intent(getActivity(), ActExpertHome.class);
        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, detailVo.getConsultantId());
        startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
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
