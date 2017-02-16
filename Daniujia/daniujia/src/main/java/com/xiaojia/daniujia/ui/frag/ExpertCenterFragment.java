package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertCenterRespVo;
import com.xiaojia.daniujia.ui.act.ActBalance;
import com.xiaojia.daniujia.ui.act.ActCollectMe;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActExpertModifyInfo;
import com.xiaojia.daniujia.ui.act.ActExpertVisit;
import com.xiaojia.daniujia.ui.control.ExpertCenterControl;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/5/7.
 */
public class ExpertCenterFragment extends BaseFragment implements View.OnClickListener {

    private ExpertCenterControl control;

    private View collectView;
    private TextView tvCollectMeCount;
    private View balanceView;
    private TextView tvBalance;
    private View visitView;
    private TextView tvVisitCount;
    private View expertHomeView;
    private View modifyInfoView;

    private float balance = 0.0f;
    private boolean needReq = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new ExpertCenterControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_center;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("专家管理中心");

        collectView = view.findViewById(R.id.collection_container);
        tvCollectMeCount = (TextView) view.findViewById(R.id.id_collect_me_count);
        balanceView = view.findViewById(R.id.balance_container);
        tvBalance = (TextView) view.findViewById(R.id.id_balance);
        visitView = view.findViewById(R.id.visit_container);
        tvVisitCount = (TextView) view.findViewById(R.id.id_today_visit);
        expertHomeView = view.findViewById(R.id.id_expert_home);
        modifyInfoView = view.findViewById(R.id.id_modify_info);

        collectView.setOnClickListener(this);
        balanceView.setOnClickListener(this);
        visitView.setOnClickListener(this);
        expertHomeView.setOnClickListener(this);
        control.requestExpertCenterInfo(true);
        modifyInfoView.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needReq) {
            control.requestExpertCenterInfo(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == collectView) {
            Intent intent = new Intent(getActivity(), ActCollectMe.class);
            startActivity(intent);
        } else if (v == balanceView) {
            needReq = true;
            Intent intent = new Intent(getActivity(), ActBalance.class);
            intent.putExtra(IntentKey.INTENT_KEY_BALANCE, balance);
            startActivity(intent);
        } else if (v == visitView) {
            Intent intent = new Intent(getActivity(), ActExpertVisit.class);
            startActivity(intent);
        } else if (v == expertHomeView) {
            Intent intent = new Intent(getActivity(), ActExpertHome.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID));
            startActivity(intent);
        } else if (v == modifyInfoView) {
            startActivity(new Intent(getActivity(), ActExpertModifyInfo.class));
        }
    }

    public void setExpertCenterData(ExpertCenterRespVo data) {
        if (data == null) {
            return;
        }
        balance = data.getActBalance();
        tvCollectMeCount.setText(String.valueOf(data.getFavoriteMeCnt()));
        tvBalance.setText(String.valueOf(balance));
        tvVisitCount.setText(String.valueOf(data.getDailyPageVisitCnt()));
    }
}
