package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.dlgs.ConfirmConsultantDialog;
import com.xiaojia.daniujia.dlgs.ShareDialog;
import com.xiaojia.daniujia.domain.PublishRequireData;
import com.xiaojia.daniujia.domain.resp.RequirementDetailRespVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.ui.act.ActChat;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActMyCommentToConsultant;
import com.xiaojia.daniujia.ui.act.ActRequirementPublish;
import com.xiaojia.daniujia.ui.act.ActToBeExpert;
import com.xiaojia.daniujia.ui.act.ActUserHome;
import com.xiaojia.daniujia.ui.control.RequirementDetailControl;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.DateUtil;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.ShareSDKUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import java.io.File;
import java.util.List;

import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by ZLOVE on 2017/1/4.
 */
public class RequirementDetailFragment extends BaseFragment implements View.OnClickListener, ConfirmConsultantDialog.ConfirmConsultantListener, DialogManager.NotifyListener {

    private RequirementDetailControl control;

    private ScrollView scrollView;
    private View allView;
    private View container;
    private ImageView ivShare;
    private TextView tvTitle;
    private TextView tvArea;
    private TextView tvBudgetPrice;
    private TextView tvCompany;
    private TextView tvVerify;
    private TextView tvRequirementContent;
    private TextView tvSignUpCount;
    private TextView tvBrowserCount;
    private TextView tvEffectTime;
    private View consultantCountContainer;
    private TextView tvConsultantCount;
    private TextView tvServiceTip;
    private TextView tvRequirementState;
    private TextView tvServiceDuration;
    private View serviceDurationContainer;
    private Button btnOperation;

    private LinearLayout cooperateExpertOutContainer;
    private LinearLayout cooperateExpertInnerContainer;
    private TextView tvSignUpTag;
    private LinearLayout signUpExpertOutContainer;
    private LinearLayout signUpExpertInnerContainer;
    private ImageView ivArrow;
    private boolean isOpen = false;
    private TextView tvPublishRequirementTip;
    private LinearLayout bottomContainer;

    private RequirementDetailRespVo respVo;
    private String status;
    private String currentUser;

    private ConfirmConsultantDialog dialog;

    private int demandId;
    private String shareTitle;
    private String shareContent;
    private String shareImgUrl;
    private String shareLinkUrl;
    private ShareDialog shareDialog;

    private boolean isEdit = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new RequirementDetailControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_requirement_detail;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_DEMAND_ID)) {
                demandId = intent.getIntExtra(IntentKey.INTENT_KEY_DEMAND_ID, 0);
            }
        }
        view.findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(IntentKey.INTENT_KEY_IS_REQUIREMENT_EDIT, isEdit);
                finishActivity(result);
            }
        });
        ((TextView) view.findViewById(R.id.id_title)).setText("需求详情");
        scrollView = (ScrollView) view.findViewById(R.id.id_scroll_view);
        allView = view.findViewById(R.id.view);
        container = view.findViewById(R.id.container);
        ivShare = (ImageView) view.findViewById(R.id.id_share);
        ivShare.setImageResource(R.drawable.demand_share);
        ivShare.setVisibility(View.VISIBLE);
        ivShare.setOnClickListener(this);

        tvTitle = (TextView) view.findViewById(R.id.id_requirement_title);
        tvArea = (TextView) view.findViewById(R.id.id_area);
        tvBudgetPrice = (TextView) view.findViewById(R.id.id_price);
        tvCompany = (TextView) view.findViewById(R.id.id_company);
        tvVerify = (TextView) view.findViewById(R.id.id_verify);
        tvRequirementContent = (TextView) view.findViewById(R.id.id_content);
        tvSignUpCount = (TextView) view.findViewById(R.id.sign_up_count);
        tvBrowserCount = (TextView) view.findViewById(R.id.browser_count);
        tvEffectTime = (TextView) view.findViewById(R.id.effect_time);
        consultantCountContainer = view.findViewById(R.id.id_consultant_count_container);
        tvConsultantCount = (TextView) view.findViewById(R.id.id_consultant_count);
        tvServiceTip = (TextView) view.findViewById(R.id.id_service_tip);
        tvRequirementState = (TextView) view.findViewById(R.id.requirement_state);
        tvServiceDuration = (TextView) view.findViewById(R.id.id_per_time);
        serviceDurationContainer = view.findViewById(R.id.service_duration_container);
        btnOperation = (Button) view.findViewById(R.id.id_confirm);

        cooperateExpertOutContainer = (LinearLayout) view.findViewById(R.id.cooperate_expert_out_container);
        cooperateExpertInnerContainer = (LinearLayout) view.findViewById(R.id.cooperate_expert_inner_container);
        tvSignUpTag = (TextView) view.findViewById(R.id.sign_up_tag);
        signUpExpertOutContainer = (LinearLayout) view.findViewById(R.id.sign_up_expert_out_container);
        signUpExpertInnerContainer = (LinearLayout) view.findViewById(R.id.sign_up_expert_inner_container);
        ivArrow = (ImageView) view.findViewById(R.id.id_arrow_1);
        tvPublishRequirementTip = (TextView) view.findViewById(R.id.requirement_tip);
        bottomContainer = (LinearLayout) view.findViewById(R.id.bottom_container);

        allView.setVisibility(View.VISIBLE);
        serviceDurationContainer.setOnClickListener(this);
        btnOperation.setOnClickListener(this);
        ivArrow.setOnClickListener(this);

        control.requestRequirementDetail(demandId);
    }

    public void setRequirementDetailInfo(RequirementDetailRespVo respVo) {
        if (respVo == null) {
            return;
        }
        this.allView.setVisibility(View.GONE);
        this.respVo = respVo;
        this.status = respVo.getStatus();
        this.currentUser = respVo.getCurrentUser();
        this.shareTitle = "[大牛家]正在寻找" + respVo.getConsultantName() + "，预算" + respVo.getBudget();
        this.shareContent = respVo.getDesc();
        this.shareImgUrl = "https://dn-daniujia.qbox.me/20170117_demand_logo.png";
        if (Config.DEBUG) {
            this.shareLinkUrl = "https://refactorapi.daniujia.com/m/?share-name=demand#!/demand/" + demandId;
        } else {
            this.shareLinkUrl = "https://www.daniujia.com/m/?share-name=demand#!/demand/" + demandId;
        }
        tvTitle.setText(respVo.getConsultantName());
        tvArea.setText(respVo.getCityName());
        tvBudgetPrice.setText(respVo.getBudget());
        tvCompany.setText(respVo.getWriteName());
        if (respVo.isCertified()) {
            tvVerify.setVisibility(View.VISIBLE);
        } else {
            tvVerify.setVisibility(View.GONE);
        }
        tvRequirementContent.setText(respVo.getDesc());
        tvSignUpCount.setText("报名专家 " + respVo.getEnterCount());
        tvBrowserCount.setText("浏览数 " + respVo.getViewCount());
        if (currentUser.equals("user") && status.equals("confirmed")) {
            tvEffectTime.setText("已完成");
        } else {
            tvEffectTime.setText("有效期 " + DateUtil.formatCouponDate(respVo.getDeadline()));
        }
        if (status.equals("unaudited")) {
            tvRequirementState.setText("需求待确认");
            tvRequirementState.setTextColor(getContext().getResources().getColor(R.color.order_state_color_wait));
            cooperateExpertOutContainer.setVisibility(View.GONE);
            signUpExpertOutContainer.setVisibility(View.GONE);
            tvPublishRequirementTip.setVisibility(View.VISIBLE);
            ivShare.setVisibility(View.GONE);
        } else if (status.equals("searching")) {
            tvRequirementState.setText("顾问寻找中");
            tvRequirementState.setTextColor(getContext().getResources().getColor(R.color.order_state_color_doing));
            ivShare.setVisibility(View.VISIBLE);
        } else if (status.equals("confirmed")) {
            tvRequirementState.setText("顾问已确定");
            tvRequirementState.setTextColor(getContext().getResources().getColor(R.color.order_state_color_finished));
            ivShare.setVisibility(View.VISIBLE);
        } else if (status.equals("closed")) {
            tvRequirementState.setText("需求已关闭");
            tvRequirementState.setTextColor(getContext().getResources().getColor(R.color.order_state_color_cancel));
            ivShare.setVisibility(View.GONE);
        }

        tvServiceDuration.setText(respVo.getServiceCycle());
        int consultantCount = respVo.getConsultantCount();
        if (consultantCount <= 1) {
            consultantCountContainer.setVisibility(View.GONE);
        } else {
            tvConsultantCount.setText(String.valueOf(respVo.getConsultantCount()));
            consultantCountContainer.setVisibility(View.VISIBLE);
        }
        tvServiceTip.setText("到现场服务说明：" + respVo.getServiceDay());
        if (currentUser.equals("user")) {
            addUserCanLook();
        } else if (currentUser.equals("applicantUser")) {
            addApplicantUserCanLook();
        } else if (currentUser.equals("confirmedUser")) {
            addConfirmedUserCanLook();
        } else if (currentUser.equals("demandUser")) {
            addDemandUserCanLook();
        }
    }

    /**
     * currentUser
     * user :未登录/未报名用户 默认
     * applicantUser : 报名用户(未被确认)
     * confirmedUser : 已确定用户(报名专家被确认为顾问)
     * demandUser : 发布者
     */

    private void addUserCanLook() {
        if (status.equals("searching")) {
            btnOperation.setVisibility(View.VISIBLE);
            btnOperation.setText("报 名");
        } else if (status.equals("confirmed") || status.equals("closed")) {
            btnOperation.setVisibility(View.GONE);
        }
        signUpExpertInnerContainer.removeAllViews();
        signUpExpertOutContainer.setVisibility(View.GONE);
        addUserCooperateExpert();
    }

    private void addApplicantUserCanLook() {
        if (status.equals("searching")) {
            btnOperation.setText("取消报名");
            btnOperation.setVisibility(View.VISIBLE);
        } else if (status.equals("confirmed") || status.equals("closed")) {
            btnOperation.setVisibility(View.GONE);
        }
        addUserSignUpExpert();
        addUserCooperateExpert();
    }

    private void addConfirmedUserCanLook() {
        btnOperation.setVisibility(View.GONE);
        bottomContainer.removeAllViews();
        final RequirementDetailRespVo.EnterExpert enterExpert = respVo.getDemandUser();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_sign_up_expert_for_confirmed, null);
        RoundedImageView ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
        TextView tvName = (TextView) view.findViewById(R.id.id_name);
        View chatView = view.findViewById(R.id.id_chat);
        View callView  = view.findViewById(R.id.id_call);
        Picasso.with(getActivity()).load(enterExpert.getImgUrl()).into(ivAvatar);
        tvName.setText(enterExpert.getName());
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterExpert.getIdentity().equals("user")) {
                    Intent intent = new Intent(getContext(), ActUserHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, enterExpert.getId());
                    startActivity(intent);
                } else if (enterExpert.getIdentity().equals("applyingExpert")) {
                    Intent intent = new Intent(getActivity(), ActExpertHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, enterExpert.getId());
                    intent.putExtra(IntentKey.INTENT_KEY_FROM_EXPERT_PREVIEW, true);
                    startActivity(intent);
                } else if (enterExpert.getIdentity().equals("expert")) {
                    Intent intent = new Intent(getActivity(), ActExpertHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, enterExpert.getId());
                    startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
                }
            }
        });
        chatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.judgeConversation(enterExpert.getUsername());
                Intent intent = new Intent(getActivity(), ActChat.class);
                intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, enterExpert.getUsername());
                intent.putExtra(IntentKey.INTENT_KEY_CHAT_NAME, enterExpert.getName());
                intent.putExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL, enterExpert.getImgUrl());
                intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, enterExpert.getId());
                int identity;
                if (enterExpert.getIdentity().equals("expert")) {
                    identity = 2;
                } else {
                    identity = 1;
                }
                intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, identity);
                startActivity(intent);
            }
        });
        callView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.callPhone(demandId,  enterExpert.getId(), "expertToPublisher");
                CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.call_phone_tip), "知道了", true);
                callTipDialog.showdialog();
            }
        });
        bottomContainer.addView(view);
        addUserCooperateExpert();
    }

    private void addDemandUserCanLook() {
        if (status.equals("unaudited")) {
            btnOperation.setText("编 辑");
            btnOperation.setVisibility(View.VISIBLE);
        }
        addPublisherSignUpExpert();
        addPublisherCooperateExpert();
    }

    //--------------发布者看到的报名专家的样式------------
    private void addPublisherSignUpExpert() {
        if (ListUtils.isEmpty(respVo.getEnterExperts())) {
            return;
        }
        tvSignUpTag.setText("报名专家");
        signUpExpertOutContainer.setVisibility(View.VISIBLE);
        signUpExpertInnerContainer.removeAllViews();
        List<RequirementDetailRespVo.EnterExpert> enterExpertList = respVo.getEnterExperts();
        if (status.equals("searching") || status.equals("closed")) {
            ivArrow.setVisibility(View.GONE);
            for (final RequirementDetailRespVo.EnterExpert enterExpert : enterExpertList) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_sign_up_expert_for_publisher, null);
                RoundedImageView ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
                TextView tvName = (TextView) view.findViewById(R.id.id_name);
                View chatView = view.findViewById(R.id.id_chat);
                View callView = view.findViewById(R.id.id_call);
                View confirmExpertView = view.findViewById(R.id.id_confirm);
                if (status.equals("closed")){
                    confirmExpertView.setEnabled(false);
                } else {
                    confirmExpertView.setEnabled(true);
                }
                Picasso.with(getActivity()).load(enterExpert.getImgUrl()).into(ivAvatar);
                tvName.setText(enterExpert.getName());
                ivAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ActExpertHome.class);
                        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, enterExpert.getId());
                        startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
                    }
                });
                chatView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status.equals("closed")) {
                            CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.requirement_shut_down_tip_txt), "知道了", true);
                            callTipDialog.showdialog();
                            return;
                        }
                        control.judgeConversation(enterExpert.getUsername());
                        Intent intent = new Intent(getActivity(), ActChat.class);
                        intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, enterExpert.getUsername());
                        intent.putExtra(IntentKey.INTENT_KEY_CHAT_NAME, enterExpert.getName());
                        intent.putExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL, enterExpert.getImgUrl());
                        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, enterExpert.getId());
                        int identity;
                        if (enterExpert.getIdentity().equals("expert")) {
                            identity = 2;
                        } else {
                            identity = 1;
                        }
                        intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, identity);
                        startActivity(intent);
                    }
                });
                callView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status.equals("closed")) {
                            CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.requirement_shut_down_tip_phone), "知道了", true);
                            callTipDialog.showdialog();
                            return;
                        }
                        control.callPhone(demandId,  enterExpert.getId(), "publisherToExpert");
                        CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.call_phone_tip), "知道了", true);
                        callTipDialog.showdialog();
                    }
                });
                confirmExpertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog == null) {
                            dialog = new ConfirmConsultantDialog(getActivity(), RequirementDetailFragment.this);
                        }
                        dialog.setExpertId(enterExpert.getId());
                        dialog.showDialog();
                    }
                });
                signUpExpertInnerContainer.addView(view);
            }
        } else if (status.equals("confirmed")) {
            ivArrow.setVisibility(View.VISIBLE);
            for (final RequirementDetailRespVo.EnterExpert enterExpert : enterExpertList) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_collect_me, null);
                RoundedImageView ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
                TextView tvName = (TextView) view.findViewById(R.id.tv_item_collect_me);
                Picasso.with(getActivity()).load(enterExpert.getImgUrl()).into(ivAvatar);
                tvName.setText(enterExpert.getName());
                ivAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ActExpertHome.class);
                        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, enterExpert.getId());
                        startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
                    }
                });
                signUpExpertInnerContainer.addView(view);
                signUpExpertInnerContainer.setVisibility(View.GONE);
            }
        }
    }

    //-------------发布者看到的合作专家的样式------------
    private void addPublisherCooperateExpert() {
        if (ListUtils.isEmpty(respVo.getConfirmedExperts())) {
            return;
        }
        cooperateExpertOutContainer.setVisibility(View.VISIBLE);
        cooperateExpertInnerContainer.removeAllViews();
        List<RequirementDetailRespVo.ConfirmedExpert> expertList = respVo.getConfirmedExperts();
        for (final RequirementDetailRespVo.ConfirmedExpert expert : expertList) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_cooperate_expert_for_publisher, null);
            RoundedImageView ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
            TextView tvName = (TextView) view.findViewById(R.id.id_name);
            View chatView = view.findViewById(R.id.id_chat);
            View callView = view.findViewById(R.id.id_call);
            View commentView = view.findViewById(R.id.id_show_comment);
            Picasso.with(getActivity()).load(expert.getImgUrl()).into(ivAvatar);
            tvName.setText(expert.getName());
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActExpertHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, expert.getId());
                    startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
                }
            });
            chatView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    control.judgeConversation(expert.getUsername());
                    Intent intent = new Intent(getActivity(), ActChat.class);
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, expert.getUsername());
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_NAME, expert.getName());
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_AVATAR_URL, expert.getImgUrl());
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, expert.getId());
                    int identity;
                    if (expert.getIdentity().equals("expert")) {
                        identity = 2;
                    } else {
                        identity = 1;
                    }
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, identity);
                    startActivity(intent);
                }
            });
            callView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    control.callPhone(demandId,  expert.getId(), "publisherToExpert");
                    CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.call_phone_tip), "知道了", true);
                    callTipDialog.showdialog();
                }
            });
            commentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActMyCommentToConsultant.class);
                    intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, demandId);
                    intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, expert.getId());
                    if (status.equals("closed")) {
                        intent.putExtra(IntentKey.INTENT_KEY_IS_REQUIREMENT_CAN_COMMENT, false);
                    }
                    startActivity(intent);
                }
            });
            cooperateExpertInnerContainer.addView(view);
        }
    }

    private void addUserSignUpExpert() {
        final RequirementDetailRespVo.EnterExpert enterExpert = respVo.getApplicantUser();
        if (enterExpert == null) {
            return;
        }
        signUpExpertInnerContainer.removeAllViews();
        bottomContainer.removeAllViews();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_sign_up_expert_for_not_select, null);
        RoundedImageView ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
        TextView tvName = (TextView) view.findViewById(R.id.id_name);
        TextView tvSignUpTip = (TextView) view.findViewById(R.id.sign_up_tip);
        TextView tvBrowserCount = (TextView) view.findViewById(R.id.browser_count);
        Picasso.with(getActivity()).load(enterExpert.getImgUrl()).into(ivAvatar);
        tvName.setText(enterExpert.getName());
        if (enterExpert.getIdentity().equals("user")) {
            tvBrowserCount.setVisibility(View.GONE);
            tvSignUpTip.setText("已报名 资料审核中");
            tvSignUpTip.setTextColor(getResources().getColor(R.color.second_text_color));
        } else {
            tvSignUpTip.setText("已报名");
            tvSignUpTip.setTextColor(getResources().getColor(R.color.first_text_color));
            tvBrowserCount.setVisibility(View.VISIBLE);
            tvBrowserCount.setText("发布者查看主页：" + enterExpert.getViewCount() + "次");
        }
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enterExpert.getIdentity().equals("user")) {
                    Intent intent = new Intent(getContext(), ActUserHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, enterExpert.getId());
                    startActivity(intent);
                } else if (enterExpert.getIdentity().equals("applyingExpert")) {
                    Intent intent = new Intent(getActivity(), ActExpertHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, enterExpert.getId());
                    intent.putExtra(IntentKey.INTENT_KEY_FROM_EXPERT_PREVIEW, true);
                    startActivity(intent);
                } else if (enterExpert.getIdentity().equals("expert")) {
                    Intent intent = new Intent(getActivity(), ActExpertHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, enterExpert.getId());
                    startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
                }
            }
        });
        if (status.equals("searching")) {
            tvSignUpTag.setText("报名状态");
            ivArrow.setVisibility(View.GONE);
            signUpExpertOutContainer.setVisibility(View.VISIBLE);
            signUpExpertInnerContainer.addView(view);
        } else if (status.equals("confirmed") || status.equals("closed")) {
            bottomContainer.addView(view);
        }
    }

    //---------------用户看见的合作专家样式------------------
    private void addUserCooperateExpert() {
        if (ListUtils.isEmpty(respVo.getConfirmedExperts())) {
            return;
        }
        cooperateExpertOutContainer.setVisibility(View.VISIBLE);
        cooperateExpertInnerContainer.removeAllViews();
        List<RequirementDetailRespVo.ConfirmedExpert> confirmedExpertList = respVo.getConfirmedExperts();
        for (final RequirementDetailRespVo.ConfirmedExpert expert : confirmedExpertList) {
            View expertView = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_cooperate_expert_for_user, null);
            RoundedImageView ivExpertAvatar = (RoundedImageView) expertView.findViewById(R.id.id_avatar);
            TextView tvExpertName = (TextView) expertView.findViewById(R.id.id_name);
            LinearLayout commentLayout = (LinearLayout) expertView.findViewById(R.id.expert_comment_container);
            Picasso.with(getActivity()).load(expert.getImgUrl()).into(ivExpertAvatar);
            tvExpertName.setText(expert.getName());
            List<RequirementDetailRespVo.ConfirmedExpert.Evaluation> evaluations = expert.getEvaluations();
            for (RequirementDetailRespVo.ConfirmedExpert.Evaluation evaluation : evaluations) {
                View commentView = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_comment_to_expert, null);
                TextView tvContent = (TextView) commentView.findViewById(R.id.id_content);
                TextView tvTime = (TextView) commentView.findViewById(R.id.id_time);
                tvContent.setText(evaluation.getContent() + "”");
                tvTime.setText(DateUtil.formatCouponDate(evaluation.getDatetime()));
                commentLayout.addView(commentView);
            }
            ivExpertAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActExpertHome.class);
                    intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, expert.getId());
                    startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
                }
            });
            cooperateExpertInnerContainer.addView(expertView);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivShare) {
            if (shareDialog == null) {
                shareDialog = new ShareDialog(getActivity());
                shareDialog.setOnShareClickListener(new ShareDialog.ShareClickListener() {
                    @Override
                    public void onShareClick(String platform) {
                        if (platform.equals(SinaWeibo.NAME)) {
                            handleSinaWebShare(shareTitle, shareImgUrl, shareLinkUrl, platform);
                        } else {
                            ShareSDKUtil.share(shareTitle, shareContent, shareImgUrl, shareLinkUrl, platform);
                        }
                        shareDialog.dismiss();
                    }
                });
            }
            shareDialog.showDialog();
        } else if (v == serviceDurationContainer) {
            CommonDialog dialog = new CommonDialog(getActivity(), null, "温馨提示", getContext().getResources().getString(R.string.requirement_service_duration_desc) , "知道了", true);
            dialog.showdialog();
        } else if (v == btnOperation) {
            // TODO
            if (currentUser.equals("demandUser") && status.equals("unaudited")) {
                //编辑
                PublishRequireData data = new PublishRequireData();
                data.setConsultantName(respVo.getConsultantName());
                data.setBudget(respVo.getBudget());
                data.setDesc(respVo.getDesc());
                data.setServiceDay(respVo.getServiceDay());
                data.setServiceCycle(respVo.getServiceCycle());
                data.setWriteName(respVo.getWriteName());
                data.setCityName(respVo.getCityName());
                data.setCityCode(respVo.getCityCode());
                data.setDeadline(respVo.getDeadline());
                data.setRequireId(respVo.getDemandId());
                Intent intent = new Intent(getActivity(), ActRequirementPublish.class);
                intent.putExtra(IntentKey.INTENT_VALUE_CHANGE_REQUIRE, data);
                startActivityForResult(intent, IntentKey.REQ_CODE_EDIT_REQUIREMENT);
            } else if (currentUser.equals("user") && status.equals("searching")) {
                // 报名
                if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                    Intent intent = new Intent(getActivity(), ActLogin.class);
                    startActivityForResult(intent, IntentKey.REQ_CODE_ASK_LOGIN);
                } else {
                    if (SysUtil.getIntPref(PrefKeyConst.PREF_APPLY_STEP) < 15) {
                        CommonDialog editInfoDialog = new CommonDialog(getActivity(), new CommonDialog.ConfirmAction() {
                            @Override
                            public void confirm() {
                                Intent intent = new Intent(getActivity(), ActToBeExpert.class);
                                intent.putExtra(IntentKey.FROM_WHERE, IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_REQUIRE);
                                startActivity(intent);
                            }

                            @Override
                            public void cancel() {
                            }
                        }, "温馨提示", getResources().getString(R.string.edit_expert_info_tip), "填写资料", "暂不报名");
                        editInfoDialog.showdialog();
                    } else {
                        CommonDialog signUpDialog = new CommonDialog(getActivity(), new CommonDialog.ConfirmAction() {
                            @Override
                            public void confirm() {
                                control.signUpRequirement(demandId, "");
                            }

                            @Override
                            public void cancel() {
                            }
                        }, "温馨提示", getResources().getString(R.string.sign_up_tip), "报名", "暂不报名");
                        signUpDialog.showdialog();
                    }
                }
            } else if (currentUser.equals("applicantUser") && status.equals("searching")) {
                // 取消报名
                CommonDialog cancelSignUpDialog = new CommonDialog(getActivity(), new CommonDialog.ConfirmAction() {
                    @Override
                    public void confirm() {
                        control.signUpRequirement(demandId, "out");
                    }

                    @Override
                    public void cancel() {
                    }
                }, "温馨提示", getContext().getResources().getString(R.string.cancel_sign_up_tip), "确定取消", "暂不取消");
                cancelSignUpDialog.showdialog();
            }
        } else if (v == ivArrow) {
            if (isOpen) {
                isOpen = false;
                ivArrow.setImageResource(R.drawable.grey_drag_bottom);
                signUpExpertInnerContainer.setVisibility(View.GONE);
            } else {
                isOpen = true;
                ivArrow.setImageResource(R.drawable.grey_drag_top);
                signUpExpertInnerContainer.setVisibility(View.VISIBLE);
                App.get().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, 200);
                    }
                }, 200);
            }
            container.postInvalidate();
        }
    }

    @Override
    public void confirm(int expertId, String content) {
        if (TextUtils.isEmpty(content)) {
            showShortToast("请输入评价内容");
            return;
        }
        control.confirmExpert(demandId, expertId, content);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_EDIT_REQUIREMENT) {
            if (data != null) {
                isEdit = true;
                control.requestRequirementDetail(demandId);
            }
        }  else if (requestCode == IntentKey.REQ_CODE_ASK_LOGIN) {
            if (data != null) {
                control.requestUserInfo();
                control.requestRequirementDetail(demandId);
            }
        }
    }

    public void setUserCenterInfo(UserCenterRespVo info) {
        if (info == null) {
            return;
        }
        int step = info.getUserDesc().getApplyStepValue();
        SysUtil.savePref(PrefKeyConst.PREF_APPLY_STEP, step);
        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 1 && info.getBeconsultanttip() == 0) {
            DialogManager.showToBeExpertDialog(getActivity(), this);
        }
    }


    @Override
    public void notifyTobeExpert() {
        control.notifyTobeExpert();
    }

    @Override
    public void tobeExpert() {
        Intent intent = new Intent(getActivity(), ActToBeExpert.class);
        intent.putExtra(IntentKey.FROM_WHERE, IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_REQUIRE);
        startActivity(intent);
    }

    @Override
    public void notifyBecomeExpert() {
        control.notifyBecomeExpert();
    }

    private void handleSinaWebShare(final String title, String imgUrl, final String linkUrl, final String platform) {
        String[] str = imgUrl.split("/");
        final File file = new File(FileStorage.APP_IMG_DIR, str[str.length - 1]);
        if (file.exists() && file.length() > 0) {
            ShareSDKUtil.share(title, shareContent, file.getAbsolutePath(), linkUrl, platform);
            return;
        }
        imgUrl = imgUrl + "?imageView2/2/w/240";
        QiniuDownloadUtil.download(file, imgUrl, new QiniuDownloadUtil.DownloadCallback() {
            @Override
            public void downloadComplete(String path) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShareSDKUtil.share(title, shareContent, file.getAbsolutePath(), linkUrl, platform);
                    }
                });
            }

            @Override
            public void onProgress(int max, int progress) {
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }
}
