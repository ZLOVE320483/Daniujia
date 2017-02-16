package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.ServiceSet;
import com.xiaojia.daniujia.domain.resp.ServiceDetailVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.ExpertServiceSettingFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/6/28.
 */
public class ExpertServiceSettingControl extends BaseControl<ExpertServiceSettingFragment> {
    public  void modifyServiceType(ServiceSet ss, final boolean isChangeStatus) {
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
                if (isChangeStatus){
                    getFragment().modifyServiceStatus();
                } else {
                    getFragment().modifyService();
                }

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
