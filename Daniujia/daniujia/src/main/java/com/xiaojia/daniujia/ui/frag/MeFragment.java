package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ask.AskRewardWebActivity;
import com.xiaojia.daniujia.children.ChildrenShareWebActivity;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.domain.resp.ChildShareRespVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.ui.act.ActCoupon;
import com.xiaojia.daniujia.ui.act.ActEntrepreneurshipGuide;
import com.xiaojia.daniujia.ui.act.ActExpertCenter;
import com.xiaojia.daniujia.ui.act.ActExpertModifyInfo;
import com.xiaojia.daniujia.ui.act.ActExpertOrder;
import com.xiaojia.daniujia.ui.act.ActHelp;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActMyExpert;
import com.xiaojia.daniujia.ui.act.ActSetting;
import com.xiaojia.daniujia.ui.act.ActToBeExpert;
import com.xiaojia.daniujia.ui.act.ActUserBasic;
import com.xiaojia.daniujia.ui.act.ActUserRequirement;
import com.xiaojia.daniujia.ui.control.MeControl;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/4/28.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener, DialogManager.NotifyListener {

    private MeControl control;

    private View headEditView;
    private RoundedImageView ivAvatar;
    private TextView tvName;
    private TextView tvJob;

    private View myExpertView;
    private TextView tvOrderCount;
    private View myCollectionView;
    private View myCouponView;
    private View toBeExpertView;
    private View helpView;
    private View askView;
    private ImageView ivSetting;
    private Button login;
    private ImageView edit_iv;

    private View expertInfoContainer;
    private TextView tvServiceCount;
    private View toBeExpertContainer;
    private View expertCenterView;
    private View expertOrderView;
    private TextView workAge;
    private RelativeLayout workAge_container;
    private RelativeLayout edit_container;
    private RelativeLayout not_login_container;
    private RelativeLayout login_container;
    private RelativeLayout fill_data_container;
    private ImageView mIvActEntrance;
    private TextView mTvAsk;
    private View separateView;

    private TextView tvExamine;
    private boolean isExamine = false;
    //    private String knowledgeState = "";
    private RelativeLayout mRlRequirement;
    private TextView mTvaRequireCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new MeControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_me;
    }

    @Override
    protected void setUpView(View view) {
        headEditView = view.findViewById(R.id.login_container);
        headEditView.setOnClickListener(this);
        ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
        ivAvatar.setOnClickListener(this);
        tvName = (TextView) view.findViewById(R.id.id_name);
        tvJob = (TextView) view.findViewById(R.id.id_job);
        workAge = (TextView) view.findViewById(R.id.workAge);

        login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(this);

        not_login_container = (RelativeLayout) view.findViewById(R.id.not_login_state);
        login_container = (RelativeLayout) view.findViewById(R.id.login_state);
        fill_data_container = (RelativeLayout) view.findViewById(R.id.fill_data);
        fill_data_container.setOnClickListener(this);

        edit_iv = (ImageView) view.findViewById(R.id.edit_iv);
        edit_iv.setOnClickListener(this);

        myExpertView = view.findViewById(R.id.id_my_expert);
        myExpertView.setOnClickListener(this);
        tvOrderCount = (TextView) view.findViewById(R.id.id_order_count);
        myCollectionView = view.findViewById(R.id.id_my_collect);
        myCollectionView.setOnClickListener(this);
        myCouponView = view.findViewById(R.id.id_my_coupon);
        myCouponView.setOnClickListener(this);
        toBeExpertView = view.findViewById(R.id.id_to_be_expert);
        toBeExpertView.setOnClickListener(this);

        helpView = view.findViewById(R.id.id_help);
        helpView.setOnClickListener(this);
        ivSetting = (ImageView) view.findViewById(R.id.setting);
        ivSetting.setOnClickListener(this);
        askView = view.findViewById(R.id.id_pay_for_ask);
        askView.setOnClickListener(this);

        expertInfoContainer = view.findViewById(R.id.id_expert_info_container);
        workAge_container = (RelativeLayout) view.findViewById(R.id.workAge_container);
        edit_container = (RelativeLayout) view.findViewById(R.id.edit_container);
        toBeExpertContainer = view.findViewById(R.id.id_to_be_expert_container);
        expertCenterView = view.findViewById(R.id.id_expert_center);
        expertCenterView.setOnClickListener(this);
        tvServiceCount = (TextView) view.findViewById(R.id.id_service_count);
        expertOrderView = view.findViewById(R.id.id_expert_order);
        expertOrderView.setOnClickListener(this);
        tvExamine = (TextView) view.findViewById(R.id.examine);
        mIvActEntrance = (ImageView) view.findViewById(R.id.frag_me_activity_entrance);
        mIvActEntrance.setOnClickListener(this);
        mTvAsk = (TextView) view.findViewById(R.id.me_tv_ask);
        separateView = view.findViewById(R.id.separate);

        mRlRequirement = (RelativeLayout) view.findViewById(R.id.id_my_requirement);
        mTvaRequireCount = (TextView) view.findViewById(R.id.id_requirement_count);
        mRlRequirement.setOnClickListener(this);

        control.showKnowledgeShareIcon();

    }

    @Override
    public void onResume() {
        super.onResume();

        final String account = SysUtil.getPref(PrefKeyConst.PREF_ACCOUNT);
        final String password = SysUtil.getPref(PrefKeyConst.PREF_PASSWORD);

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            separateView.setVisibility(View.GONE);
            UserInfo userInfo = SysUtil.getUsrInfo();
            if (!TextUtils.isEmpty(userInfo.getHead())) {
                Picasso.with(getActivity()).load(userInfo.getHead()).resize(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60)).into(ivAvatar);
            } else {
                Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60)).into(ivAvatar);
            }
            if (userInfo.getIdentity() == 2) {
                expertInfoContainer.setVisibility(View.VISIBLE);
                toBeExpertContainer.setVisibility(View.GONE);
            } else {
                expertInfoContainer.setVisibility(View.GONE);
                toBeExpertContainer.setVisibility(View.VISIBLE);
            }
            edit_container.setVisibility(View.VISIBLE);
            login_container.setVisibility(View.VISIBLE);
            not_login_container.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(userInfo.getHead())) {
                Picasso.with(getActivity()).load(userInfo.getHead()).resize(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60)).into(ivAvatar);
            } else {
                Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(60), ScreenUtils.dip2px(60)).into(ivAvatar);
            }
            if (!TextUtils.isEmpty(userInfo.getName())) {
                tvName.setText(userInfo.getName());
            } else {
                tvName.setText(userInfo.getUsername());
            }
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                control.requestUserInfo();
            }
        } else {
            separateView.setVisibility(View.VISIBLE);
            tvOrderCount.setVisibility(View.GONE);
            tvExamine.setVisibility(View.GONE);
            tvServiceCount.setVisibility(View.GONE);
            expertInfoContainer.setVisibility(View.GONE);
            ivAvatar.setImageResource(R.drawable.ic_avatar_default);
            edit_container.setVisibility(View.INVISIBLE);
            not_login_container.setVisibility(View.VISIBLE);
            toBeExpertContainer.setVisibility(View.VISIBLE);
            login_container.setVisibility(View.GONE);
            mTvAsk.setVisibility(View.GONE);
        }

    }

    public void onSetKnowledgeState(ChildShareRespVo respVo) {
        if (respVo == null || TextUtils.isEmpty(respVo.getCharityExpert())) {
            mIvActEntrance.setVisibility(View.GONE);
        } else {
            mIvActEntrance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && control != null && SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            control.requestUserInfo();
        }

        if (!hidden) {
            control.showKnowledgeShareIcon();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == myExpertView) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActLogin.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), ActMyExpert.class);
                startActivity(intent);
            }
        } else if (v == myCollectionView) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActLogin.class);
                startActivity(intent);
            } else {
                goToCollection();
            }
        } else if (v == myCouponView) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActLogin.class);
                startActivity(intent);
            } else {
                startActivity(new Intent(getActivity(), ActCoupon.class));
            }
        } else if (v == helpView) {
            Intent intent = new Intent(getActivity(), ActHelp.class);
            startActivity(intent);
        } else if (v == ivSetting) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                startActivity(new Intent(getActivity(), ActSetting.class));
            }
        } else if (v == expertCenterView) {
            Intent intent = new Intent(getActivity(), ActExpertCenter.class);
            startActivity(intent);
        } else if (v == expertOrderView) {
            Intent intent = new Intent(getActivity(), ActExpertOrder.class);
            startActivity(intent);
        } else if (v == toBeExpertView) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                startActivity(new Intent(getActivity(), ActLogin.class));
            } else {
                goToToBeExpert();
            }
        } else if (v == login) {
            startActivity(new Intent(getActivity(), ActLogin.class));
        } else if (v == fill_data_container) {
            Intent intent = new Intent(getActivity(), ActUserBasic.class);
            intent.putExtra(IntentKey.INTENT_KEY_IS_EXAMINE, isExamine);
            startActivity(intent);
        } else if (v == edit_iv) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActUserBasic.class);
                intent.putExtra(IntentKey.INTENT_KEY_IS_EXAMINE, isExamine);
                startActivity(intent);
            }
        } else if (v == ivAvatar) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActUserBasic.class);
                intent.putExtra(IntentKey.INTENT_KEY_IS_EXAMINE, isExamine);
                startActivity(intent);
            } else {
                startActivity(new Intent(getActivity(), ActLogin.class));
            }
        } else if (v == mIvActEntrance) {
            Intent intent = new Intent(getActivity(), ChildrenShareWebActivity.class);
            startActivity(intent);
        } else if (v == askView) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                goToAskForward();
            } else {
                startActivity(new Intent(getActivity(), ActLogin.class));
            }

        } else if (v == mRlRequirement) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActUserRequirement.class);
                intent.putExtra(IntentKey.INTENT_KEY_IS_EXAMINE, isExamine);
                startActivity(intent);
            } else {
                startActivity(new Intent(getActivity(), ActLogin.class));
            }
        } else if (v == headEditView) {
            if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActUserBasic.class);
                intent.putExtra(IntentKey.INTENT_KEY_IS_EXAMINE, isExamine);
                startActivity(intent);
            } else {
                startActivity(new Intent(getActivity(), ActLogin.class));
            }
        }
    }

    private void goToCollection() {
        Intent intent = new Intent(getActivity(), ActEntrepreneurshipGuide.class);
        intent.putExtra(ExtraConst.FROM_WHERE, ExtraConst.FROM_MY_COLLECTION);
        startActivity(intent);
    }

    private void goToAskForward() {
        Intent intent = new Intent(getActivity(), AskRewardWebActivity.class);
        intent.putExtra(IntentKey.INTENT_KEY_ASK_REWARD_FROM, IntentKey.MY_ASK_LIST_URL);
        startActivity(intent);
    }

    private void goToToBeExpert() {
        if (isExamine) {
            startActivity(new Intent(getActivity(), ActExpertModifyInfo.class));
        } else {
            Intent intent = new Intent(getActivity(), ActToBeExpert.class);
            intent.putExtra(IntentKey.FROM_WHERE, IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_ME);
            startActivity(intent);
        }
    }

    public void setUserCenterInfo(UserCenterRespVo info) {
        if (info == null) {
            return;
        }
        int serviceCount = info.getNeedingServiceOrdersCnt();
        int orderCount = info.getPendingOrdersCnt();
        int requireCount = info.getDemandCount();
        int step = info.getUserDesc().getApplyStepValue();
        SysUtil.savePref(PrefKeyConst.PREF_APPLY_STEP, step);

        if (step >= 15) {
            separateView.setVisibility(View.GONE);
            isExamine = true;
            tvExamine.setVisibility(View.VISIBLE);
        } else {
            separateView.setVisibility(View.VISIBLE);
            isExamine = false;
            tvExamine.setVisibility(View.GONE);
        }
        if (serviceCount <= 0) {
            tvServiceCount.setVisibility(View.GONE);
        } else if (serviceCount <= 99) {
            tvServiceCount.setVisibility(View.VISIBLE);
            tvServiceCount.setText(String.valueOf(serviceCount));
        } else {
            tvServiceCount.setVisibility(View.VISIBLE);
            tvServiceCount.setText("99+");
        }
        if (orderCount <= 0) {
            tvOrderCount.setVisibility(View.GONE);
        } else if (orderCount <= 99) {
            tvOrderCount.setVisibility(View.VISIBLE);
            tvOrderCount.setText(String.valueOf(orderCount));
        } else {
            tvOrderCount.setVisibility(View.VISIBLE);
            tvOrderCount.setText("99+");
        }

        if (requireCount <= 0) {
            mTvaRequireCount.setVisibility(View.GONE);
        } else if (requireCount > 0 && requireCount < 99) {
            mTvaRequireCount.setVisibility(View.VISIBLE);
            mTvaRequireCount.setText(String.valueOf(requireCount));
        } else {
            mTvaRequireCount.setVisibility(View.VISIBLE);
            mTvaRequireCount.setText("99+");
        }

        if (info.getRewardCount() <= 0) {
            mTvAsk.setVisibility(View.GONE);
        } else {
            mTvAsk.setText(String.valueOf(info.getRewardCount()));
            mTvAsk.setVisibility(View.VISIBLE);
        }
        String company = info.getUserDesc().getCompany();
        String position = info.getUserDesc().getPosition();
        if (!TextUtils.isEmpty(company) && !TextUtils.isEmpty(position)) {
            tvJob.setVisibility(View.VISIBLE);
            tvJob.setText(position + "   |   " + company);
        } else {
            tvJob.setVisibility(View.GONE);
        }

        if (workAge != null && info.getUserDesc().getWorkage() != 0) {
            workAge_container.setVisibility(View.VISIBLE);
            workAge.setText(info.getUserDesc().getWorkage() + "年工作经验");
        } else {
            workAge_container.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(company) && TextUtils.isEmpty(position) && info.getUserDesc().getWorkage() == 0) {
            fill_data_container.setVisibility(View.VISIBLE);
            tvJob.setVisibility(View.GONE);
            workAge_container.setVisibility(View.GONE);
        } else {
            fill_data_container.setVisibility(View.GONE);
        }

        if (info.getUserDesc().getIdentity() == 2) {
            SysUtil.savePref(PrefKeyConst.PREF_USER_IDENTITY, 2);
            expertInfoContainer.setVisibility(View.VISIBLE);
            toBeExpertContainer.setVisibility(View.GONE);
        } else {
            SysUtil.savePref(PrefKeyConst.PREF_USER_IDENTITY, 1);
            expertInfoContainer.setVisibility(View.GONE);
            toBeExpertContainer.setVisibility(View.VISIBLE);
        }

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 1 && info.getBeconsultanttip() == 0) {
            DialogManager.showToBeExpertDialog(getActivity(), this);
        }

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2 && info.getConsultantFirstLogin() == 0) {
            DialogManager.showBecomeExpertDialog(getActivity(), this);
        }
    }

    @Override
    public void notifyTobeExpert() {
        control.notifyTobeExpert();
    }

    @Override
    public void tobeExpert() {
        Intent intent = new Intent(getActivity(), ActToBeExpert.class);
        intent.putExtra(IntentKey.FROM_WHERE, IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_ME);
        startActivity(intent);
    }

    @Override
    public void notifyBecomeExpert() {
        control.notifyBecomeExpert();
    }
}
