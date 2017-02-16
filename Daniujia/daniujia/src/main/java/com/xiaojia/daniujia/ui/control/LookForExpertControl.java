package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.AllIndustryVo;
import com.xiaojia.daniujia.domain.resp.SearchConditionVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.LookForExpertFragment;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by Administrator on 2016/4/26.
 */
public class LookForExpertControl extends BaseControl<LookForExpertFragment>{

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
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                showShortToast("网络超时");
                LogUtil.d("ZLOVE", "classifications---onError---e---" + e.toString());
            }
        });
    }

    public void initSearchData() {
        String url = Config.WEB_API_SERVER_V3 + "/experts/search/conditions";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<SearchConditionVo>() {

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                LogUtil.d("ZLOVE", "classifications---onFail---errorMsg---" + errorMsg);
            }

            @Override
            public void onResponse(SearchConditionVo result) {
                getFragment().refreshData(result);

            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                showShortToast("网络超时");
                LogUtil.d("ZLOVE", "classifications---onError---e---" + e.toString());
            }
        });
    }

}
