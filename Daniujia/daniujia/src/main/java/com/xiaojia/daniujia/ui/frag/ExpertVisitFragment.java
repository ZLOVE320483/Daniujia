package com.xiaojia.daniujia.ui.frag;

import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.ExpertVisitRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/6/3.
 */
public class ExpertVisitFragment extends BaseFragment {

    private TextView tvDailyVisit;
    private TextView tvMonthVisit;
    private TextView tvTotalVist;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_visit;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("主页访问");

        tvDailyVisit = (TextView) view.findViewById(R.id.id_today_visit);
        tvMonthVisit = (TextView) view.findViewById(R.id.id_month_visit);
        tvTotalVist = (TextView) view.findViewById(R.id.id_total_visit);

        requestExpertVisit();
    }

    private void setData(ExpertVisitRespVo respVo) {
        if (respVo == null) {
            return;
        }
        tvDailyVisit.setText(String.valueOf(respVo.getDailyPageVisitCnt()) + "次");
        tvMonthVisit.setText(String.valueOf(respVo.getMonthPageVisitCnt()) + "次");
        tvTotalVist.setText(String.valueOf(respVo.getTotalPageVisitCnt()) + "次");
    }

    private void requestExpertVisit() {
        String url = Config.WEB_API_SERVER + "/user/consultant/center/homepage/visits/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<ExpertVisitRespVo>() {
            @Override
            public void onResponse(ExpertVisitRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    setData(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
            }
        });
    }

}
