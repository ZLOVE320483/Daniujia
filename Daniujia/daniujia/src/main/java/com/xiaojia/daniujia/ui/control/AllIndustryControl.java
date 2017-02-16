package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.AllIndustryVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.AllIndustryFragment;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by Administrator on 2016/4/26.
 */
public class AllIndustryControl extends BaseControl<AllIndustryFragment>{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData() {
        String url = Config.WEB_API_SERVER_V3 + "/classifications/new";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<AllIndustryVo>() {

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                LogUtil.d("ZLOVE", "classifications---onFail---errorMsg---" + errorMsg);
            }

            @Override
            public void onResponse(AllIndustryVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().refreshData(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                LogUtil.d("ZLOVE", "classifications---onError---e---" + e.toString());
            }
        });
    }
}
