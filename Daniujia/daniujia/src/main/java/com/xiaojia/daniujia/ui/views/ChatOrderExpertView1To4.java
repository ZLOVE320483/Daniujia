package com.xiaojia.daniujia.ui.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.children.ChildrenShareWebActivity;
import com.xiaojia.daniujia.dlgs.KnowledgeShareTipDlg;
import com.xiaojia.daniujia.domain.resp.OrderDetailRespVo;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.TimeUtils;

/**
 * Created by ZLOVE on 2016/5/24.
 */
public class ChatOrderExpertView1To4 extends LinearLayout implements View.OnClickListener {
    private static final String SHARE_ALL = "all";
    private static final String SHARE_HALF = "half";

    private Context mContext;

    private View arrowView;
    private HideChatOrderListener listener;

    private ImageView ivStep1;
    private ImageView ivStep2;
    private ImageView ivStep3;
    private ImageView ivStep4;
    private ImageView ivStep5;

    private TextView tvOrderNum;
    private TextView tvCreateTime;
    private RoundedImageView userAvatar;
    private TextView userName;
    private TextView consultType;
    private TextView perPrice;
    private TextView tvServiceTime;
    private TextView tvHasCall;
    private TextView totalPrice;
    private TextView tvRejectTag;
    private TextView tvOrderDesc;
    private TextView cancelView;
    private View containerView;
    private TextView confirmView;
    private TextView callView;
    private TextView tvMyAsk;
    private ExpandableTextView tvMyIntro;

    private boolean isFromChat = false;
    private int orderStatus = 0;

    private String questionDesc;
    private String userIntro;

    private String share;
    private KnowledgeShareTipDlg tipDlgHalf = null;
    private KnowledgeShareTipDlg tipDlgAll = null;
    private ImageView ivHelpStudent;
    private String knowledgeState = "";

    public void setKnowledgeState(String knowledgeState) {
        this.knowledgeState = knowledgeState;
    }

    private KnowledgeShareTipDlg.OnJoinClickListener knowListener = new KnowledgeShareTipDlg.OnJoinClickListener() {
        @Override
        public void onClickJoin() {
            Intent intent = new Intent(mContext, ChildrenShareWebActivity.class);
            intent.putExtra(IntentKey.INTENT_KEY_JOIN_STATE, knowledgeState);
            mContext.startActivity(intent);
        }
    };

    public ChatOrderExpertView1To4(Context context, HideChatOrderListener listener, boolean isFromChat) {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.isFromChat = isFromChat;
        View view = LayoutInflater.from(context).inflate(R.layout.view_expert_order_detail, null);
        addView(view);
        initView(view);
    }

    public ChatOrderExpertView1To4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatOrderExpertView1To4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(View view) {
        arrowView = view.findViewById(R.id.id_hide_order);
        arrowView.setOnClickListener(this);

        if (isFromChat) {
            arrowView.setVisibility(VISIBLE);
        } else {
            arrowView.setVisibility(INVISIBLE);
        }

        tvOrderNum = (TextView) view.findViewById(R.id.order_num);
        tvCreateTime = (TextView) view.findViewById(R.id.create_time);
        ivStep1 = (ImageView) view.findViewById(R.id.step1);
        ivStep2 = (ImageView) view.findViewById(R.id.step2);
        ivStep3 = (ImageView) view.findViewById(R.id.step3);
        ivStep4 = (ImageView) view.findViewById(R.id.step4);
        ivStep5 = (ImageView) view.findViewById(R.id.step5);

        userAvatar = (RoundedImageView) view.findViewById(R.id.user_avatar);
        userName = (TextView) view.findViewById(R.id.user_name);
        consultType = (TextView) view.findViewById(R.id.consult_type);
        perPrice = (TextView) view.findViewById(R.id.per_price);
        totalPrice = (TextView) view.findViewById(R.id.total_price);
        tvServiceTime = (TextView) view.findViewById(R.id.total_time);
        tvHasCall = (TextView) view.findViewById(R.id.has_call);
        tvRejectTag = (TextView) view.findViewById(R.id.reject_tag);
        tvOrderDesc = (TextView) view.findViewById(R.id.order_desc);
        containerView = view.findViewById(R.id.container2);
        cancelView = (TextView) view.findViewById(R.id.id_cancel);
        cancelView.setOnClickListener(this);
        confirmView = (TextView) view.findViewById(R.id.id_confirm);
        confirmView.setOnClickListener(this);
        callView = (TextView) view.findViewById(R.id.id_call);
        callView.setOnClickListener(this);


        ivHelpStudent = (ImageView) view.findViewById(R.id.help_student_icon);
        ivHelpStudent.setOnClickListener(this);

        tvMyAsk = (TextView) view.findViewById(R.id.id_my_ask);
        tvMyIntro = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
    }

    public void initData(OrderDetailRespVo detailVo) {
        if (detailVo == null) {
            return;
        }
        orderStatus = detailVo.getStatus();
        questionDesc = detailVo.getQuesDesc();
        userIntro = detailVo.getProfile();

        if (detailVo.getCharity() == null) {
            ivHelpStudent.setVisibility(View.GONE);
        } else {
            ivHelpStudent.setVisibility(View.VISIBLE);
            share = detailVo.getCharity().getShare();
        }

        int serviceType = detailVo.getServiceType();
        if (serviceType == 1) {
            consultType.setText("图文约谈:");
            perPrice.setText(detailVo.getServicePrice() + "元/次");
            tvServiceTime.setVisibility(GONE);
            tvHasCall.setVisibility(GONE);
        } else if (serviceType == 2) {
            consultType.setText("电话约谈:");
            perPrice.setText(detailVo.getServicePrice() + "元/分钟");
            tvServiceTime.setVisibility(VISIBLE);
            tvServiceTime.setText("共计:" + detailVo.getServiceCnt() + "分钟");
            tvHasCall.setVisibility(VISIBLE);
            tvHasCall.setText("已通话:" + detailVo.getDuration() + "分钟");
        } else if (serviceType == 3) {
            consultType.setText("线下约谈:");
            perPrice.setText(detailVo.getServicePrice() + "元/小时");
            tvServiceTime.setVisibility(VISIBLE);
            tvServiceTime.setText("共计:" + detailVo.getServiceCnt() + "小时");
            tvHasCall.setVisibility(GONE);
        }

        if (orderStatus == 0) {
            ivStep1.setImageResource(R.drawable.order_state_1_white);
            tvOrderDesc.setText("用户提出约谈，请仔细查看用户提问后，给予确认或拒绝");
            cancelView.setText("拒  绝");
            confirmView.setText("确  认");
        } else if (orderStatus == 1) {
            ivStep2.setImageResource(R.drawable.order_state_2_white);
            tvOrderDesc.setText("您已确认订单，等待用户支付");
            cancelView.setVisibility(INVISIBLE);
            confirmView.setVisibility(INVISIBLE);
            containerView.setVisibility(GONE);
        } else if (orderStatus == 2) {
            ivStep3.setImageResource(R.drawable.order_state_3_white);
            if (serviceType == 1) {
                tvOrderDesc.setText("用户已支付，您可以给用户发送消息，回答用户的问题");
            } else if (serviceType == 2) {
                tvOrderDesc.setText("用户已支付，您可以点击下方[拨打电话]按钮回答用户的问题");
            } else if (serviceType == 3) {
                tvOrderDesc.setText("用户已支付，您可以给用户发送消息，确定约谈时间和地点");
            }
            cancelView.setText("结束约谈");
            confirmView.setText("聊天界面");
            if (isFromChat) {
                confirmView.setVisibility(GONE);
            }
            if (serviceType == 2) {
                callView.setVisibility(VISIBLE);
            }
        } else if (orderStatus == 3) {
            ivStep4.setImageResource(R.drawable.order_state_4_white);
            tvOrderDesc.setText("约谈已结束,等待用户评价");
            cancelView.setVisibility(INVISIBLE);
            confirmView.setVisibility(INVISIBLE);
            containerView.setVisibility(GONE);
        } else if (orderStatus == 10) {
            ivStep1.setImageResource(R.drawable.order_state_6_white);
            tvOrderDesc.setText("用户已取消约谈");
            cancelView.setVisibility(View.INVISIBLE);
            confirmView.setVisibility(INVISIBLE);
            containerView.setVisibility(GONE);
        } else if (orderStatus == 11) {
            ivStep1.setImageResource(R.drawable.order_state_6_white);
            tvRejectTag.setVisibility(VISIBLE);
            tvOrderDesc.setText("您已拒绝用户的约谈");
            tvRejectTag.setText("拒绝理由:" + detailVo.getRejectReason());
            cancelView.setVisibility(View.INVISIBLE);
            confirmView.setVisibility(INVISIBLE);
            containerView.setVisibility(GONE);
        } else if (orderStatus == 12) {
            ivStep5.setImageResource(R.drawable.order_state_5_white);
            containerView.setVisibility(GONE);
        }
        tvOrderNum.setText(detailVo.getOrderNo());
        tvCreateTime.setText("(" + TimeUtils.getTime(detailVo.getCreateTime() * 1000, TimeUtils.DATE_FORMAT_ORDER) + ")");
        if (!TextUtils.isEmpty(detailVo.getImgurl())) {
            Picasso.with(mContext).load(detailVo.getImgurl()).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(userAvatar);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(userAvatar);
        }
        if (!TextUtils.isEmpty(detailVo.getName())) {
            userName.setText(detailVo.getName());
        } else {
            userName.setText(detailVo.getUsername());
        }

        totalPrice.setText(detailVo.getTotalPrice());
        tvMyAsk.setText(questionDesc);
        tvMyIntro.setText(userIntro);
    }

    @Override
    public void onClick(View v) {
        if (v == arrowView) {
            if (listener != null) {
                listener.hideOrder();
            }
        } else if (v == cancelView) {
            if (orderStatus == 0) {
                if (listener != null) {
                    listener.expertRejectOrder();
                }
            } else if (orderStatus == 2) {
                if (listener != null) {
                    listener.expertFinishOrder();
                }
            }
        } else if (v == confirmView) {
            if (orderStatus == 0) {
                if (listener != null) {
                    listener.expertConfirmOrder();
                }
            } else if (orderStatus == 2) {
                if (listener != null) {
                    listener.gotoChat();
                }
            }
        } else if (v == callView) {
            if (listener != null) {
                listener.expertGotoCall();
            }
        } else if (v == ivHelpStudent) {
            if (share != null && !share.equals("")) {
                if (share.equals(SHARE_ALL)) {
                    if (tipDlgAll == null) {
                        tipDlgAll = new KnowledgeShareTipDlg((Activity) mContext,
                                "我正在参与知识“童”享公益助学活动，全部约谈费用将捐助用于改善安徽省金寨县沙堰小学同学们的生活和学习条件。",
                                "了解活动", "知道了");
                        tipDlgAll.setJoinClickListener(knowListener);
                    }

                    tipDlgAll.showDialog();
                } else if (share.equals(SHARE_HALF)) {
                    if (tipDlgHalf == null) {
                        tipDlgHalf = new KnowledgeShareTipDlg((Activity) mContext,
                                "我正在参与知识“童”享公益助学活动，一半约谈费用将捐助用于改善安徽省金寨县沙堰小学同学们的生活和学习条件。",
                                "了解活动", "知道了");
                        tipDlgHalf.setJoinClickListener(knowListener);
                    }
                    tipDlgHalf.showDialog();
                }
            }
        }
    }
}
