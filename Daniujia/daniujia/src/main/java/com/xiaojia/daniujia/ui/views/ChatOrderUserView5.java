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
public class ChatOrderUserView5 extends LinearLayout implements View.OnClickListener {
    private static final String SHARE_ALL = "all";
    private static final String SHARE_HALF = "half";

    private Context mContext;

    private View arrowView;
    private HideChatOrderListener listener;

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

    private TextView tvUserComment;
    private TextView tvShowScore;
    private ImageView ivOpenScore;
    private View expertReplyContainer;
    private TextView tvExpertReply;
    private TextView tvMyAsk;
    private ExpandableTextView tvMyIntro;
    private View goChatView;

    private TextView tvScore;
    private ImageView ivHelpStudent;

    private boolean isFromChat = false;
    private String questionDesc;
    private String userIntro;

    private String share;
    private KnowledgeShareTipDlg tipDlgHalf = null;
    private KnowledgeShareTipDlg tipDlgAll = null;
    private String knowledgeState = "";
    private DashedLineView dashedView;

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

    public ChatOrderUserView5(Context context, HideChatOrderListener listener, boolean isFromChat) {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.isFromChat = isFromChat;
        View view = LayoutInflater.from(context).inflate(R.layout.view_user_order_detail_finished, null);
        addView(view);
        initView(view);
    }

    public ChatOrderUserView5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatOrderUserView5(Context context, AttributeSet attrs, int defStyleAttr) {
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

        expertAvatar = (RoundedImageView) view.findViewById(R.id.expert_header);
        expertAvatar.setOnClickListener(this);
        expertName = (TextView) view.findViewById(R.id.expert_name);
        expertPosition = (TextView) view.findViewById(R.id.position);
        consultType = (TextView) view.findViewById(R.id.consult_type);
        perPrice = (TextView) view.findViewById(R.id.per_price);
        totalPrice = (TextView) view.findViewById(R.id.total_price);
        tvServiceTime = (TextView) view.findViewById(R.id.total_time);
        tvHasCall = (TextView) view.findViewById(R.id.has_call);

        tvScore = (TextView) view.findViewById(R.id.score);

        tvUserComment = (TextView) view.findViewById(R.id.user_comment);
        tvShowScore = (TextView) view.findViewById(R.id.show_score);
        tvShowScore.setOnClickListener(this);
        ivOpenScore = (ImageView) view.findViewById(R.id.score_open);
        ivOpenScore.setOnClickListener(this);
        expertReplyContainer = view.findViewById(R.id.expert_reply_container);
        tvExpertReply = (TextView) view.findViewById(R.id.id_expert_reply);
        dashedView = (DashedLineView) view.findViewById(R.id.user_order_finished_dashed);

        ivHelpStudent = (ImageView) view.findViewById(R.id.help_student_icon);
        ivHelpStudent.setOnClickListener(this);

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
        questionDesc = detailVo.getQuesDesc();
        userIntro = detailVo.getProfile();
        ivStep5.setImageResource(R.drawable.order_state_5_white);

        if (detailVo.getCharity() == null) {
            ivHelpStudent.setVisibility(View.GONE);
        } else {
            ivHelpStudent.setVisibility(View.VISIBLE);
            share = detailVo.getCharity().getShare();
        }

        tvOrderNum.setText(detailVo.getOrderNo());
        tvCreateTime.setText("(" + TimeUtils.getTime(detailVo.getCreateTime() * 1000, TimeUtils.DATE_FORMAT_ORDER) + ")");
        if (!TextUtils.isEmpty(detailVo.getImgurl())) {
            Picasso.with(mContext).load(detailVo.getImgurl()).resize(ScreenUtils.dip2px(35), ScreenUtils.dip2px(35)).error(R.drawable.ic_avatar_default).into(expertAvatar);
        } else {
            Picasso.with(mContext).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(35), ScreenUtils.dip2px(35)).error(R.drawable.ic_avatar_default).into(expertAvatar);
        }
        expertName.setText(detailVo.getName());
        expertPosition.setText(detailVo.getCompany() + "·" + detailVo.getPosition());

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
        tvScore.setText(detailVo.getScore() + "分");

        if (!TextUtils.isEmpty(detailVo.getReply())) {
            expertReplyContainer.setVisibility(VISIBLE);
            dashedView.setVisibility(View.VISIBLE);
            tvExpertReply.setText(detailVo.getReply());
        } else {
            expertReplyContainer.setVisibility(GONE);
            dashedView.setVisibility(View.INVISIBLE);
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
        } else if (v == ivOpenScore) {
            if (tvScore.getVisibility() == GONE) {
                tvScore.setVisibility(VISIBLE);
                ivOpenScore.setImageResource(R.drawable.score_collapse);
            } else {
                tvScore.setVisibility(GONE);
                ivOpenScore.setImageResource(R.drawable.score_open);
            }
        } else if (v == tvShowScore) {
            if (tvScore.getVisibility() == GONE) {
                tvScore.setVisibility(VISIBLE);
                ivOpenScore.setImageResource(R.drawable.score_collapse);
            } else {
                tvScore.setVisibility(GONE);
                ivOpenScore.setImageResource(R.drawable.score_open);
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
        } else if (v == goChatView) {
            if (listener != null) {
                listener.gotoChat();
            }
        }
    }
}
