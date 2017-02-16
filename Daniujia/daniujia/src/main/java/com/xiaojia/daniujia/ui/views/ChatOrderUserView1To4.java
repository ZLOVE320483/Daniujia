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
import com.xiaojia.daniujia.dlgs.AlertMsgDlg;
import com.xiaojia.daniujia.dlgs.KnowledgeShareTipDlg;
import com.xiaojia.daniujia.domain.resp.OrderDetailRespVo;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.TimeUtils;

/**
 * Created by ZLOVE on 2016/5/24.
 */
public class ChatOrderUserView1To4 extends LinearLayout implements View.OnClickListener, AlertMsgDlg.ModifyListener {
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
    private RoundedImageView expertAvatar;
    private TextView expertName;
    private TextView expertPosition;
    private TextView consultType;
    private TextView perPrice;
    private TextView totalPrice;
    private TextView tvServiceTime;
    private TextView tvHasCall;
    private TextView tvRejectTag;
    private TextView tvOrderDesc;
    private View containerView;
    private TextView cancelView;
    private TextView confirmView;
    private TextView tvMyAsk;
    private ExpandableTextView tvMyIntro;
    private View modifyAskView;
    private View modifyIntroView;

    private boolean isFromChat = false;
    private int orderStatus = 0;
    private String questionDesc;
    private String userIntro;
    private int modifyType = 1;
    private AlertMsgDlg dialog;

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

    public ChatOrderUserView1To4(Context context, HideChatOrderListener listener, boolean isFromChat) {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.isFromChat = isFromChat;
        View view = LayoutInflater.from(context).inflate(R.layout.view_user_order_detail, null);
        addView(view);
        initView(view);
    }

    public ChatOrderUserView1To4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatOrderUserView1To4(Context context, AttributeSet attrs, int defStyleAttr) {
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

        expertAvatar = (RoundedImageView) view.findViewById(R.id.expert_header);
        expertAvatar.setOnClickListener(this);
        expertName = (TextView) view.findViewById(R.id.expert_name);
        expertPosition = (TextView) view.findViewById(R.id.position);
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

        ivHelpStudent = (ImageView) view.findViewById(R.id.help_student_icon);
        ivHelpStudent.setOnClickListener(this);

        tvMyAsk = (TextView) view.findViewById(R.id.id_my_ask);
        tvMyIntro = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        modifyAskView = view.findViewById(R.id.id_modify_ask);
        modifyIntroView = view.findViewById(R.id.id_modify_info);

        modifyAskView.setOnClickListener(this);
        modifyIntroView.setOnClickListener(this);
    }

    public void initData(OrderDetailRespVo detailVo) {
        if (detailVo == null) {
            return;
        }

        if (detailVo.getCharity() == null) {
            ivHelpStudent.setVisibility(View.GONE);
        } else {
            ivHelpStudent.setVisibility(View.VISIBLE);
            share = detailVo.getCharity().getShare();
        }

        orderStatus = detailVo.getStatus();
        questionDesc = detailVo.getQuesDesc();
        userIntro = detailVo.getProfile();
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
            tvOrderDesc.setText("约谈已提交给专家,正在等待专家确认");
            cancelView.setText("取  消");
            confirmView.setVisibility(View.GONE);
            modifyAskView.setVisibility(VISIBLE);
            modifyIntroView.setVisibility(VISIBLE);
        } else if (orderStatus == 1) {
            ivStep2.setImageResource(R.drawable.order_state_2_white);
            tvOrderDesc.setText("专家已确定约谈,赶紧去支付吧");
            cancelView.setVisibility(GONE);
            confirmView.setText("去支付");
        } else if (orderStatus == 2) {
            ivStep3.setImageResource(R.drawable.order_state_3_white);
            if (serviceType == 1) {
                tvOrderDesc.setText("您已支付成功，可以给专家发送消息进行沟通");
            } else if (serviceType == 2) {
                tvOrderDesc.setText("您已支付成功，专家会通过大牛家的电话和你联系，请耐心等待");
            } else if (serviceType == 3) {
                tvOrderDesc.setText("您已支付成功，可以给专家发送消息，确定约谈时间和地点");
            }
            cancelView.setVisibility(GONE);
            confirmView.setText("聊天界面");
            if (isFromChat) {
                containerView.setVisibility(GONE);
            }
        } else if (orderStatus == 3) {
            ivStep4.setImageResource(R.drawable.order_state_4_white);
            tvOrderDesc.setText("本次约谈已结束，去评价一下吧");
            cancelView.setVisibility(GONE);
            confirmView.setText("去评价");
        } else if (orderStatus == 10) {
            ivStep1.setImageResource(R.drawable.order_state_6_white);
            tvOrderDesc.setText("您已取消约谈");
            containerView.setVisibility(GONE);
            cancelView.setVisibility(View.GONE);
            confirmView.setVisibility(GONE);
        } else if (orderStatus == 11) {
            ivStep1.setImageResource(R.drawable.order_state_6_white);
            tvRejectTag.setVisibility(VISIBLE);
            tvRejectTag.setText("拒绝理由:" + detailVo.getRejectReason());
            tvOrderDesc.setText("专家已拒绝您的约谈");
            containerView.setVisibility(GONE);
            cancelView.setVisibility(View.GONE);
            confirmView.setVisibility(GONE);
        } else if (orderStatus == 12) {
            ivStep5.setImageResource(R.drawable.order_state_5_white);
        }

        tvOrderNum.setText(detailVo.getOrderNo());
        tvCreateTime.setText("(" + TimeUtils.getTime(detailVo.getCreateTime() * 1000, TimeUtils.DATE_FORMAT_ORDER) + ")");
        if (!TextUtils.isEmpty(detailVo.getImgurl())) {
            Picasso.with(mContext).load(detailVo.getImgurl()).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(expertAvatar);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(expertAvatar);
        }

        expertName.setText(detailVo.getName());
        expertPosition.setText(detailVo.getPosition() + " | " + detailVo.getCompany());
        totalPrice.setText(detailVo.getTotalPrice());
        tvMyAsk.setText(questionDesc);
        tvMyIntro.setText(userIntro);
    }

    public void reSetProfileOrQues(String profile, String questionDesc) {
        this.userIntro = profile;
        this.questionDesc = questionDesc;
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
                    listener.userCancelOrder();
                }
            }
        } else if (v == confirmView) {
            if (orderStatus == 1) {
                if (listener != null) {
                    listener.userPayOrder();
                }
            } else if (orderStatus == 2) {
                if (listener != null) {
                    listener.gotoChat();
                }
            } else if (orderStatus == 3) {
                if (listener != null) {
                    listener.userCommentOrder();
                }
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
        } else if (v == expertAvatar) {
            if (listener != null) {
                listener.gotoExpertMain();
            }
        } else if (v == modifyAskView) {
            modifyType = 2;
            if (dialog == null) {
                dialog = new AlertMsgDlg((Activity) mContext);
            }
            dialog.initDlg(R.drawable.ic_question_blue, "我的提问", questionDesc);
            dialog.setEditable(this, modifyType);
            dialog.show();
        } else if (v == modifyIntroView) {
            modifyType = 1;
            if (dialog == null) {
                dialog = new AlertMsgDlg((Activity) mContext);
            }
            dialog.initDlg(R.drawable.introduce, "我的简介", userIntro);
            dialog.setEditable(this, modifyType);
            dialog.show();
        }
    }

    @Override
    public void modify(String content) {
        if (listener != null) {
            listener.modify(content, modifyType);
        }
        if (modifyType == 1) {
            tvMyIntro.setText(content);
        } else {
            tvMyAsk.setText(content);
        }
    }
}
