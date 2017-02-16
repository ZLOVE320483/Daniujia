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
 * Created by ZLOVE on 2016/5/25.
 */
public class ChatOrderExpertView5 extends LinearLayout implements View.OnClickListener {
    private static final String SHARE_ALL = "all";
    private static final String SHARE_HALF = "half";

    private Context mContext;

    private View arrowView;
    private HideChatOrderListener listener;

    private ImageView ivStep5;

    private TextView tvOrderNum;
    private TextView tvCreateTime;
    private RoundedImageView userAvatar;
    private TextView userName;
    private TextView consultType;
    private TextView perPrice;
    private TextView totalPrice;
    private TextView tvServiceTime;
    private TextView tvHasCall;
    private TextView tvUserComment;
    private View myReplyContainer;
    private TextView tvMyReply;
    private TextView tvReply;
    private TextView tvMyAsk;
    private ExpandableTextView tvMyIntro;
    private View goChatView;

    private boolean isFromChat = false;

    private String questionDesc;
    private String userIntro;

    private String share;
    private KnowledgeShareTipDlg tipDlgHalf = null;
    private KnowledgeShareTipDlg tipDlgAll = null;
    private ImageView ivHelpStudent;
    private String knowledgeState = "";
    private DashedLineView dashedLineView;

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

    public ChatOrderExpertView5(Context context, HideChatOrderListener listener, boolean isFromChat) {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.isFromChat = isFromChat;
        View view = LayoutInflater.from(context).inflate(R.layout.view_expert_order_detail_finished, null);
        addView(view);
        initView(view);
    }

    public ChatOrderExpertView5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatOrderExpertView5(Context context, AttributeSet attrs, int defStyleAttr) {
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

        ivStep5 = (ImageView) view.findViewById(R.id.step5);

        userAvatar = (RoundedImageView) view.findViewById(R.id.user_avatar);
        userName = (TextView) view.findViewById(R.id.user_name);
        consultType = (TextView) view.findViewById(R.id.consult_type);
        perPrice = (TextView) view.findViewById(R.id.per_price);
        totalPrice = (TextView) view.findViewById(R.id.total_price);
        tvServiceTime = (TextView) view.findViewById(R.id.total_time);
        tvHasCall = (TextView) view.findViewById(R.id.has_call);

        tvUserComment = (TextView) view.findViewById(R.id.user_comment);

        myReplyContainer = view.findViewById(R.id.my_reply_container);
        tvMyReply = (TextView) view.findViewById(R.id.id_my_reply);

        tvReply = (TextView) view.findViewById(R.id.id_expert_reply);
        tvReply.setOnClickListener(this);

        ivHelpStudent = (ImageView) view.findViewById(R.id.help_student_icon);
        ivHelpStudent.setOnClickListener(this);
        dashedLineView = (DashedLineView) view.findViewById(R.id.expert_order_finished_dashed);
        tvMyAsk = (TextView) view.findViewById(R.id.id_my_ask);
        tvMyIntro = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        goChatView = view.findViewById(R.id.id_confirm);
        goChatView.setOnClickListener(this);
        if (isFromChat) {
            goChatView.setVisibility(GONE);
        } else {
            goChatView.setVisibility(VISIBLE);
        }
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

        questionDesc = detailVo.getQuesDesc();
        userIntro = detailVo.getProfile();
        ivStep5.setImageResource(R.drawable.order_state_5_white);

        tvOrderNum.setText(detailVo.getOrderNo());
        tvCreateTime.setText("(" + TimeUtils.getTime(detailVo.getCreateTime() * 1000, TimeUtils.DATE_FORMAT_ORDER) + ")");

        if (!TextUtils.isEmpty(detailVo.getImgurl())) {
            Picasso.with(mContext).load(detailVo.getImgurl()).resize(ScreenUtils.dip2px(40),ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(userAvatar);
        }else{
            Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(40),ScreenUtils.dip2px(40)).error(R.drawable.ic_avatar_default).into(userAvatar);
        }
        if (!TextUtils.isEmpty(detailVo.getName())) {
            userName.setText(detailVo.getName());
        } else {
            userName.setText(detailVo.getUsername());
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
        totalPrice.setText(detailVo.getTotalPrice());
        tvUserComment.setText(detailVo.getComment());

        if (!TextUtils.isEmpty(detailVo.getReply())) {
            myReplyContainer.setVisibility(VISIBLE);
            dashedLineView.setVisibility(VISIBLE);
            tvMyReply.setText(detailVo.getReply());
            tvReply.setVisibility(GONE);
        } else {
            myReplyContainer.setVisibility(GONE);
            // 注意要设置invisible，相对位置不会发生改变
            dashedLineView.setVisibility(INVISIBLE);
            tvReply.setVisibility(VISIBLE);
        }

        tvMyAsk.setText(questionDesc);
        tvMyIntro.setText(userIntro);
    }

    @Override
    public void onClick(View v) {
        if (v == arrowView) {
            if (listener != null) {
                listener.hideOrder();
            }
        } else if (v == tvReply) {
            if (listener != null) {
                listener.expertReplyOrder();
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
        } else if (v == goChatView) {
            if (listener != null) {
                listener.gotoChat();
            }
        }
    }

}
