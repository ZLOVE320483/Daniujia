package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.ServiceSet;
import com.xiaojia.daniujia.domain.resp.ServiceDetailVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.StepFourthToBeExpertFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ConsultServiceControl extends BaseControl<StepFourthToBeExpertFragment>{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public  void modifyServiceType(ServiceSet ss) {
        String url = Config.WEB_API_SERVER_V3 + "/user/consultservice/add";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("msgserviceprice", ss.getMsgserviceprice());
        builder.add("msgservicestatus", ss.getMsgservicestatus());
        builder.add("telserviceprice", ss.getTelserviceprice());
        builder.add("telservicestatus", ss.getTelservicestatus());
        builder.add("offlineservicestatus",ss.getOfflineservicestatus());
        builder.add("offlineserviceprice", ss.getOfflineserviceprice());
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
            }
        }, builder);
    }

    public void getServiceDetail() {
        String url = Config.WEB_API_SERVER_V3 + "/user/expert/serviceproducts/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<ServiceDetailVo>() {
            @Override
            public void onResponse(ServiceDetailVo result) {
                getFragment().refreshView(result);
            }
        });
    }


}
